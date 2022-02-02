package no.nav.tms.auth.mock.tokendings

data class TokendingsParams(
    val clientAssertion: String,
    val subjectToken: String,
    val audience: String
)
