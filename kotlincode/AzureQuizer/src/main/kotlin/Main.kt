package com.walcron

import com.walcron.llm.Gemini
import com.walcron.llm.LLMQuizzer
import io.ktor.server.application.Application
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(aiEngine: LLMQuizzer) {
    routing {
        get("/") {
            call.respondText("SC-300 Quiz Backend is Alive!")
        }
        get("/healthz") {
            call.respondText("SC-300 Quiz Backend is Alive!")
        }
        get("/quizz") {
            call.respondText(aiEngine.quizz() ?: "Please retry, engine broke down.")
        }
    }
}

fun main() {
    val aiEngine = Gemini()

    embeddedServer(CIO, port = 8080) {
        configureRouting(aiEngine)
    }.start(wait = true)
}