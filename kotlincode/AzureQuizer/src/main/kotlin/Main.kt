package com.walcron

import com.walcron.llm.Gemini
import com.walcron.llm.LLMQuizzer
import com.walcron.llm.QuizQuestion
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun <T> engineHandler(message: T?) = message ?: "Please retry, engine broke down."

fun Application.module(aiEngine: LLMQuizzer) {
    configureRouting(aiEngine)
    install(ContentNegotiation) {
        json()
    }
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
        get("/quiz/generate") {
            call.respond(HttpStatusCode.OK, engineHandler(aiEngine.quizGenerate()))
        }
    }
}

fun main() {
    val aiEngine = Gemini()

    embeddedServer(CIO, port = 8080) {
        module(aiEngine)
    }.start(wait = true)
}