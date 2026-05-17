package com.walcron.llm

import kotlinx.serialization.Serializable

@Serializable
data class QuizQuestion(
    val scenario: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)