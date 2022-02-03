package no.nav.tms.auth.mock.config

import no.nav.personbruker.dittnav.common.util.config.StringEnvVar.getEnvVar

data class Environment(
    val localUrl: String = getEnvVar("LOCAL_URL"),
    val tokendingsPrivateJwk: String = getEnvVar("TOKENDINGS_PRIVATE_JWK"),
    val azurePrivateJwk: String = getEnvVar("AZURE_PRIVATE_JWK"),
    val azureTenantId: String = "tms-auth-mock"
)
