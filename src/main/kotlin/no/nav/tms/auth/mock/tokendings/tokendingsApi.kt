package no.nav.tms.auth.mock.tokendings

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*

fun Route.tokenApi(
    tokendingsMetadataBuilder: TokendingsMetadataBuilder,
    tokenExchangeService: TokenExchangeService
) {
    get("/tokendings/.well-known/oauth-authorization-server") {
        val metadata = tokendingsMetadataBuilder.createConfigurationMetadata()

        call.respond(metadata)
    }

    get("/tokendings/jwks") {
        val metadata = tokendingsMetadataBuilder.createConfigurationMetadata()

        call.respond(metadata)
    }

    post("/tokendings/token") {
        val params = call.receiveTokendingsParams()

        if (params != null) {
            exchangeTokenAndRespond(tokenExchangeService, params)
        } else {
            call.respondText("", status = HttpStatusCode.BadRequest)
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.exchangeTokenAndRespond(
    tokenExchangeService: TokenExchangeService,
    params: TokendingsParams
) {
    try {
        val token = tokenExchangeService.getExchangedToken(params.subjectToken, params.audience, params.clientAssertion)

        call.respond(TokenResponse(token))
    } catch (e: Exception) {
        call.respond(HttpStatusCode.InternalServerError)
    }
}

private suspend fun ApplicationCall.receiveTokendingsParams(): TokendingsParams? {
    val params = receiveParameters()

    val clientAssertion = params["client_assertion"]
    val subjectToken = params["subject_token"]
    val audience = params["audience"]

    return if (clientAssertion == null || subjectToken == null || audience == null) {
        null
    } else {
        TokendingsParams(clientAssertion, subjectToken, audience)
    }
}
