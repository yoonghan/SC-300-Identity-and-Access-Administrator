package com.walcron

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun testRoot(): Unit = testApplication {
        application {
            configureRouting()
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("SC-300 Quiz Backend is Alive!", response.bodyAsText())
    }

    @Test
    fun testHealth(): Unit = testApplication {
        val response = client.get("/healthz")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("SC-300 Quiz Backend is Alive!", response.bodyAsText())
    }
}