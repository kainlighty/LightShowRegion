import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    kotlin("jvm") version "2.3.10"

    id("com.gradleup.shadow") version "9.4.1"
}

group = "ru.kainlight.lightshowregion"
version = "1.4.3"
description = "Displaying the region in the ActionBar"

val kotlinVersion = "2.3.10"
val papiVersion = "2.11.6"
val worldGuardVersion = "7.0.15"

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/releases/")
    maven("https://jitpack.io/")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":API"))

    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")

    compileOnly("com.sk89q.worldguard:worldguard-bukkit:${worldGuardVersion}")
//    compileOnly("me.clip:placeholderapi:$papiVersion")
    compileOnly(files("C:/Users/kainlight/IdeaProjects/LightLibrary/bukkit/libs/placeholderapi-2.12.2.jar"))
    //compileOnly("net.kyori:adventure-text-minimessage:$adventureVersion")


    implementation(files(
        "C:/Users/kainlight/IdeaProjects/LightLibrary/bukkit/build/libs/LightLibraryBukkit-1.0.jar"
    ))
}

val javaLanguageVersion = JavaLanguageVersion.of(21)
java {
    toolchain.languageVersion.set(javaLanguageVersion)
}
kotlin {
    jvmToolchain { javaLanguageVersion }
}

tasks {
    processResources {
        val libraries = listOf(
            "org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion}",
        )
        val props = mapOf(
            "pluginVersion" to version,
            "kotlinVersion" to kotlinVersion,
            "description" to project.description,
            "libraries" to libraries,
        )
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
    
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set(project.name)
        archiveFileName.set("${project.name}-${project.version}.jar")

        // Исключения
        exclude("META-INF/maven/**",
                "META-INF/INFO_BIN",
                "META-INF/INFO_SRC",
                "kotlin/**"
        )
        mergeServiceFiles()

        // Переименование пакетов
        val shadedPath = "ru.kainlight.${project.name.lowercase()}.shaded"
        relocate("ru.kainlight.lightlibrary", "$shadedPath.lightlibrary")
    }
}