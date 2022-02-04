# tms-auth

Mock ulike auth-tjenester for å tilrettelegge lokal kjøring av autentiserte apper.

# Kom i gang
1. Bygg tms-auth-mock ved å kjøre `gradle build`
2. Start appen lokalt ved å kjøre `gradle runServer`
3. Appen nås på `http://localhost:9051`

For å kjøre appen trengs 3 miljøvariabler:

- LOCAL_URL: Brukes som navn på issuer, og i metadata som f. eks. jwks uri.
- TOKENDINGS_PRIVATE_JWK: Jwk som brukes til å signere utstede tokenx-tokens.
- AZURE_PRIVATE_JWK: Jwk som brukes til å signere utstede azure tokens.

Se i bootstrapJwk.json for en jwk som kan brukes i de to jwk-variablene ved første oppstart.
Appen tilbyr et /jwk/generate endepunkt som kan brukes til å lage nye jwker. Det er ikke noe teknisk i veien
for at azure og tokendings deler jwk, men de bør helst være ulike.

Andre apper setter sine azure- eller tokendings miljøvariabler slik at de peker på denne appen og riktig endepunkt.
For eksempel setter man TOKEN_X_WELL_KNOWN_URL til http://localhost:9051/tokendings/.well-known/oauth-authorization-server

Client ids må også matche det som settes i utstedt token. For eksempel hvis appen `backend-one` har 
TOKEN_X_CLIENT_ID=local:backend-one, må appen `requesting-app` bruke "local:backend-one" som navn på target app. Det
samme gjelder AZURE_APP_CLIENT_ID. 
