---
trigger: always_on
---

# A quiz bot in Kotlin using Ktor for SC-300 course.
The code structure for kotlin is under kotlincode folder.

## Architecture & Code Style
- **Framework:** Always use `Ktor` for web routing.
- **Clarity:** Use meaningful variable names. Maintain a "Clean Code" approach—keep functions small and focused.

## Azure & DevOps Preparation
- **Managed Identity:** When suggesting integrations, prioritize Azure Managed Identity (passwordless) over connection strings.
- **Containerization:** Always provide multi-stage `Dockerfiles` using `alpine` or `distroless` to keep images under 30MB for fast Azure Container App cold starts.

## Agent Behavior
- **Explain "Why":** Before implementing, briefly explain the architectural choice.
- **DRY Principle:** If you notice repetitive logic, suggest a Trait or a helper module.
- **Security:** Never hardcode secrets. Always use environment variables or `dotenv`.