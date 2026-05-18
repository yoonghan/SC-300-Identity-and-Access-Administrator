package com.walcron.llm

interface LLMQuizzer {
    fun quizz(): String?
    fun quizGenerate(): QuizQuestion?
}

enum class Domains(val description: String) {
    identity("Implement and manage user identities"),
    access("Implement authentication and access management"),
    apps("Plan and implement workload identities"),
    governance("Plan and automate identity governance")
}