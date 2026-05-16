package com.walcron

import io.ktor.server.engine.*
import io.ktor.server.cio.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

object Main {
    val aiEngine = Gemini()

    fun run() {
        embeddedServer(CIO, port = 8080) {
            routing {
                get("/") {
                    call.respondText("SC-300 Quiz Backend is Alive!")
                }
                get("/healthz") {
                    call.respondText("SC-300 Quiz Backend is Alive!")
                }
                get("/quizz") {
                    call.respondText(aiEngine.quizz())
                }
            }
        }.start(wait = true)
    }
}

fun main() {
    Main.run()
}