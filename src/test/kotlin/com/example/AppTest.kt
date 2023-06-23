package com.example

import org.http4k.client.OkHttp
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.junit.jupiter.api.Test

class AppTest {

    @Test
    fun testReturnsHello() {
        val client = OkHttp()

        val response = app(
            Request(GET, "http://localhost:9000/")
        )

        assert(response.bodyString() == "Hello")
    }
}
