package no.nav.tms.auth.mock.tokendings

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class TokendingsExchangeServiceTest {

    private val jwtBuilder: TokendingsJwtBuilder = mockk()

    private val exchangeService = TokendingsExchangeService(jwtBuilder)

    @Test
    fun `should extract client name from client assertion and pass this on to jwt builder`() {
        val clientId = "dummyClient"
        val targetApp = "dummyTarget"
        val token = "dummyToken"

        val expectedToken = "exchangedToken"

        val clientAssertion = JwtBuilder.buildAbbreviatedClientAssertion(clientId)

        val capturedClient = slot<String>()

        every { jwtBuilder.buildExchangedToken(token, capture(capturedClient), targetApp) } returns expectedToken

        val exchangedToken = exchangeService.getExchangedToken(token, targetApp, clientAssertion)

        exchangedToken `should be equal to` expectedToken
        capturedClient.captured `should be equal to` clientId
    }
}
