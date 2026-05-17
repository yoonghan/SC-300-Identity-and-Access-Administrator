package com.walcron.llm

interface LLMQuizzer {
    fun quizz(): String?
    fun quizGenerate(): QuizQuestion?
}