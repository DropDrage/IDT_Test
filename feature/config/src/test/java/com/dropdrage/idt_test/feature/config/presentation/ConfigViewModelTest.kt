package com.dropdrage.idt_test.feature.config.presentation

import app.cash.turbine.test
import com.dropdrage.idt_test.common.ui.utils.TextResource
import com.dropdrage.idt_test.feature.config.R
import com.dropdrage.idt_test.feature.config.presentation.ui.screen.ConfigViewModel
import com.dropdrage.idt_test.feature.config.presentation.ui.screen.model.Command
import com.dropdrage.idt_test.feature.config.presentation.ui.screen.model.News
import com.dropdrage.idt_test.feature.config.presentation.ui.screen.model.UiState
import com.dropdrage.idt_test.feature.config.presentation.ui.screen.validator.RangeValidator
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
internal class ConfigViewModelTest : BehaviorSpec(
    {

        val testDispatcher = UnconfinedTestDispatcher()

        val rowsValidationError = TextResource.id(R.string.config_row_validation_error)
        val columnsValidationError = TextResource.id(R.string.config_column_validation_error)

        lateinit var rowsValidator: RangeValidator<Int>
        lateinit var columnsValidator: RangeValidator<Int>
        lateinit var viewModel: ConfigViewModel

        beforeTest {
            Dispatchers.setMain(testDispatcher)
            rowsValidator = RangeValidator(1..10)
            columnsValidator = RangeValidator(1..10)
            viewModel = ConfigViewModel(rowsValidator, columnsValidator)
        }

        afterTest {
            Dispatchers.resetMain()
        }

        given("ConfigViewModel in initial state") {
            then("initial state has empty fields and no errors") {
                viewModel.uiState.value shouldBe UiState(
                    rows = "",
                    columns = "",
                    rowsError = null,
                    columnsError = null,
                )
            }

            `when`("Generate is dispatched") {
                then("initial state has empty fields and no errors") {
                    viewModel.news.test {
                        viewModel.command(Command.Generate)

                        expectNoEvents()
                        cancelAndIgnoreRemainingEvents()

                        val state = viewModel.uiState.value
                        state.rows shouldBe ""
                        state.rowsError shouldBe rowsValidationError
                        state.columns shouldBe ""
                        state.columnsError shouldBe columnsValidationError
                    }
                }
            }
        }

        given("ConfigViewModel with an empty form") {
            `when`("RowsChanged is dispatched with non-digit characters") {
                then("non-digits are filtered from rows") {
                    viewModel.command(Command.RowsChanged("1a2b3"))

                    val state = viewModel.uiState.value
                    state.rows shouldBe "123"
                    state.rowsError shouldBe rowsValidationError
                    state.columns shouldBe ""
                    state.columnsError shouldBe null
                }
            }

            `when`("RowsChanged is dispatched with a value inside the validator range") {
                then("rows is set and rowsError is null") {
                    viewModel.command(Command.RowsChanged("5"))

                    val state = viewModel.uiState.value
                    state.rows shouldBe "5"
                    state.rowsError shouldBe null
                    state.columns shouldBe ""
                    state.columnsError shouldBe null
                }
            }

            `when`("RowsChanged is dispatched with a value outside the validator range") {
                then("rows is set and rowsError is raised") {
                    viewModel.command(Command.RowsChanged("99"))

                    val state = viewModel.uiState.value
                    state.rows shouldBe "99"
                    state.rowsError shouldBe rowsValidationError
                    state.columns shouldBe ""
                    state.columnsError shouldBe null
                }
            }

            `when`("RowsChanged is dispatched with an input that has no digits") {
                then("rows is empty and rowsError is raised") {
                    viewModel.command(Command.RowsChanged("abc"))

                    val state = viewModel.uiState.value
                    state.rows shouldBe ""
                    state.rowsError shouldBe rowsValidationError
                    state.columns shouldBe ""
                    state.columnsError shouldBe null
                }
            }

            `when`("ColumnsChanged is dispatched with non-digit characters") {
                then("non-digits are filtered from columns") {
                    viewModel.command(Command.ColumnsChanged("4x5y6"))

                    val state = viewModel.uiState.value
                    state.rows shouldBe ""
                    state.rowsError shouldBe null
                    state.columns shouldBe "456"
                    state.columnsError shouldBe columnsValidationError
                }
            }

            `when`("ColumnsChanged is dispatched with a value inside the validator range") {
                then("columns is set and columnsError is cleared") {
                    viewModel.command(Command.ColumnsChanged("7"))

                    val state = viewModel.uiState.value
                    state.rows shouldBe ""
                    state.rowsError shouldBe null
                    state.columns shouldBe "7"
                    state.columnsError shouldBe null
                }
            }

            `when`("ColumnsChanged is dispatched with a value outside the validator range") {
                then("columns is set and columnsError is raised") {
                    viewModel.command(Command.ColumnsChanged("0"))

                    val state = viewModel.uiState.value
                    state.rows shouldBe ""
                    state.rowsError shouldBe null
                    state.columns shouldBe "0"
                    state.columnsError shouldBe columnsValidationError
                }
            }

            `when`("ColumnsChanged is dispatched with an input that has no digits") {
                then("columns is empty and columnsError is raised") {
                    viewModel.command(Command.ColumnsChanged("xyz"))

                    val state = viewModel.uiState.value
                    state.rows shouldBe ""
                    state.rowsError shouldBe null
                    state.columns shouldBe ""
                    state.columnsError shouldBe columnsValidationError
                }
            }
        }

        given("ConfigViewModel with both fields valid") {
            `when`("Generate is dispatched") {
                then("News.Generate is emitted") {
                    viewModel.command(Command.RowsChanged("3"))
                    viewModel.command(Command.ColumnsChanged("4"))

                    viewModel.news.test {
                        viewModel.command(Command.Generate)
                        awaitItem() shouldBe News.Generate
                    }
                }
            }
        }

        given("ConfigViewModel with an invalid rows value") {
            `when`("Generate is dispatched") {
                then("no News is emitted") {
                    viewModel.command(Command.RowsChanged("99"))
                    viewModel.command(Command.ColumnsChanged("4"))

                    viewModel.news.test {
                        viewModel.command(Command.Generate)
                        expectNoEvents()
                        cancelAndIgnoreRemainingEvents()
                    }
                }
            }
        }

        given("ConfigViewModel with an invalid columns value") {
            `when`("Generate is dispatched") {
                then("no News is emitted") {
                    viewModel.command(Command.RowsChanged("3"))
                    viewModel.command(Command.ColumnsChanged("99"))

                    viewModel.news.test {
                        viewModel.command(Command.Generate)
                        expectNoEvents()
                        cancelAndIgnoreRemainingEvents()
                    }
                }
            }
        }

        given("ConfigViewModel with both fields invalid") {
            `when`("Generate is dispatched") {
                then("no News is emitted") {
                    viewModel.command(Command.RowsChanged("99"))
                    viewModel.command(Command.ColumnsChanged("99"))

                    viewModel.news.test {
                        viewModel.command(Command.Generate)
                        expectNoEvents()
                        cancelAndIgnoreRemainingEvents()
                    }
                }
            }
        }

        given("ConfigViewModel with columns already set") {
            `when`("RowsChanged is dispatched") {
                then("columns and columnsError are untouched") {
                    viewModel.command(Command.ColumnsChanged("4"))
                    viewModel.command(Command.RowsChanged("99"))

                    val state = viewModel.uiState.value
                    state.rows shouldBe "99"
                    state.rowsError shouldBe rowsValidationError
                    state.columns shouldBe "4"
                    state.columnsError shouldBe null
                }
            }
        }

        given("ConfigViewModel with rows already set") {
            `when`("ColumnsChanged is dispatched") {
                then("rows and rowsError are untouched") {
                    viewModel.command(Command.RowsChanged("5"))
                    viewModel.command(Command.ColumnsChanged("99"))

                    val state = viewModel.uiState.value
                    state.rows shouldBe "5"
                    state.rowsError shouldBe null
                    state.columns shouldBe "99"
                    state.columnsError shouldBe columnsValidationError
                }
            }
        }
    },
)
