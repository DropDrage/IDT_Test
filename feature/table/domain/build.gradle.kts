plugins {
    id("idt-test.library.kotlin")
    id("idt-test.component.unit-tests")
    alias(libs.plugins.metro)
}

kotlin {
    target {
        compilations.getByName("test").associateWith( // Fixes test compilation due to Metro test compilation break
            compilations.getByName("main"),
        )
    }
}
