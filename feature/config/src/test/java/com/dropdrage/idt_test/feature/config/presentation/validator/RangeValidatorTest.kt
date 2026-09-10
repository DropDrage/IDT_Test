package com.dropdrage.idt_test.feature.config.presentation.validator

import com.dropdrage.idt_test.feature.config.presentation.ui.screen.validator.RangeValidator
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

internal class RangeValidatorTest : ShouldSpec(
    {
        context("RangeValidator.isValid") {
            val validator = RangeValidator(1..1000)

            withData(listOf(1, 2, 50, 1000)) { value ->
                should("return true") {
                    validator.isValid(value) shouldBe true
                }
            }
            withData(listOf(Int.MIN_VALUE, -10, 0, 1001, Int.MAX_VALUE)) { value ->
                should("return false") {
                    validator.isValid(value) shouldBe false
                }
            }
        }
    },
)
