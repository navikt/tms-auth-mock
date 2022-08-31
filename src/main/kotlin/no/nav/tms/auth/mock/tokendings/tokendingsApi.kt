package no.nav.tms.auth.mock.tokendings

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import no.nav.tms.auth.mock.tokendings.ClientAssertion.createSignedAssertion

fun Route.tokendingsApi(
    tokendingsMetadataBuilder: TokendingsMetadataBuilder,
    tokenExchangeService: TokendingsExchangeService,
    runningInDockerEnv: Boolean
) {
    get("/tokendings/.well-known/oauth-authorization-server") {
        val metadata = tokendingsMetadataBuilder.localConfigurationMetadata()

        call.respond(metadata)
    }

    get("/tokendings/docker/.well-known/oauth-authorization-server") {
        if (!runningInDockerEnv) {
            call.respond(HttpStatusCode.NotFound, "docker-internal network address not specified")
        }

        val metadata = tokendingsMetadataBuilder.dockerConfigurationMetadata()

        call.respond(metadata)
    }

    get("/tokendings/jwks") {
        val metadata = tokendingsMetadataBuilder.createJwksMetadata()

        call.respond(metadata)
    }

    get("/tokendings/clientassertion") {
        val params = call.request.queryParameters
        val clientId = params["clientId"]
        val audience = params["audience"]
        if(clientId != null && audience != null) {
            call.respond(createSignedAssertion(clientId, audience))
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
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
    tokenExchangeService: TokendingsExchangeService,
    params: TokendingsParams
) {
    try {
        val token = tokenExchangeService.getExchangedToken(params.subjectToken, params.audience, params.clientAssertion)

        call.respond(TokendingsTokenResponse(token))
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

