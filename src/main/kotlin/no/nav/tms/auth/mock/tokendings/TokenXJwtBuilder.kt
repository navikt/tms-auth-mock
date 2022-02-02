package no.nav.tms.auth.mock.tokendings

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

class TokenXJwtBuilder(privateJwk: String, private val localUrl: String) {

    private val privateRsaKey = RSAKey.parse(privateJwk)

    fun buildExchangedToken(originalToken: String, clientApp: String, targetApp: String): String {
        val originalClaims = extractClaims(originalToken)

        val (issueTime, expiry) = generateIatExp()
        val originalIssuer = determineOriginalIssuer(originalClaims)
        val uniqueId = generateUUID()

        return JWTClaimsSet.Builder(originalClaims)
            .issuer(localUrl)
            .claim("client_id", clientApp)
            .audience(targetApp)
            .notBeforeTime(issueTime)
            .issueTime(issueTime)
            .expirationTime(expiry)
            .claim("idp", originalIssuer)
            .jwtID(uniqueId)
            .build()
            .sign(privateRsaKey)
            .serialize()
    }

    private fun extractClaims(originalToken: String): JWTClaimsSet {
        val jwt = JWTParser.parse(originalToken)

        return jwt.jwtClaimsSet
    }

    private fun generateIatExp(): Pair<Date, Date> {
        val now = Instant.now()
        val hourFromNow = now.plus(1, ChronoUnit.HOURS)

        return Date.from(now) to Date.from(hourFromNow)
    }

    private fun determineOriginalIssuer(originalClaims: JWTClaimsSet): String {
        val currentIdp = originalClaims.getClaim("idp")

        return if (currentIdp != null) {
            currentIdp.toString()
        } else {
            originalClaims.issuer
        }
    }

    private fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }

    private fun JWTClaimsSet.sign(rsaKey: RSAKey): SignedJWT =
        SignedJWT(
            JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(rsaKey.keyID)
                .type(JOSEObjectType.JWT).build(),
            this
        ).apply {
            sign(RSASSASigner(rsaKey.toPrivateKey()))
        }
}
