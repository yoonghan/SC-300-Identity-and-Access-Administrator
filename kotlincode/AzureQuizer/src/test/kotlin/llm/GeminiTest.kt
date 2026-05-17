package com.walcron.llm

import dev.mokkery.answering.returns
import dev.mokkery.mock
import dev.mokkery.every
import dev.mokkery.matcher.any
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertNull

class GeminiTest {
    @Test
    fun `should throw exception if gemini keys is not defined`() {
        val error = assertThrows<IllegalArgumentException> {
            Gemini()
        }
        assertThat(error.message, containsString("environment variable GOOGLE_API_KEY or GEMINI_API_KEY"))
    }

    @Test
    fun testQuizz() {
        val geminiClientWrapper = mock<ClientWrapper> {
            every { generateContent(
                "Explain how AI works in a few words",
                null
            ) } returns null
        }

        val gemini = Gemini(geminiClientWrapper)
        val response = gemini.quizz()
        assertNull(response)
    }

    @Test
    fun testQuizGenerate() {
        val quizQuestion = QuizQuestion("", "", emptyList(), 1, "")

        val geminiClientWrapper = mock<ClientWrapper> {
            every { generateContent(
                "Prompt me the first question.",
                any()
            ) } returns null
            every {generateQuizContent("Prompt me the first question.")} returns quizQuestion
        }

        val gemini = Gemini(geminiClientWrapper)
        val response = gemini.quizGenerate()
        assertThat(response, equalTo(quizQuestion))
    }
}