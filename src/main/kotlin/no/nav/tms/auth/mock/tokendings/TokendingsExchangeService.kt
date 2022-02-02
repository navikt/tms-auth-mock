package no.nav.tms.auth.mock.tokendings

import com.nimbusds.jwt.JWTParser

class TokendingsExchangeService(private val jwtBuilder: TokenXJwtBuilder) {

    fun getExchangedToken(token: String, audience: String, clientAssertion: String): String {
        val client = extractClientName(clientAssertion)

        return jwtBuilder.buildExchangedToken(token, client, audience)
    }

    private fun extractClientName(clientAssertion: String): String {
        val jwt = JWTParser.parse(clientAssertion)

        val claimSet = jwt.jwtClaimsSet

        return claimSet.subject.toString()
    }
}
