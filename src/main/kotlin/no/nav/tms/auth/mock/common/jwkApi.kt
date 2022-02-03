package no.nav.tms.auth.mock.common

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.jwkApi() {
    get("/jwk/generate") {
        call.respondText(JwkBuilder.generateJwkString(), contentType = ContentType.Application.Json)
    }
}
