package net.resessh.healthCheckApi

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.routing.get
import io.ktor.http.ContentType
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.jackson.jackson
import io.ktor.features.ContentNegotiation

fun main(args: Array<String>): Unit = io.ktor.server.jetty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
  install(ContentNegotiation) {
    jackson {
      enable(SerializationFeature.INDENT_OUTPUT)
    }
  }

  routing {
    get("/") {
      call.respondText("Hello, world!", contentType = ContentType.Text.Plain)
    }

    get("/json/jackson") {
      call.respond(mapOf("hello" to "world"))
    }
  }
}
