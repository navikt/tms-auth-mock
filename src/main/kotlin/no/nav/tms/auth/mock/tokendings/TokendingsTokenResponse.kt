package no.nav.tms.auth.mock.tokendings

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TokendingsTokenResponse(
        @SerialName("access_token") val accessToken: String,
        @SerialName("issued_token_type") val issuedTokenType: String = "urn:ietf:params:oauth:token-type:access_token",
        @SerialName("token_type") val tokenType: String = "Bearer",
        @SerialName("expires_in") val expiresIn: Int = 3600
)
