package com.walcron.llm

import kotlinx.html.*

// A clean, isolated layout component that only handles presentation
fun DIV.quizQuestionFragment(question: QuizQuestion, domain: String) {
    id = "quiz-card"
    classes = setOf("space-y-4", "p-6", "bg-white", "rounded-xl", "shadow-md", "border", "border-gray-100")

    span {
        classes = setOf("inline-block", "px-3", "py-1", "text-xs", "font-semibold", "text-blue-700", "bg-blue-100", "rounded-full", "uppercase")
        + "$domain domain"
    }

    p {
        classes = setOf("text-sm", "italic", "text-gray-500", "bg-gray-50", "p-3", "rounded-lg", "border-l-4", "border-gray-300")
        + question.scenario
    }

    h3 {
        classes = setOf("text-lg", "font-bold", "text-gray-800")
        + question.question
    }

    form {
        classes = setOf("space-y-2")
        question.options.forEachIndexed { index, optionText ->
            label {
                classes = setOf("flex", "items-center", "space-x-3", "p-3", "bg-gray-50", "rounded-lg", "border", "border-gray-200", "cursor-pointer", "hover:bg-blue-50", "transition-colors")
                input(type = InputType.radio) {
                    name = "quiz-option"
                    value = index.toString()
                    classes = setOf("text-blue-600", "focus:ring-blue-500")
                }
                span {
                    classes = setOf("text-gray-700")
                    + optionText
                }
            }
            br
        }
    }

    button {
        classes = setOf("mt-4", "px-4", "py-2", "bg-gray-800", "text-white", "text-sm", "font-medium", "rounded-lg", "hover:bg-gray-700", "transition")
        // Pure client-side toggle instruction embedded in standard markup attributes
        attributes["onclick"] = "document.getElementById('answer-pane').classList.remove('hidden'); this.remove();"
        + "Reveal Answer"
    }

    div {
        id = "answer-pane"
        classes = setOf("hidden", "mt-4", "p-4", "bg-emerald-50", "rounded-lg", "border", "border-emerald-200")
        p {
            classes = setOf("text-emerald-800", "font-bold", "mb-1")
            + "Correct Choice Index: Option #${question.correctIndex + 1}"
        }
        p {
            classes = setOf("text-gray-600", "text-sm")
            + question.explanation
        }
    }
}