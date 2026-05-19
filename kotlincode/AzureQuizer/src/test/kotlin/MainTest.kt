package com.walcron

import com.walcron.llm.Domain
import com.walcron.llm.LLMQuizzer
import com.walcron.llm.QuizQuestion
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.ofType
import dev.mokkery.mock
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.StringContains.containsString
import kotlin.test.*

class ApplicationTest {
    val quizQuestion = QuizQuestion("", "", emptyList(), 1, "")

    val llmQuizzer = mock<LLMQuizzer> {
        every { quizz() } returns "A quiz was sent!"
        every { quizGenerate(Domain.identity) } returns quizQuestion
    }

    @Test
    fun testRoot(): Unit = testApplication {
        application {
            module(llmQuizzer)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertThat(response.bodyAsText(), containsString("!DOCTYPE html"))
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
    fun testDomains(): Unit = testApplication {
        application {
            module(llmQuizzer)
        }
        val response = client.get("/quiz/domains")
        assertEquals(HttpStatusCode.OK, response.status)
        assertThat(response.body(), containsString("identity - Implement and manage user identities<br>"))
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
        val llmQuizzer = mock<LLMQuizzer> {
            every { quizGenerate(ofType<Domain>()) } returns quizQuestion
        }
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
            every { quizGenerate(ofType<Domain>()) } returns null
        }
        application {
            module(failedLlmQuizzer)
        }
        val response = client.get("/quiz/generate")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Please retry, engine broke down.", response.bodyAsText())
    }

    @Test
    fun testQuizGenerateByDomainCaseInsensitive(): Unit = testApplication {
        application {
            module(llmQuizzer)
        }

        val jsonClient = createClient {
            this.install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json()
            }
        }

        val response = jsonClient.get("/quiz/generate/idEntity")
        assertEquals(HttpStatusCode.OK, response.status)
        val returnedQuestion: QuizQuestion = response.body<QuizQuestion>()
        assertEquals(quizQuestion, returnedQuestion)
    }

    @Test
    fun `test quiz generate by domain has no response will send you a retry`(): Unit = testApplication {
        val failedLlmQuizzer = mock<LLMQuizzer> {
            every { quizGenerate(Domain.identity) } returns null
        }
        application {
            module(failedLlmQuizzer)
        }
        val response = client.get("/quiz/generate/identity")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Please retry, engine broke down.", response.bodyAsText())
    }

    @Test
    fun `shows exception if domains for generate is not recognized`(): Unit = testApplication {
        val failedLlmQuizzer = mock<LLMQuizzer> {
            every { quizGenerate(Domain.identity) } returns null
        }
        application {
            module(failedLlmQuizzer)
        }
        val response = client.get("/quiz/generate/eoeoeo")
        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertThat(response.bodyAsText(), containsString("{\"error\":\"Domain eoeoeo not applicable. Only:identity - Implement and manage user identities\\n"))
    }
}