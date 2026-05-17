package com.walcron

import com.walcron.llm.LLMQuizzer
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    val llmQuizzer = mock<LLMQuizzer> {
        every { quizz() } returns "A quiz was sent!"
        every { quizGenerate() } returns "Generate a quiz!"
    }

    @Test
    fun testRoot(): Unit = testApplication {
        application {
            configureRouting(llmQuizzer)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("SC-300 Quiz Backend is Alive!", response.bodyAsText())
    }

    @Test
    fun testHealth(): Unit = testApplication {
        application {
            configureRouting(llmQuizzer)
        }
        val response = client.get("/healthz")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("SC-300 Quiz Backend is Alive!", response.bodyAsText())
    }

    @Test
    fun testQuiz(): Unit = testApplication {
        application {
            configureRouting(llmQuizzer)
        }
        val response = client.get("/quizz")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("A quiz was sent!", response.bodyAsText())
    }

    @Test
    fun `test quiz has no response will send you a retry`(): Unit = testApplication {
        val failedLlmQuizzer = mock<LLMQuizzer> {
            every { quizz() } returns null
        }
        application {
            configureRouting(failedLlmQuizzer)
        }
        val response = client.get("/quizz")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Please retry, engine broke down.", response.bodyAsText())
    }

    @Test
    fun testQuizGenerate(): Unit = testApplication {
        application {
            configureRouting(llmQuizzer)
        }
        val response = client.get("/quiz/generate")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Generate a quiz!", response.bodyAsText())
    }
}