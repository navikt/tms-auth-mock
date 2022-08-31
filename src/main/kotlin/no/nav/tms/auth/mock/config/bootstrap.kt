package no.nav.tms.auth.mock.config

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*
import no.nav.tms.auth.mock.azure.azureApi
import no.nav.tms.auth.mock.common.jwkApi
import no.nav.tms.auth.mock.health.healthApi
import no.nav.tms.auth.mock.tokendings.tokendingsApi

fun Application.mainModule(appContext: ApplicationContext = ApplicationContext()) {

    install(DefaultHeaders)

    install(ContentNegotiation) {
        json(jsonConfig())
    }

    routing {
        jwkApi()
        healthApi()
        tokendingsApi(
            appContext.tokendingsMetadataBuilder,
            appContext.tokendingsExchangeService,
            appContext.environment.isRunningInDocker
        )
        azureApi(appContext.azureMetadataBuilder,
            appContext.azureTokenBuilderService,
            appContext.environment.isRunningInDocker
        )
    }
}
