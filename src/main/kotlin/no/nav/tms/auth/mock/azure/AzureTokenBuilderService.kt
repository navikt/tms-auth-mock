package no.nav.tms.auth.mock.azure


class AzureTokenBuilderService(private val jwtBuilder: AzureJwtBuilder) {

    private val scopeFormat = "^api://(.+)/\\.default$".toRegex()

    fun createToken(scope: String, clientId: String): String {

        val targetApp = extractTargetApp(scope)

        return jwtBuilder.buildToken(clientId, targetApp)
    }

    private fun extractTargetApp(scopeString: String): String {
        return scopeFormat.find(scopeString)
            ?.destructured
            ?.let { (targetApp) -> targetApp }
            ?: throw RuntimeException("Invalid scope")
    }
}
