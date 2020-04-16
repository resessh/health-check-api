package net.resessh.healthCheckApi.models

object Env {
  val healthPlanetClientId =
    mustGetEnv("HEALTH_PLANET_CLIENT_ID")
  val healthPlanetClientSecret =
    mustGetEnv("HEALTH_PLANET_CLIENT_SECRET")
  val appOrigin = System.getenv("APP_ORIGIN") ?: "http://api.health.resessh.net/"

  private fun mustGetEnv(key: String) = System.getenv(key) ?: throw Exception("Environment variables not found: $key")
}
