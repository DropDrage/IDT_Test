package com.dropdrage.idt_test.feature.table.data.di

import com.dropdrage.idt_test.feature.table.domain.repository.TableRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph

@DependencyGraph(AppScope::class)
interface TableDataGraph {

    fun tableRepository(): TableRepository

}
