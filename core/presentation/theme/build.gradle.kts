plugins {
    id("idt-test.library.android")
    id("idt-test.component.compose")
}

android {
    namespace = "com.dropdrage.idt_test.core.presentation.theme"
}

dependencies {
    implementation(libs.androidx.compose.material3)
}
