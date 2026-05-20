plugins {
    application
    kotlin("jvm") version "2.3.21"
    id("dev.mokkery") version "3.3.0"
    kotlin("plugin.serialization") version "2.3.21"
}

application {
    mainClass.set("com.walcron.MainKt")
}

group = "com.walcron"
version = "1.0-SNAPSHOT"

val ktorVersion = "3.5.0"

repositories {
    mavenCentral()
}

dependencies {
    // Gemini
    implementation("com.google.genai:google-genai:1.54.0")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.5.32")

    // The core server engine (CIO = Coroutine-based I/O)
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-apache5:$ktorVersion")

    // For handling incoming/outgoing JSON payloads natively
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    // To load HTMLX
    implementation("io.ktor:ktor-server-html-builder:${ktorVersion}")

    // testing
    testImplementation(kotlin("test"))
    testImplementation("org.hamcrest:hamcrest:3.0")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation:${ktorVersion}")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}