import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("idt-test.component.compose")
}

plugins.withId("com.android.library") {
    pluginManager.apply("dev.zacsweers.metro")
}

val libs = the<LibrariesForLibs>()

dependencies {
    "api"(libs.bundles.metro.viewmodel)

    "implementation"(libs.androidx.compose.ui)
    "implementation"(libs.androidx.compose.ui.graphics)
    "implementation"(libs.androidx.compose.ui.tooling.preview)
    "implementation"(libs.androidx.compose.material3)
    "debugImplementation"(libs.androidx.compose.ui.test.manifest)
    "debugImplementation"(libs.androidx.compose.ui.tooling)
}
