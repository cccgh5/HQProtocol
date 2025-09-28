plugins {
    id("hq.shared")
    id("io.papermc.paperweight.userdev")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.hqservice.kr/repository/maven-public/")
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(framework.core)
    compileOnly(project(":modules:api"))
    compileOnly(project(":modules:core"))

    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
}