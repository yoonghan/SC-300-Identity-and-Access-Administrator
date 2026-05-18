package com.walcron.llm

interface LLMQuizzer {
    fun quizz(): String?
    fun quizGenerate(domain:Domain): QuizQuestion?
}

enum class Domain(val description: String, val focus: String) {
    identity("Implement and manage user identities", "Microsoft Entra Connect hybrid sync, user/group lifecycles, and External Identities (B2B/B2C)."),
    access("Implement authentication and access management", "Conditional Access Policies (CAP), Multi-Factor Authentication (MFA), passwordless, and device registration states like Microsoft Entra Join."),
    apps("Plan and implement workload identities", "Enterprise Applications, App Registrations, API permissions, Consent frameworks, and Managed Identities."),
    governance("Plan and automate identity governance", "Privileged Identity Management (PIM) eligibility/activation lifecycles, Entitlement Management access packages, and Access Reviews configuration settings.")
}