package com.dropdrage.idt_test.feature.table.domain.model

import kotlin.math.absoluteValue

@JvmInline
value class CellId private constructor(private val packedValue: Long) {

    val row: Int
        get() = (packedValue shr Int.SIZE_BITS).toInt()
    val column: Int
        get() = packedValue.toInt()


    constructor(row: Int, column: Int) : this(row.toLong() shl 32 or column.absoluteValue.toLong())


    operator fun component1(): Int = row
    operator fun component2(): Int = column
}
