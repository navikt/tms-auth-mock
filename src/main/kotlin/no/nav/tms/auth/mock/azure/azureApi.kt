package no.nav.tms.auth.mock.azure

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*

fun Route.azureApi(
    azureMetadataBuilder: AzureMetadataBuilder,
    tokenExchangeService: AzureTokenBuilderService
) {
    get("/azure/.well-known/oauth-authorization-server") {
        val metadata = azureMetadataBuilder.createConfigurationMetadata()

        call.respond(metadata)
    }

    get("/azure/jwks") {
        val metadata = azureMetadataBuilder.createJwksMetadata()

        call.respond(metadata)
    }

    post("/azure/token") {
        val params = call.receiveazureParams()

        if (params != null) {
            exchangeTokenAndRespond(tokenExchangeService, params)
        } else {
            call.respondText("", status = HttpStatusCode.BadRequest)
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.exchangeTokenAndRespond(
    tokenExchangeService: AzureTokenBuilderService,
    params: AzureParams
) {
    try {
        val token = tokenExchangeService.createToken(params.scope, params.clientId)

        call.respond(AzureTokenResponse(token))
    } catch (e: Exception) {
        call.respond(HttpStatusCode.InternalServerError)
    }
}

private suspend fun ApplicationCall.receiveazureParams(): AzureParams? {
    val params = receiveParameters()

    val scope = params["scope"]
    val clientId = params["client_id"]

    return if (scope == null || clientId == null) {
        null
    } else {
        AzureParams(scope, clientId)
    }
}
