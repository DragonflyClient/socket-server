plugins {
    kotlin("jvm")
    application
}

group = "net.inceptioncloud"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":packets-library"))
    implementation("org.litote.kmongo:kmongo-coroutine:4.0.3")
    implementation("org.apache.logging.log4j:log4j-api:2.0-beta9")
    implementation("org.apache.logging.log4j:log4j-core:2.0-beta9")
    implementation("com.esotericsoftware:kryonet:2.22.0-RC1")
}

kotlin {
    sourceSets {
        val main by getting
    }
}

application {
    mainClassName = "net.dragonfly.socket.server.DragonflySocketServer"
}
