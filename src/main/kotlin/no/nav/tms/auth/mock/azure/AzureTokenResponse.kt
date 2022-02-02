package no.nav.tms.auth.mock.azure

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AzureTokenResponse(
        @SerialName("access_token") val accessToken: String,
        @SerialName("token_type") val tokenType: String = "Bearer",
        @SerialName("expires_in") val expiresIn: Int = 3600,
        @SerialName("ext_expires_in") val extExpiresIn: Int = 3600
)
