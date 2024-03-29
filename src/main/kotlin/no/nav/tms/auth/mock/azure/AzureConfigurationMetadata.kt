package no.nav.tms.auth.mock.azure

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import java.security.interfaces.RSAPublicKey

class AzureMetadataBuilder(private val localUrl: String, private val internalDockerUrl: String?, private val publicJwk: String) {

    fun localConfigurationMetadata(): AzureConfigurationMetadata {

        val tokenUrl = "$localUrl/azure/token"
        val jwksUrl = "$localUrl/azure/jwks"

        return AzureConfigurationMetadata(tokenUrl, localUrl, jwksUrl)
    }

    fun dockerConfigurationMetadata(): AzureConfigurationMetadata {
        if (internalDockerUrl == null) {
            throw RuntimeException("Url in docker environment was not set")
        }

        val tokenUrl = "$internalDockerUrl/azure/token"
        val jwksUrl = "$internalDockerUrl/azure/jwks"

        return AzureConfigurationMetadata(tokenUrl, internalDockerUrl, jwksUrl)
    }

    fun createJwksMetadata(): JwksMetadata {

        val jwkAsJson = Json.parseToJsonElement(publicJwk)

        return JwksMetadata(listOf(jwkAsJson))
    }
}

@Serializable
data class AzureConfigurationMetadata(
        @SerialName("token_endpoint") val tokenEndpoint: String,
        @SerialName("issuer") val issuer: String,
        @SerialName("jwks_uri") val jwksUri: String
)

@Serializable
data class JwksMetadata(
    val keys: List<JsonElement>
)
