import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    kotlin("jvm").version(Kotlin.version)
    kotlin("plugin.allopen").version(Kotlin.version)
    kotlin("plugin.serialization").version(Kotlin.version)

    id(Shadow.pluginId) version ("7.1.2")
    // Apply the application plugin to add support for building a CLI application.
    application
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "13"
}

repositories {
    maven("https://jitpack.io")
    mavenLocal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

dependencies {
    implementation(DittNAV.Common.logging)
    implementation(DittNAV.Common.utils)
    implementation(Jackson.dataTypeJsr310)
    implementation(Kotlinx.coroutines)
    implementation(Kotlinx.htmlJvm)
    implementation(Ktor.auth)
    implementation(Ktor.authJwt)
    implementation(Ktor.clientApache)
    implementation(Ktor.clientJackson)
    implementation(Ktor.clientJson)
    implementation(Ktor.clientLogging)
    implementation(Ktor.clientLoggingJvm)
    implementation(Ktor.clientSerializationJvm)
    implementation(Ktor.serverNetty)
    implementation(Nimbusds.joseJwt)
    implementation(Ktor.serialization)
    implementation(Logback.classic)
    implementation(Logstash.logbackEncoder)

    testImplementation(Junit.api)
    testImplementation(Ktor.clientMock)
    testImplementation(Ktor.clientMockJvm)
    testImplementation(Kluent.kluent)
    testImplementation(Mockk.mockk)
    testImplementation(Jjwt.api)

    testRuntimeOnly(Bouncycastle.bcprovJdk15on)
    testRuntimeOnly(Jjwt.impl)
    testRuntimeOnly(Jjwt.jackson)
    testRuntimeOnly(Junit.engine)
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    register("runServer", JavaExec::class) {

        environment("LOCAL_URL", "http://localhost:9051")
        environment("TOKENDINGS_PRIVATE_JWK", """{"p":"6g6NG4osmUJhwwoFdOJredxdhipAkjXD1iE-r8KcLh8av0MGOXHIhNnfwLnllZctVBZ4LZMXsRI8FMAwIlDlF1GvjLg39h6nzSHv7yCrsTSjLeCsIYd6Qr2TpuCfJWdqjc-qUk-GmwDFLQY3gq7gqgIFlfDgbgUfP5QntIppF-M","kty":"RSA","q":"neuXEL_rYF1ZHUwfah2ybyOpCE_tFqfrnNfsrVpY1EPVCHJs7tIP-wn2wv0cPol38dAuWCvvtxAoT6lDGwJ-4ZnA3NPOi25T-U0dCZDN6SLEN65DAxl-SGxjOfZdjVVor689ib1NV8KXEstVYZt-LJD5g0BVz9aMOp513bha7PE","d":"MN52b9UjSk2FhnyIjoh0-RvlN4kWKXjPi5EfY_n7ZwZdnZafRLJWTiWI-Z3VtpEnZ6ZF_tbdZsz0esh-d_sPYMaa2l2pCsQUipQh2_8UowJ7eg9-KfjEY3XxwGtuoMOctEk2Ccw_CGnvL8gd-BpUYYMp-CkYdauaDk_81qqglfewy6zxNJfbRJDiiKTsAcvYLRIhkMAKbFcZVlqdNWE3HNIxu1H3-UUNXkwM5SeDeo4s5ECRGnMcdVovkj3ejEK8ZIW3dDBsY4AlhFoBci5eWFiijfhCC3Y7zLJfhAxiFLdO8mfn66T5TylKLiGOR92wJe-IviNGeOM2EcFGmwoTQQ","e":"AQAB","use":"sig","kid":"KID","qi":"4MNHneKkuw9LXGaYM2X9tuhhQegbZIqmOBNOhyilKZ2-rbtpdiy0jeYdrwieSw4bAGexssdEBAj2MctK4B_XR1ZrD7djwwMfHYAag4UWGrTT6XPP2cTJkENP_m58OYlwzmMOJRnTKtOLFkjXJVggW8z4vxvKdgSoVd-qmLS0JQM","dp":"bSbaEYTPpa16b_2la8wHuS3Wg7ICqOzf5mVcmZTVRwEEdTuplHKGw8XjfjpzYd69qeBMrVxSiSZq1HaIPAijvcs_Zg2nM2U6dgSko00GJL7adpO3Um-eya8lQ4avUT84RugYkwg5SuICEiw7CPvLFB2bQ0CxOT20Q-8w0RZXSmc","alg":"RS256","dq":"UAE07UVtx8pDBXa1S5tz9J5KWm2znZDA6xrxSvnEmvUqjjb6PRurAPGRVEPK9VvLqnRfpkfMECBda85CswJWqZCthrK_px_j5x9MILZ9uyjj45mjVYSq16IhuxG3X4zvlmHfeVKwRk_NHbZpsSOHjqh74CTN1JfLPz9cU1G8ruE","n":"kGJSA1GhT-U5IKNobyxtPbUmST3ysDHGq1IXAIv18q3b_ezl7sytfPxphSUhfw1XSA6-wrpRoupRYiHCj9EuyO068GEq3iboawPQRLPEqGT9B1nn9zdyU6EOOPq_26Yi8dOp5L5jWgHLt2xAK31RCc3z5bjefn0lcAoZmvEahyFBG9ZxmUDZ8AKapa10GA0JfJn8uds_hm5DMA6rA5hXd2EMfKRh8AZHMZIXPpI2Yy-2D24CxNMu5ZopDO6TGWCzx_ycWVA2fObUIIVdGeUT5ffgPrYVyLKkueeCHfZgEkRQX5ILl-fsz0Ee38nsu1UTIu5HlTPNUC7YxmCjvMLAsw"}""")

        main = application.mainClassName
        classpath = sourceSets["main"].runtimeClasspath
    }
}

apply(plugin = Shadow.pluginId)
