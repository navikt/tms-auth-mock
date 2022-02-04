package no.nav.tms.auth.mock.tokendings

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import no.nav.tms.auth.mock.common.JwkBuilder
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.log

object JwtBuilder {

    private val rsaKey = RSAKey.parse(JwkBuilder.generateJwkString())

    fun buildAbbreviatedUserToken(ident: String, loginLevel: String, issuer: String, identClaim: String = "pid"): String {
        return JWTClaimsSet.Builder()
            .claim(identClaim, ident)
            .claim("acr", loginLevel)
            .issuer(issuer)
            .build()
            .sign(rsaKey)
            .serialize()
    }

    fun buildAbbreviatedUserTokenWithIdp(ident: String, loginLevel: String, issuer: String, idpClaim: String): String {
        return JWTClaimsSet.Builder()
            .claim("pid", ident)
            .claim("acr", loginLevel)
            .issuer(issuer)
            .claim("idp", idpClaim)
            .build()
            .sign(rsaKey)
            .serialize()
    }

    fun buildAbbreviatedClientAssertion(clientId: String): String {
        return JWTClaimsSet.Builder()
            .subject(clientId)
            .build()
            .sign(rsaKey)
            .serialize()
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
