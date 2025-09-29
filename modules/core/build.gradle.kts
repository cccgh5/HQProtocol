plugins {
    id("hq.shared")
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(framework.core)
    compileOnly(framework.command)

    compileOnly(project(":modules:api"))
}