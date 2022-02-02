package no.nav.tms.auth.mock.tokendings

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class TokendingsMetadataBuilder(private val localUrl: String, private val publicJwk: String) {
    fun createConfigurationMetadata(): TokendingsConfigurationMetadata {

        val url = "$localUrl/tokendings/token"

        return TokendingsConfigurationMetadata(url, localUrl)
    }

    fun createJwksMetadata(): JwksMetadata {
        return JwksMetadata(listOf(publicJwk))
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
    val keys: List<String>
)
