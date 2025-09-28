rootProject.name = getProperty("projectName")

pluginManagement {
    val paperWeightVersion: String by settings

    plugins {
        id("kr.hqservice.resource-generator.bukkit") version "1.0.0"
        id("io.papermc.paperweight.userdev") version paperWeightVersion apply false
    }
    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://maven.hqservice.kr/repository/maven-public")
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://maven.hqservice.kr/repository/maven-public")
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    versionCatalogs {
        create("libs") {
            library("paper-api", "io.papermc.paper:paper-api:${getProperty("paperVersion")}")
        }

        create("framework") {
            library("core", "kr.hqservice:hqframework-bukkit-core:${getProperty("hqFrameworkVersion")}")
            library("command", "kr.hqservice:hqframework-bukkit-command:${getProperty("hqFrameworkVersion")}")
            library("nms", "kr.hqservice:hqframework-bukkit-nms:${getProperty("hqFrameworkVersion")}")
            library("inventory", "kr.hqservice:hqframework-bukkit-inventory:${getProperty("hqFrameworkVersion")}")
            library("database", "kr.hqservice:hqframework-bukkit-database:${getProperty("hqFrameworkVersion")}")
            library("scheduler", "kr.hqservice:hqframework-bukkit-scheduler:${getProperty("hqFrameworkVersion")}")
        }
    }
}

file(rootProject.projectDir.path + "/credentials.gradle.kts").let {
    if (it.exists()) {
        apply(it.path)
    }
}

includeBuild("build-logic")
includeAll("modules")

fun includeAll(modulesDir: String) {
    file("${rootProject.projectDir.path}/${modulesDir.replace(":", "/")}/").listFiles()?.forEach { modulePath ->
        include("${modulesDir.replace("/", ":")}:${modulePath.name}")
    }
}

fun getProperty(key: String): String {
    return extra[key]?.toString() ?: throw IllegalArgumentException("property with $key not found")
}
