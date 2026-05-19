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
import io.ktor.server.http.content.default
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.enums.enumEntries
import kotlin.random.Random
import kotlinx.html.*

fun <T> engineHandler(message: T?) = message ?: "Please retry, engine broke down."

fun Application.module(aiEngine: LLMQuizzer) {
    configureRouting(aiEngine)
    install(ContentNegotiation) {
        json()
    }
}

private fun domainsAvailable(separator: String) = Domain.entries.joinToString(separator) { domain ->
    "$domain - ${domain.description}"
}

fun Application.configureRouting(aiEngine: LLMQuizzer) {
    routing {
        get("/") {
            call.respondText("SC-300 Quiz Backend is Alive!")
        }
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

        staticResources("/static", "static") {
            default("index.html") // Fallback file if someone hits http://localhost:8080/static/
        }
        get("/quiz/render/{domainId}") {
            val searchDomain = call.parameters["domainId"]
            val domain = enumEntries<Domain>().find { domain -> domain.name.equals(searchDomain, ignoreCase = true) }

            if(domain == null) {
                call.respond(HttpStatusCode.BadRequest,
                    Message("Domain $searchDomain not applicable. Only:${domainsAvailable("\n")}")
                )
            } else {
                val question = aiEngine.quizGenerate(domain)
                call.respondHtml(HttpStatusCode.OK) {
                    body {
                        // Call the separate reusable template layout component
                        div {
                            quizQuestionFragment(question!!, domain.description)
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
    }.start(wait = true)
}