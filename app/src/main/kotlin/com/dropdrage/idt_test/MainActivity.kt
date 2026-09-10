package com.dropdrage.idt_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.dropdrage.idt_test.core.navigation.NavigationEntry
import com.dropdrage.idt_test.core.presentation.theme.IdtTestTheme
import com.dropdrage.idt_test.di.AppGraph
import com.dropdrage.idt_test.feature.config.presentation.navigation.ConfigNavigationEntry
import com.dropdrage.idt_test.feature.config.presentation.ui.screen.ConfigScreen
import com.dropdrage.idt_test.feature.table.data.di.TableDataGraph
import com.dropdrage.idt_test.feature.table.presentation.navigation.TableNavigationEntry
import com.dropdrage.idt_test.feature.table.presentation.ui.screen.TableScreen
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory

internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val tableGraph = createGraph<TableDataGraph>()
        val graph = createGraphFactory<AppGraph.Factory>().create(tableGraph)
        setContent {
            IdtTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val backStack = rememberSaveable { mutableStateListOf<NavigationEntry>(ConfigNavigationEntry) }

                    CompositionLocalProvider(LocalMetroViewModelFactory provides graph.metroViewModelFactory) {
                        NavDisplay(
                            modifier = Modifier.padding(innerPadding),
                            backStack = backStack,
                            onBack = { backStack.removeLastOrNull() },
                            entryProvider = entryProvider {
                                entry<ConfigNavigationEntry> {
                                    ConfigScreen { rows, columns ->
                                        backStack.add(TableNavigationEntry(rows, columns))
                                    }
                                }
                                entry<TableNavigationEntry> {
                                    TableScreen(rows = it.rows, columns = it.columns) {
                                        backStack.removeLastOrNull()
                                    }
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}
