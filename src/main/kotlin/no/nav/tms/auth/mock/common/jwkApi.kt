package no.nav.tms.auth.mock.common

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.jwkApi(privateJwk: String, publicJwk: String) {
    get("/jwk/generate") {
        call.respondText(JwkBuilder.generateJwkString(), contentType = ContentType.Application.Json)
    }

    get("/jwk/private") {
        call.respondText(privateJwk, contentType = ContentType.Application.Json)
    }

    get("/jwk/public") {
        call.respondText(publicJwk, contentType = ContentType.Application.Json)
    }
}
