package no.nav.tms.auth.mock.azure

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

class AzureJwtBuilder(privateJwk: String, private val localUrl: String, private val tenantId: String) {

    private val privateRsaKey = RSAKey.parse(privateJwk)

    fun buildToken(clientId: String, targetApp: String): String {

        val (issueTime, expiry) = generateIatExp()

        return JWTClaimsSet.Builder()
            .issuer(localUrl)
            .audience(targetApp)
            .claim("azp", clientId)
            .claim("tid", tenantId)
            .subject(generateUUID())
            .claim("azpacr", "2")
            .claim("aio", "N/A")
            .claim("oid", generateUUID())
            .claim("rh", "N/A")
            .claim("roles", listOf("access_as_application"))
            .claim("uti", generateUUID())
            .claim("ver", "2.0")
            .notBeforeTime(issueTime)
            .issueTime(issueTime)
            .expirationTime(expiry)
            .build()
            .sign(privateRsaKey)
            .serialize()
    }

    private fun generateIatExp(): Pair<Date, Date> {
        val now = Instant.now()
        val hourFromNow = now.plus(1, ChronoUnit.HOURS)

        return Date.from(now) to Date.from(hourFromNow)
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
