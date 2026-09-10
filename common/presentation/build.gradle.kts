plugins {
    id("idt-test.library.android")
    id("idt-test.component.compose")
}

android {
    namespace = "com.dropdrage.idt_test.common.ui"
}

dependencies {
    implementation(libs.androidx.compose.ui)
}
