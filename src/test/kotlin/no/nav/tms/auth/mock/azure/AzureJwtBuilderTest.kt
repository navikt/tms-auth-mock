package no.nav.tms.auth.mock.azure

import com.nimbusds.jwt.JWTParser
import no.nav.tms.auth.mock.common.JwkBuilder
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.Test

internal class AzureJwtBuilderTest {

    private val clientApp = "cluster:namespace:clientApp"
    private val targetApp = "cluster:namespace:otherApp"

    private val tenantId = "local-auth-mock"
    private val tokendingsUrl = "http://tokendings"
    private val jwk = JwkBuilder.generateJwkString()

    private val jwtBuilder = AzureJwtBuilder(jwk, tokendingsUrl, tenantId)

    @Test
    fun `should imitate token exchange done by azure app`() {
        val exchangedToken = jwtBuilder.buildToken(clientApp, targetApp)

        val tokenClaims = JWTParser.parse(exchangedToken).jwtClaimsSet

        tokenClaims.audience `should contain` targetApp
        tokenClaims.issuer `should be equal to` tokendingsUrl
        tokenClaims.getClaim("azp") `should be equal to` clientApp
    }
}
