package com.walcron

import com.walcron.llm.LLMQuizzer
import com.walcron.llm.QuizQuestion
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ContentNegotiation
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    val quizQuestion = QuizQuestion("", "", emptyList(), 1, "")

    val llmQuizzer = mock<LLMQuizzer> {
        every { quizz() } returns "A quiz was sent!"
        every { quizGenerate() } returns quizQuestion
    }

    @Test
    fun testRoot(): Unit = testApplication {
        application {
            module(llmQuizzer)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("SC-300 Quiz Backend is Alive!", response.bodyAsText())
    }

    @Test
    fun testHealth(): Unit = testApplication {
        application {
            module(llmQuizzer)
        }
        val response = client.get("/healthz")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("SC-300 Quiz Backend is Alive!", response.bodyAsText())
    }

    @Test
    fun testQuiz(): Unit = testApplication {
        application {
            module(llmQuizzer)
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
            module(failedLlmQuizzer)
        }
        val response = client.get("/quizz")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Please retry, engine broke down.", response.bodyAsText())
    }

    @Test
    fun testQuizGenerate(): Unit = testApplication {
        application {
            module(llmQuizzer)
        }

        val jsonClient = createClient {
            this.install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json()
            }
        }

        val response = jsonClient.get("/quiz/generate")
        assertEquals(HttpStatusCode.OK, response.status)
        val returnedQuestion: QuizQuestion = response.body<QuizQuestion>()
        assertEquals(quizQuestion, returnedQuestion)
    }

    @Test
    fun `test quiz generate has no response will send you a retry`(): Unit = testApplication {
        val failedLlmQuizzer = mock<LLMQuizzer> {
            every { quizGenerate() } returns null
        }
        application {
            module(failedLlmQuizzer)
        }
        val response = client.get("/quiz/generate")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Please retry, engine broke down.", response.bodyAsText())
    }
}