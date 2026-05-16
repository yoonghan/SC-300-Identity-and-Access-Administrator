package com.walcron

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import kotlin.system.exitProcess

class Gemini {
    var client: Client

    init {
        if (System.getenv("GEMINI_API_KEY") == null) {
            System.err.println("Missing GEMINI_API_KEY!")
            exitProcess(1)
        }

        client = Client()
    }

    fun quizz(): String {
        val response: GenerateContentResponse? = client.models.generateContent(
            "gemini-3-flash-preview",
            "Explain how AI works in a few words",
            null
        )
        return response?.text() ?: "I can't answer anything now."
    }
}