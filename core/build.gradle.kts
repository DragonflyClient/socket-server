plugins {
    kotlin("jvm")
    application
}

group = "net.inceptioncloud"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":packets-library"))
    implementation(fileTree("libs"))
    implementation("org.litote.kmongo:kmongo-coroutine:4.0.3")
    implementation("org.apache.logging.log4j:log4j-api:2.0-beta9")
    implementation("org.apache.logging.log4j:log4j-core:2.0-beta9")
    implementation("com.esotericsoftware:kryonet:2.22.0-RC1")
    implementation("khttp:khttp:1.0.0")
    implementation("com.google.code.gson:gson:2.7")
    implementation("org.json:json:20200518")
}

kotlin {
    sourceSets {
        val main by getting
    }
}

tasks {
    register<Jar>("fullJar") {
        archiveClassifier.set("full")

        manifest {
            attributes["Main-Class"] = "net.dragonfly.kernel.server.DragonflySocketServer"
        }

        from(configurations.runtimeClasspath.get()
            .onEach { println(it.name) }
            .map { if (it.isDirectory) it else zipTree(it) }
        )
        with(jar.get() as CopySpec)
    }

    build.configure {
        dependsOn("fullJar")
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

application {
    mainClassName = "net.dragonfly.kernel.server.DragonflySocketServer"
}
