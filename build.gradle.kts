
val kotlin_version: String by project
val logback_version: String by project
val prometheus_version: String by project

plugins {
    kotlin("jvm") version "1.9.24"
    id("io.ktor.plugin") version "3.0.0-rc-1"
    id("com.google.devtools.ksp") version "1.9.24-1.0.20"
    id("application")
}

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-webjars-jvm")
    implementation("org.webjars:jquery:3.2.1")
    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-call-id-jvm")
    implementation("io.ktor:ktor-server-metrics-micrometer-jvm")
    implementation("io.micrometer:micrometer-registry-prometheus:$prometheus_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-jackson-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")

    //koin implementation
    implementation("io.insert-koin:koin-core:4.0.0-RC2")
    implementation("io.insert-koin:koin-annotations:1.4.0-RC4")
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.24-1.0.20")
    ksp("io.insert-koin:koin-ksp-compiler:1.4.0-RC4")

    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

ksp {
    arg("KOIN_CONFIG_CHECK","true")
}