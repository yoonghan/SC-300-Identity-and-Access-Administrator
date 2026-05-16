plugins {
    kotlin("jvm") version "2.3.21"
}

group = "com.walcron"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // Gemini
    implementation("com.google.genai:google-genai:1.53.0")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.5.16")

    // The core server engine (CIO = Coroutine-based I/O)
    implementation("io.ktor:ktor-server-core:3.4.3")
    implementation("io.ktor:ktor-server-cio:3.4.3")

    // For handling incoming/outgoing JSON payloads natively
    implementation("io.ktor:ktor-server-content-negotiation:3.4.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.4.3")
}

tasks.test {
    useJUnitPlatform()
}