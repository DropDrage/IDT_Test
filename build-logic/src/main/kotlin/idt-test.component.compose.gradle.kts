import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("org.jetbrains.kotlin.plugin.compose")
}

plugins.withId("com.android.library") {
    configure<LibraryExtension> {
        buildFeatures {
            compose = true
        }
    }
}

// Application modules
plugins.withId("com.android.application") {
    configure<ApplicationExtension> {
        buildFeatures {
            compose = true
        }
    }
}

val libs = the<LibrariesForLibs>()

dependencies {
    "implementation"(platform(libs.androidx.compose.bom))
}
