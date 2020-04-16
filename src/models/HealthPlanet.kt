package net.resessh.healthCheckApi.models

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.ParametersBuilder
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol

const val healthPlanetHost = "www.healthplanet.jp"

data class OAuthTokens(val accessToken: String, val refreshToken: String)
data class OAuthTokensResponse(val access_token: String, val refresh_token: String, val expires_in: Int)

class HealthPlanet() {
  val authUrl
    get() = URLBuilder(
      host = healthPlanetHost,
      protocol = URLProtocol.HTTPS,
      encodedPath = "/oauth/auth",
      parameters = ParametersBuilder().also {
        it.append("client_id", Env.healthPlanetClientId)
        it.append("scope", "innerscan")
        it.append("response_type", "code")
        it.append("redirect_uri", "${Env.appOrigin}/")
      }
    ).buildString()

  suspend fun getTokensFromCode(code: String): OAuthTokens {
    val apiUrl = URLBuilder(
      host = healthPlanetHost,
      protocol = URLProtocol.HTTPS,
      encodedPath = "/oauth/token",
      parameters = ParametersBuilder().also {
        it.append("client_id", Env.healthPlanetClientId)
        it.append("client_secret", Env.healthPlanetClientSecret)
        it.append("code", code)
        it.append("redirect_uri", "https://$healthPlanetHost/success.html")
        it.append("grant_type", "authorization_code")
      }
    ).buildString()

    val client = HttpClient {
      install(JsonFeature) {
        serializer = JacksonSerializer()
      }
    }
    val response: OAuthTokensResponse = client.post(apiUrl) {
      accept(ContentType.Application.Json)
    }
    return OAuthTokens(response.access_token, response.refresh_token)
  }
}
