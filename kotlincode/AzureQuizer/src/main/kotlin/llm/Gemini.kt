package com.walcron.llm

import com.google.genai.Client
import com.google.genai.Models
import com.google.genai.types.*
import kotlinx.serialization.json.Json
import com.google.genai.shaded.jackson.module.kotlin.ExtensionsKt.jacksonObjectMapper
import io.ktor.http.ContentType
import org.slf4j.LoggerFactory

interface ClientWrapper {
    fun generateContent(text: String, config: GenerateContentConfig?): GenerateContentResponse?
    fun generateQuizContent(text: String, generateConfigBuilder: GenerateContentConfig.Builder): QuizQuestion?
}

class GeminiClientWrapper(val client: Client = Client(), val geminiModel: String) : ClientWrapper {
    private fun getModel(): Models = client.models
    private val logger = LoggerFactory.getLogger(GeminiClientWrapper::class.java)

    override fun generateContent(text: String, config: GenerateContentConfig?): GenerateContentResponse? =
        getModel().generateContent(
            geminiModel,
            text,
            config
        )

    override fun generateQuizContent(text: String, generateConfigBuilder: GenerateContentConfig.Builder): QuizQuestion? {
        val response: GenerateContentResponse? = generateContent(text, generateConfigBuilder.build())

        val jsonText = response?.text()
        logger.info("Generated content json: $jsonText")

        return jsonText?.let { Json.decodeFromString<QuizQuestion>(jsonText) }
    }
}

class Gemini(val client: ClientWrapper = GeminiClientWrapper(Client(), "gemini-3-flash-preview")) : LLMQuizzer {
    override fun quizz(): String? {
        val response = client.generateContent(
            "Explain how AI works in a few words",
            null
        )
        return response?.text()
    }

    override fun quizGenerate(): QuizQuestion? {
        val parsedSchemaMap = mapOf(
            "type" to "object",
            "properties" to mapOf(
                "scenario" to mapOf(
                    "type" to "string",
                    "description" to "The enterprise infrastructure identity situation baseline context."
                ),
                "question" to mapOf(
                    "type" to "string",
                    "description" to "The core exam multiple-choice problem statement text."
                ),
                "options" to mapOf(
                    "type" to "array",
                    "items" to mapOf("type" to "string"),
                    "description" to "Exactly 4 distinct answer choice string items."
                ),
                "correctIndex" to mapOf(
                    "type" to "integer",
                    "description" to "The ZERO-BASED index integer (0, 1, 2, or 3) pointing to the correct choice inside the options array."
                ),
                "explanation" to mapOf(
                    "type" to "string",
                    "description" to "Detailed technical analysis justifying the answer based on Microsoft SC-300 study guidelines."
                )
            ),
            "required"  to listOf("scenario", "question", "options", "correctIndex", "explanation")
        )

        val configBuilder = GenerateContentConfig.builder()
            .thinkingConfig(ThinkingConfig.builder().thinkingLevel(ThinkingLevel("low")))
            .systemInstruction(
                Content.fromParts(Part.fromText("You are an expert Azure Security Architect writing questions for the SC-300 exam.")))
            .responseMimeType(ContentType.Application.Json.toString())
            .responseJsonSchema(parsedSchemaMap)
            .candidateCount(1)


        return client.generateQuizContent(
            "Challenge me with a quiz question.", configBuilder
        )
    }
}