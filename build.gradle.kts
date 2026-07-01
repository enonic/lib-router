plugins {
    id("java")
    id("jacoco")
    id("maven-publish")

    alias(libs.plugins.enonic.defaults)
    id("com.enonic.xp.base")
}

repositories {
    mavenCentral()
    xp.enonicRepo("dev")
}

dependencies {
    compileOnly(xplibs.api.script)

    testImplementation("com.enonic.xp:testing:${property("xpVersion")}")
    testImplementation(platform(libs.junit.bom))
    testImplementation(platform(libs.mockito.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockito.jupiter)
    testRuntimeOnly(libs.junit.launcher)
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
        html.required = true
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestReport)
}

artifacts {
    add("archives", tasks.jar)
}

// Assemble the @enonic-types/lib-router npm package (published on release by the CI npmPublish step).
val assembleTypes = tasks.register<Copy>("assembleTypes") {
    from("types")
    into(layout.buildDirectory.dir("types"))
    filesMatching("package.json") {
        filter { line ->
            line.replace(Regex("\"version\":\\s*\"[^\"]*\""), "\"version\": \"${project.version}\"")
        }
    }
}

tasks.jar {
    dependsOn(assembleTypes)
}
