plugins {
    id("idt-test.library.kotlin")
    alias(libs.plugins.metro)
}

dependencies {
    implementation(libs.kotlin.coroutines)

    implementation(projects.feature.table.domain)
}
