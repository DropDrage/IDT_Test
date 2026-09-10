plugins {
    id("idt-test.library.android")
    id("idt-test.bundle.ui-screen")
    id("idt-test.component.unit-tests")
    alias(libs.plugins.metro)
}

android {
    namespace = "com.dropdrage.idt_test.feature.config"

    buildFeatures {
        resValues = true
    }
}

dependencies {
    implementation(projects.common.presentation)

    implementation(projects.core.navigation)
    implementation(projects.core.presentation.theme)

    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.turbine)
}
