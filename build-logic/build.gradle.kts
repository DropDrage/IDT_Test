plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.compose.gradle.plugin)
    implementation(libs.metro.gradle.plugin)
    implementation(libs.kotest.gradle.plugin)

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
