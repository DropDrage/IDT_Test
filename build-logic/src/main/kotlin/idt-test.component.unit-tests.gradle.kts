import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("io.kotest")
}

val libs = the<LibrariesForLibs>()

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {
    "testImplementation"(libs.bundles.test)
}
