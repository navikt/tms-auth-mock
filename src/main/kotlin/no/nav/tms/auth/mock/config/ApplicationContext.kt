package no.nav.tms.auth.mock.config

import no.nav.tms.auth.mock.azure.AzureJwtBuilder
import no.nav.tms.auth.mock.azure.AzureMetadataBuilder
import no.nav.tms.auth.mock.azure.AzureTokenBuilderService
import no.nav.tms.auth.mock.common.JwkBuilder
import no.nav.tms.auth.mock.tokendings.TokendingsExchangeService
import no.nav.tms.auth.mock.tokendings.TokendingsJwtBuilder
import no.nav.tms.auth.mock.tokendings.TokendingsMetadataBuilder

class ApplicationContext {

    val environment = Environment()

    val tokendingsPrivateJwk = environment.tokendingsPrivateJwk
    val tokendingsPublicJwk = JwkBuilder.getPublicJwk(environment.tokendingsPrivateJwk)

    private val tokenXJwtBuilder = TokendingsJwtBuilder(environment.tokendingsPrivateJwk, environment.localUrl)
    val tokendingsExchangeService = TokendingsExchangeService(tokenXJwtBuilder)
    val tokendingsMetadataBuilder = TokendingsMetadataBuilder(environment.localUrl, environment.internalDockerUrl, tokendingsPublicJwk)

    val azurePrivateJwk = environment.azurePrivateJwk
    val azurePublicJwk = JwkBuilder.getPublicJwk(azurePrivateJwk)

    private val azureJwtBuilder = AzureJwtBuilder(environment.azurePrivateJwk, environment.localUrl, environment.azureTenantId)
    val azureTokenBuilderService = AzureTokenBuilderService(azureJwtBuilder)
    val azureMetadataBuilder = AzureMetadataBuilder(environment.localUrl, environment.internalDockerUrl ,azurePublicJwk)
}
