package com.dropdrage.idt_test.di

import com.dropdrage.idt_test.feature.config.presentation.ui.screen.validator.RangeValidator
import com.dropdrage.idt_test.feature.table.data.di.TableDataGraph
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ViewModelGraph

@DependencyGraph(AppScope::class)
interface AppGraph : ViewModelGraph {

    @Named("rowsValidator")
    @Provides
    val rowsValidator: RangeValidator<Int>
        get() = RangeValidator(1..1000)
    @Named("columnsValidator")
    @Provides
    val columnsValidator: RangeValidator<Int>
        get() = RangeValidator(1..6)

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Includes messageGraph: TableDataGraph): AppGraph
    }
}
