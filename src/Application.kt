package net.resessh.healthCheckApi

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.coroutines.async
import net.resessh.healthCheckApi.models.HealthPlanet

fun main(args: Array<String>): Unit = io.ktor.server.jetty.EngineMain.main(args)

val healthPlanet = HealthPlanet()

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
      this.call.respond(mapOf("hello" to "world"))
    }

    get("/auth/start") {
      this.call.respondRedirect(healthPlanet.authUrl, false)
    }

    get("/auth/update") {
      val code = this.call.request.queryParameters["code"]
      if (code == null) {
        this.call.respond(HttpStatusCode.BadRequest)
        return@get
      }

      val tokens = async {
        healthPlanet.getTokensFromCode(code)
      }.await()

      this.call.respondText(tokens.accessToken, ContentType.Application.Json)
    }
  }
}
