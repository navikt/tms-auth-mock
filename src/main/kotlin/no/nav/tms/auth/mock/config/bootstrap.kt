package no.nav.tms.auth.mock.config

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.util.*
import no.nav.tms.auth.mock.azure.azureApi
import no.nav.tms.auth.mock.common.jwkApi
import no.nav.tms.auth.mock.tokendings.tokenApi

@KtorExperimentalAPI
fun Application.mainModule(appContext: ApplicationContext = ApplicationContext()) {

    install(DefaultHeaders)

    install(ContentNegotiation) {
        json(jsonConfig())
    }

    routing {
        jwkApi(appContext.tokendingsPrivateJwk, appContext.tokendingsPublicJwk)
        tokenApi(appContext.tokendingsMetadataBuilder, appContext.tokendingsExchangeService)
        azureApi(appContext.azureMetadataBuilder, appContext.azureTokenBuilderService)
    }
}
