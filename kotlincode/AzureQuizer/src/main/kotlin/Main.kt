package com.walcron

import com.walcron.llm.Domain
import com.walcron.llm.Gemini
import com.walcron.llm.LLMQuizzer
import com.walcron.llm.quizQuestionFragment
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.enums.enumEntries
import kotlin.random.Random
import kotlinx.html.*
import io.ktor.server.metrics.micrometer.*
import io.micrometer.core.instrument.Metrics
import io.micrometer.core.instrument.Timer
import io.micrometer.core.instrument.composite.CompositeMeterRegistry
import io.micrometer.core.instrument.logging.LoggingMeterRegistry
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.instrumentation.ktor.v3_0.KtorServerTelemetry
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk
import io.opentelemetry.semconv.ServiceAttributes

fun <T> engineHandler(message: T?) = message ?: "Please retry, engine broke down."

fun getOpenTelemetry(serviceName: String): OpenTelemetry {
    return AutoConfiguredOpenTelemetrySdk.builder().addResourceCustomizer { oldResource, _ ->
        oldResource.toBuilder()
            .putAll(oldResource.attributes)
            .put(ServiceAttributes.SERVICE_NAME, serviceName)
            .build()
    }.build().openTelemetrySdk
}

fun Application.configureTelemetry() {
    val loggingRegistry = LoggingMeterRegistry()

    val compositeRegistry = CompositeMeterRegistry().apply {
        add(loggingRegistry)
    }
    Metrics.addRegistry(compositeRegistry)

    // 2. Install the Micrometer plugin directly onto the Ktor pipeline
    install(MicrometerMetrics) {
        registry = compositeRegistry

        // Automatically attach structural tag labels to your metrics
        timers { call, exception ->
            tag("route", call.request.local.uri)
            tag("status", call.response.status()?.value?.toString() ?: "200")
            if (exception != null) tag("exception", exception.javaClass.simpleName)
        }
    }
}

fun Application.module(aiEngine: LLMQuizzer) {
    configureRouting(aiEngine)
    configureTelemetry()
    install(ContentNegotiation) {
        json()
    }
}

private fun domainsAvailable(separator: String) = Domain.entries.joinToString(separator) { domain ->
    "$domain - ${domain.description}"
}

fun Application.configureRouting(aiEngine: LLMQuizzer) {
    routing {
        get("/healthz") {
            call.respondText("SC-300 Quiz Backend is Alive!")
        }
        get("/quizz") {
            call.respond(HttpStatusCode.OK, engineHandler(aiEngine.quizz()))
        }
        get("/quiz/domains") {
            call.respondText(domainsAvailable("<br>"), ContentType.Text.Html)
        }
        get("/quiz/generate") {
            val randomDomain = Random.nextInt(0, 3)
            val domain = enumEntries<Domain>()[randomDomain]
            call.respond(HttpStatusCode.OK, engineHandler(aiEngine.quizGenerate(domain)))
        }
        get("/quiz/generate/{domainId}") {
            val searchDomain = call.parameters["domainId"]
            val domain = enumEntries<Domain>().find { domain -> domain.name.equals(searchDomain, ignoreCase = true) }

            if(domain == null) {
                call.respond(HttpStatusCode.BadRequest,
                    Message("Domain $searchDomain not applicable. Only:${domainsAvailable("\n")}")
                )
            } else {
                call.respond(HttpStatusCode.OK, engineHandler(aiEngine.quizGenerate(domain)))
            }
        }

        staticResources("/", "static") {
            default("index.html")
        }

        get("/quiz/render/{domainId}") {
            val searchDomain = call.parameters["domainId"]
            val domain = enumEntries<Domain>().find { domain -> domain.name.equals(searchDomain, ignoreCase = true) }

            if(domain == null) {
                call.respondHtml(HttpStatusCode.OK) {
                    body {
                        div {
                            classes = setOf("text-red-600")
                            + "Domain $searchDomain is not valid."
                        }
                    }
                }
            } else {

                val geminiTimer = Timer.builder("ai.gemini.request")
                    .description("Tracks the total processing latency of the Google GenAI backend model computation")
                    .tag("domain", domain.name)
                    .register(Metrics.globalRegistry)

                val question = geminiTimer.recordCallable {
                    aiEngine.quizGenerate(domain)
                }

                question?.let {
                    call.respondHtml(HttpStatusCode.OK) {
                        body {
                            div {
                                quizQuestionFragment(question, domain.description)
                            }
                        }
                    }
                } ?: run {
                    call.respondHtml(HttpStatusCode.OK) {
                        body {
                            div {
                                classes = setOf("text-red-600")
                                + "Fail to provide question and answer."
                            }
                        }
                    }
                }
            }
        }
    }
}

fun main() {
    val aiEngine = Gemini()

    embeddedServer(CIO, port = 8080) {
        module(aiEngine)
        val openTelemetry = getOpenTelemetry(serviceName = "sc-300-quizz")

        install(KtorServerTelemetry){
            setOpenTelemetry(openTelemetry)
        }
    }.start(wait = true)
}