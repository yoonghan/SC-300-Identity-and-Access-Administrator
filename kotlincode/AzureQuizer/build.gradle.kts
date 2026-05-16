plugins {
    kotlin("jvm") version "2.3.21"
    id("dev.mokkery") version "3.3.0"
}

group = "com.walcron"
version = "1.0-SNAPSHOT"
val ktorVersion = "3.4.3"

repositories {
    mavenCentral()
}

dependencies {
    // Gemini
    implementation("com.google.genai:google-genai:1.53.0")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.5.16")

    // The core server engine (CIO = Coroutine-based I/O)
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-apache5:$ktorVersion")

    // For handling incoming/outgoing JSON payloads natively
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    // testing
    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")

}

tasks.test {
    useJUnitPlatform()
}