package no.nav.tms.auth.mock.config

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.util.*
import no.nav.tms.auth.mock.common.jwkApi
import no.nav.tms.auth.mock.health.healthApi
import no.nav.tms.auth.mock.tokendings.tokenApi

@KtorExperimentalAPI
fun Application.mainModule(appContext: ApplicationContext = ApplicationContext()) {

    install(DefaultHeaders)

    install(ContentNegotiation) {
        json(jsonConfig())
    }

    routing {
        jwkApi(appContext.tokendingsPrivateJwk, appContext.tokendingsPublicJwk)
        tokenApi(appContext.tokendingsMetadataBuilder, appContext.tokenExchangeService)
    }
}
