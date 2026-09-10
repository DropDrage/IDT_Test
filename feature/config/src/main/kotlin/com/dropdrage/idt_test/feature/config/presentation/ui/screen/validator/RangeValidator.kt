package com.dropdrage.idt_test.feature.config.presentation.ui.screen.validator

class RangeValidator<T : Comparable<T>>(private val range: ClosedRange<T>) {
    fun isValid(value: T?): Boolean = value != null && value in range
}
