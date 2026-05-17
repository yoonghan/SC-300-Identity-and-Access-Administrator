package com.walcron.llm

import com.google.genai.Client
import com.google.genai.Models
import com.google.genai.types.*

interface ClientWrapper {
    fun generateContent(text: String, config: GenerateContentConfig?): GenerateContentResponse?
}

class GeminiClientWrapper(val client: Client = Client(), val geminiModel: String) : ClientWrapper {
    private fun getModel(): Models = client.models

    override fun generateContent(text: String, config: GenerateContentConfig?): GenerateContentResponse? =
        getModel().generateContent(
            geminiModel,
            text,
            config
        )
}

class Gemini(val client: ClientWrapper = GeminiClientWrapper(Client(), "gemini-3-flash-preview")) : LLMQuizzer {
    override fun quizz(): String? {
        val response = client.generateContent(
            "Explain how AI works in a few words",
            null
        )
        return response?.text()
    }

    override fun quizGenerate(): String? {
        val quizSchema = Schema.builder()
            .build()

        val config = GenerateContentConfig.builder()
            .thinkingConfig(ThinkingConfig.builder().thinkingLevel(ThinkingLevel("low")))
            .systemInstruction(
                Content.fromParts(Part.fromText("You are an expert Azure Security Architect writing questions for the SC-300 exam."))
            )
            .responseMimeType("application/json")
            .responseSchema(quizSchema)
            .build()

        val response: GenerateContentResponse? = client.generateContent(
            "Prompt me the first question.",
            config
        )

        return response?.text()
    }
}