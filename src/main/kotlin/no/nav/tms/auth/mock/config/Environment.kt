package no.nav.tms.auth.mock.config

import no.nav.personbruker.dittnav.common.util.config.StringEnvVar.getEnvVar
import no.nav.personbruker.dittnav.common.util.config.StringEnvVar.getOptionalEnvVar

data class Environment(
    val localUrl: String = getEnvVar("LOCAL_URL"),
    val internalDockerUrl: String? = getOptionalEnvVar("INTERNAL_DOCKER_URL"),
    val tokendingsPrivateJwk: String = getEnvVar("TOKENDINGS_PRIVATE_JWK"),
    val azurePrivateJwk: String = getEnvVar("AZURE_PRIVATE_JWK"),
    val azureTenantId: String = "tms-auth-mock"
) {
    val isRunningInDocker get() = internalDockerUrl != null
}
