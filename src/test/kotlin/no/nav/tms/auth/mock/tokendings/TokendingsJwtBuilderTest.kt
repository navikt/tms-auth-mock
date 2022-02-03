package no.nav.tms.auth.mock.tokendings

import com.nimbusds.jwt.JWTParser
import no.nav.tms.auth.mock.common.JwkBuilder
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.Test

internal class TokendingsJwtBuilderTest {

    private val userIdent = "123456"
    private val originalIssuer = "http://id-porten"
    private val loginLevel = "Level3"

    private val clientApp = "cluster:namespace:clientApp"
    private val targetApp = "cluster:namespace:otherApp"

    private val tokendingsUrl = "http://tokendings"
    private val jwk = JwkBuilder.generateJwkString()

    private val jwtBuilder = TokendingsJwtBuilder(jwk, tokendingsUrl)

    @Test
    fun `should imitate token exchange done by tokendings app`() {
        val originalToken = JwtBuilder.buildAbbreviatedUserToken(userIdent, loginLevel, originalIssuer)

        val exchangedToken = jwtBuilder.buildExchangedToken(originalToken, clientApp, targetApp)

        val tokenClaims = JWTParser.parse(exchangedToken).jwtClaimsSet

        tokenClaims.getClaim("client_id") `should be equal to` clientApp
        tokenClaims.audience `should contain` targetApp
        tokenClaims.issuer `should be equal to` tokendingsUrl
        tokenClaims.getClaim("idp") `should be equal to` originalIssuer
        tokenClaims.getClaim("acr") `should be equal to` loginLevel
        tokenClaims.getClaim("pid") `should be equal to` userIdent
    }

    @Test
    fun `should keep name of ident claim`() {
        val originalToken = JwtBuilder.buildAbbreviatedUserToken(userIdent, loginLevel, originalIssuer, identClaim = "sub")

        val exchangedToken = jwtBuilder.buildExchangedToken(originalToken, clientApp, targetApp)

        val tokenClaims = JWTParser.parse(exchangedToken).jwtClaimsSet

        tokenClaims.getClaim("sub") `should be equal to` userIdent
    }

    @Test
    fun `should keep idp claim if set`() {
        val differentOriginalIssuer = "http://different-issuer"

        val originalToken = JwtBuilder.buildAbbreviatedUserTokenWithIdp(userIdent, loginLevel, originalIssuer, idpClaim = differentOriginalIssuer)

        val exchangedToken = jwtBuilder.buildExchangedToken(originalToken, clientApp, targetApp)

        val tokenClaims = JWTParser.parse(exchangedToken).jwtClaimsSet

        tokenClaims.getClaim("client_id") `should be equal to` clientApp
        tokenClaims.audience `should contain` targetApp
        tokenClaims.issuer `should be equal to` tokendingsUrl
        tokenClaims.getClaim("idp") `should be equal to` differentOriginalIssuer
        tokenClaims.getClaim("acr") `should be equal to` loginLevel
    }
}
