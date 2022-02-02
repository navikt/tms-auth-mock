package no.nav.tms.auth.mock.common

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator

internal object JwkBuilder {
    private fun generateJwk(): RSAKey {
        return RSAKeyGenerator(2048)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID("KID")
                .generate()
    }

    fun generateJwkString(): String = generateJwk().toJSONString()

    fun getPublicJwk(privateKey: String): String {
        val rsaKey = RSAKey.parse(privateKey)

        return rsaKey.toPublicJWK().toJSONString()
    }
}
