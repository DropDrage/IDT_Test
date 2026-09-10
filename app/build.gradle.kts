plugins {
    alias(libs.plugins.android.application)
    id("idt-test.bundle.ui-screen")
    alias(libs.plugins.metro)
}

android {
    namespace = "com.dropdrage.idt_test"

    compileSdk {
        version = release(libs.versions.targetSdk.get().toInt())
    }
    defaultConfig {
        applicationId = "com.dropdrage.idt_test"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            optimization {
                enable = true
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)

    implementation(projects.common.presentation)

    implementation(projects.core.navigation)
    implementation(projects.core.presentation.theme)

    implementation(projects.feature.config)
    implementation(projects.feature.table.data)
    implementation(projects.feature.table.domain)
    implementation(projects.feature.table)
}
