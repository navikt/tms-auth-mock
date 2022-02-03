package no.nav.tms.auth.mock.azure

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class AzureTokenBuilderServiceTest {

    private val jwtBuilder: AzureJwtBuilder = mockk()

    private val tokenBuilderService = AzureTokenBuilderService(jwtBuilder)

    @Test
    fun `should extract target app from scope and pass to jwt builder`() {
        val clientId = "dummyClient"
        val targetApp = "dummyTarget"

        val scope = "api://$targetApp/.default"

        val expectedToken = "exchangedToken"

        val capturedTargetApp = slot<String>()

        every { jwtBuilder.buildToken(clientId, capture(capturedTargetApp)) } returns expectedToken

        val createdToken = tokenBuilderService.createToken(scope, clientId)

        createdToken `should be equal to` expectedToken
        capturedTargetApp.captured `should be equal to` targetApp
    }
}
