package no.nav.tms.auth.mock.config

import no.nav.tms.auth.mock.common.JwkBuilder
import no.nav.tms.auth.mock.tokendings.TokenExchangeService
import no.nav.tms.auth.mock.tokendings.TokenXJwtBuilder
import no.nav.tms.auth.mock.tokendings.TokendingsMetadataBuilder

class ApplicationContext {

    val environment = Environment()

    val tokendingsPrivateJwk = environment.tokendingsPrivateJwk
    val tokendingsPublicJwk = JwkBuilder.getPublicJwk(environment.tokendingsPrivateJwk)

    private val tokenXJwtBuilder = TokenXJwtBuilder(environment.tokendingsPrivateJwk, environment.localUrl)
    val tokenExchangeService = TokenExchangeService(tokenXJwtBuilder)
    val tokendingsMetadataBuilder = TokendingsMetadataBuilder(environment.localUrl, tokendingsPublicJwk)
}
