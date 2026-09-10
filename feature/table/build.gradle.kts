plugins {
    id("idt-test.library.android")
    id("idt-test.bundle.ui-screen")
    id("idt-test.component.unit-tests")
}

android {
    namespace = "com.dropdrage.idt_test.feature.table"

    buildFeatures {
        resValues = true
    }
}

dependencies {
    implementation(projects.common.presentation)

    implementation(projects.core.navigation)
    implementation(projects.core.presentation.theme)

    implementation(projects.feature.table.domain)

    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.turbine)
}
