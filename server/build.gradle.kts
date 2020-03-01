import org.gradle.jvm.tasks.Jar

plugins {
    application
    kotlin("jvm")
}

buildscript {
    repositories {
        mavenCentral()
    }
}

group = "io.github.servb"
version = "1.0-SNAPSHOT"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenCentral()
    jcenter()
    maven { setUrl("https://kotlin.bintray.com/ktor") }
    maven { setUrl("https://kotlin.bintray.com/kotlin-js-wrappers") }
}

val kotlinVersion: String by project
val ktorVersion: String by project
val logbackVersion: String by project

dependencies {
    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}

fun inline(provider: Provider<Configuration>) = provider.get().map { if (it.isDirectory) it else zipTree(it) }

val fatJar = task("fatJar", type = Jar::class) {
    baseName = "${project.name}-fat"
    manifest {
        attributes(
            "Main-Class" to application.mainClassName
        )
    }
    from(inline(configurations.runtimeClasspath))
    with(tasks.jar.get() as CopySpec)
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}
