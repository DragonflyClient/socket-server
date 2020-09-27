plugins {
    kotlin("jvm")
}

group = "net.inceptioncloud"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.reflections:reflections:0.9.10")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")
    implementation("org.apache.logging.log4j:log4j-api:2.0-beta9")
    implementation("com.esotericsoftware:kryonet:2.22.0-RC1")
}

kotlin {
    sourceSets {
        val main by getting
    }
}
