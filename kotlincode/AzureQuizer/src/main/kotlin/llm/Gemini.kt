package com.walcron.llm

import com.google.genai.Client
import com.google.genai.types.GenerateContentResponse
import com.walcron.LLMQuizzer
import kotlin.system.exitProcess

class Gemini: LLMQuizzer {
    var client: Client

    init {
        if (System.getenv("GEMINI_API_KEY") == null) {
            System.err.println("Missing GEMINI_API_KEY!")
            exitProcess(1)
        }

        client = Client()
    }

    override fun quizz(): String {
        val response: GenerateContentResponse? = client.models.generateContent(
            "gemini-3-flash-preview",
            "Explain how AI works in a few words",
            null
        )
        return response?.text() ?: "I can't answer anything now."
    }
}