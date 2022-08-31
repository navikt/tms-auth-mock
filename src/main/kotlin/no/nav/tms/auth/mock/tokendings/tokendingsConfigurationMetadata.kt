package no.nav.tms.auth.mock.tokendings

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class TokendingsMetadataBuilder(private val localUrl: String, private val internalDockerUrl: String?, private val publicJwk: String) {

    fun localConfigurationMetadata(): TokendingsConfigurationMetadata {

        val tokenUrl = "$localUrl/tokendings/token"
        val jwksUrl = "$localUrl/tokendings/jwks"

        return TokendingsConfigurationMetadata(tokenUrl, localUrl, jwksUrl)
    }

    fun dockerConfigurationMetadata(): TokendingsConfigurationMetadata {
        if (internalDockerUrl == null) {
            throw RuntimeException("Url in docker environment was not set")
        }

        val tokenUrl = "$internalDockerUrl/tokendings/token"
        val jwksUrl = "$internalDockerUrl/tokendings/jwks"

        return TokendingsConfigurationMetadata(tokenUrl, internalDockerUrl, jwksUrl)
    }

    fun createJwksMetadata(): JwksMetadata {
        val jwkAsJson = Json.parseToJsonElement(publicJwk)

        return JwksMetadata(listOf(jwkAsJson))
    }
}

@Serializable
data class TokendingsConfigurationMetadata(
        @SerialName("token_endpoint") val tokenEndpoint: String,
        @SerialName("issuer") val issuer: String,
        @SerialName("jwks_uri") val jwksUri: String = ""
)

@Serializable
data class JwksMetadata(
    val keys: List<JsonElement>
)
