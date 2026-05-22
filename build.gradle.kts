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
