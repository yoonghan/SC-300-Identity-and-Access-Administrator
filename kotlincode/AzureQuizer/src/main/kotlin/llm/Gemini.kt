package com.walcron.llm

import com.google.genai.Client
import com.google.genai.Models
import com.google.genai.types.*
import kotlinx.serialization.json.Json
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
        try {
            getModel().generateContent(
                geminiModel,
                text,
                config
            )
        } catch (ex: Exception) {
            logger.error(ex.message, ex)
            null
        }

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
            """Act as an expert Microsoft Certified Trainer specializing in the SC-300 (Microsoft Identity and Access Administrator) exam. Your task is to randomly generate realistic, scenario-based study questions and explanations.

For every generation, randomly select a topic from the SC-300 syllabus (e.g., Conditional Access, Privileged Identity Management, Entitlement Management, App Registrations, Identity Protection, or Cross-Tenant Access).

Output the response strictly in the following format:

**Topic:** [The specific SC-300 topic]
**Scenario:** [A 2-3 sentence real-world problem a cloud administrator would face]
**Question:** [The specific question the student must answer]
**Explanation:** [A clear, concise answer explaining the "Why" and the "How"]
**Extra Note:** [1-2 bullet points with edge cases, limitations, or best practices related to the scenario]
                    """,
            null
        )
        return response?.text()
    }

    override fun quizGenerate(domain:Domain): QuizQuestion? {
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
                Content.fromParts(Part.fromText("Act as an expert Microsoft Certified Trainer specializing in the SC-300 (Microsoft Identity and Access Administrator) exam. Your task is to randomly generate realistic, scenario-based study questions and explanations.")))
            .responseMimeType(ContentType.Application.Json.toString())
            .responseJsonSchema(parsedSchemaMap)
            .candidateCount(1)


        return client.generateQuizContent(
            "Generate a challenging Microsoft SC-300 exam question focusing explicitly on: ${domain.focus}", configBuilder
        )
    }
}