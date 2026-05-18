package com.walcron.llm

import com.google.genai.types.GenerateContentConfig
import com.google.genai.types.ThinkingLevel
import dev.mokkery.answering.returns
import dev.mokkery.mock
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.matcher.capture.Capture.Companion.slot
import dev.mokkery.matcher.capture.capture
import dev.mokkery.verify
import io.ktor.util.reflect.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import kotlin.jvm.optionals.getOrNull
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
            every {
                generateContent(
                    "Explain how AI works in a few words",
                    null
                )
            } returns null
        }

        val gemini = Gemini(geminiClientWrapper)
        val response = gemini.quizz()
        assertNull(response)
    }

    @Test
    fun testQuizGenerate() {
        val quizQuestion = QuizQuestion("", "", emptyList(), 1, "")

        val captor = slot<GenerateContentConfig.Builder>()
        val geminiClientWrapper = mock<ClientWrapper> {
            every {
                generateQuizContent(any(), capture(captor))
            } returns quizQuestion
        }

        val gemini = Gemini(geminiClientWrapper)
        val response = gemini.quizGenerate()
        assertThat(response, equalTo(quizQuestion))
        verify {
            geminiClientWrapper.generateQuizContent("Challenge me with a quiz question.", any())
        }
        val config = captor.values.firstOrNull()?.build()
        assertThat(config?.candidateCount()?.getOrNull(), equalTo(1))
        assertThat(config?.responseMimeType()?.getOrNull(), equalTo("application/json"))
        assertThat(config?.thinkingConfig()?.getOrNull()?.thinkingLevel()?.getOrNull(), equalTo(ThinkingLevel("low")))
        assertThat(config?.systemInstruction()?.getOrNull()?.text(), equalTo("You are an expert Azure Security Architect writing questions for the SC-300 exam."))
        assertTrue(config?.responseJsonSchema()?.getOrNull()?.instanceOf(Map::class) ?: false)

        val schema = config?.responseJsonSchema()!!.get() as Map<*, *>
        assertThat(schema["type"], equalTo("object"))
        assertThat(schema["required"], equalTo(listOf("scenario", "question", "options", "correctIndex", "explanation")))
        with(schema["properties"] as Map<*, *>) {
            assertThat(this["question"], equalTo(mapOf(
                "type" to "string",
                "description" to "The core exam multiple-choice problem statement text."
            )))

            assertThat(this["scenario"], equalTo(mapOf(
                "type" to "string",
                "description" to "The enterprise infrastructure identity situation baseline context."
            )))

            assertThat(this["options"], equalTo(mapOf(
                "type" to "array",
                "items" to mapOf("type" to "string"),
                "description" to "Exactly 4 distinct answer choice string items."
            )))

            assertThat(this["correctIndex"], equalTo(mapOf(
                "type" to "integer",
                "description" to "The ZERO-BASED index integer (0, 1, 2, or 3) pointing to the correct choice inside the options array."
            )))

            assertThat(this["explanation"], equalTo(mapOf(
                "type" to "string",
                "description" to "Detailed technical analysis justifying the answer based on Microsoft SC-300 study guidelines."
            )))
        }

    }
}