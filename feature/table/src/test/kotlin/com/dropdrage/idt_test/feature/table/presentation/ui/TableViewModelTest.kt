package com.dropdrage.idt_test.feature.table.presentation.ui

import app.cash.turbine.test
import com.dropdrage.idt_test.common.ui.utils.TextResource
import com.dropdrage.idt_test.feature.table.R
import com.dropdrage.idt_test.feature.table.domain.model.Cell
import com.dropdrage.idt_test.feature.table.domain.model.CellId
import com.dropdrage.idt_test.feature.table.domain.usecase.GenerateTableUseCase
import com.dropdrage.idt_test.feature.table.presentation.ui.screen.model.CellUi
import com.dropdrage.idt_test.feature.table.presentation.ui.screen.model.Command
import com.dropdrage.idt_test.feature.table.presentation.ui.edit_cell_dialog.model.EditDialogUiState
import com.dropdrage.idt_test.feature.table.presentation.ui.screen.model.UiState
import com.dropdrage.idt_test.feature.table.presentation.ui.screen.TableViewModel
import com.dropdrage.idt_test.feature.table.presentation.ui.screen.mapper.toUi
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
internal class TableViewModelTest : BehaviorSpec(
    {

        val testDispatcher = UnconfinedTestDispatcher()

        lateinit var generateTableUseCase: GenerateTableUseCase
        lateinit var viewModel: TableViewModel

        beforeTest {
            Dispatchers.setMain(testDispatcher)
            generateTableUseCase = mockk()
            viewModel = TableViewModel(generateTableUseCase)
        }

        afterTest {
            Dispatchers.resetMain()
            clearAllMocks()
        }

        fun cell(row: Int, column: Int, text: String) = Cell(CellId(row, column), text)

        fun List<List<Cell>>.toUi(): List<List<CellUi>> = map { it.map(Cell::toUi) }


        given("TableViewModel") {
            then("initial state is Loading") {
                viewModel.uiState.value.shouldBeInstanceOf<UiState.Loading>()
            }
        }

        given("TableViewModel in Loading state") {
            `when`("LoadData is dispatched") {
                then("Data is emitted with the generated cells and a loaded title") {
                    val rows = 2
                    val columns = 2
                    val generated = listOf(
                        listOf(cell(0, 0, "A"), cell(0, 1, "B")),
                        listOf(cell(1, 0, "C"), cell(1, 1, "D")),
                    )
                    coEvery { generateTableUseCase(rows, columns) } returns generated

                    viewModel.uiState.test {
                        awaitItem() shouldBe UiState.Loading

                        viewModel.command(Command.LoadData(rows, columns))

                        val data = awaitItem().shouldBeInstanceOf<UiState.Data>()
                        data.title shouldBe TextResource.idWithArgs(
                            R.string.table_title_table_loaded,
                            generated.size,
                            generated.first().size,
                        )
                        data.cells shouldBe generated.toUi()
                        data.selectedCell shouldBe null
                        data.editDialogUiState shouldBe EditDialogUiState.Hidden
                    }
                }
            }

            `when`("any command other than LoadData is dispatched") {
                then("the state remains Loading until LoadData is dispatched") {
                    val generated = listOf(listOf(cell(0, 0, "A")))
                    coEvery { generateTableUseCase(1, 1) } returns generated

                    viewModel.uiState.test {
                        awaitItem() shouldBe UiState.Loading

                        viewModel.command(Command.ToggleCellColor(CellId(0, 0)))
                        viewModel.command(Command.EditCell(CellId(0, 0)))
                        viewModel.command(Command.CancelCellEdit)
                        viewModel.command(Command.ApplyCellEdit(CellId(0, 0), "ignored"))

                        expectNoEvents()

                        viewModel.command(Command.LoadData(1, 1))
                        awaitItem().shouldBeInstanceOf<UiState.Data>()

                        expectNoEvents()
                    }
                }
            }
        }

        given("TableViewModel already showing a table") {
            `when`("LoadData is dispatched with new dimensions") {
                then("state become Loading and then Data with the new cells") {
                    coEvery { generateTableUseCase(1, 1) } returns
                        listOf(listOf(cell(0, 0, "First")))
                    val updatedList = listOf(
                        listOf(cell(0, 0, "X"), cell(0, 1, "Y")),
                        listOf(cell(1, 0, "Z"), cell(1, 1, "W")),
                    )
                    coEvery { generateTableUseCase(2, 2) } returns updatedList

                    viewModel.uiState.test {
                        awaitItem() // Loading
                        viewModel.command(Command.LoadData(1, 1))
                        awaitItem() // Data

                        viewModel.command(Command.LoadData(2, 2))
                        awaitItem() shouldBe UiState.Loading

                        val data = awaitItem().shouldBeInstanceOf<UiState.Data>()
                        data.title shouldBe TextResource.idWithArgs(
                            R.string.table_title_table_loaded,
                            updatedList.size,
                            updatedList.first().size,
                        )
                        data.cells shouldBe updatedList.toUi()
                        data.selectedCell shouldBe null
                        data.editDialogUiState shouldBe EditDialogUiState.Hidden
                    }
                }
            }
        }

        given("TableViewModel showing a table with no selection") {
            `when`("ToggleCellColor is dispatched for a cell") {
                then("that cell becomes the selected cell") {
                    val generated = listOf(
                        listOf(cell(0, 0, "A"), cell(0, 1, "B")),
                        listOf(cell(1, 0, "C"), cell(1, 1, "D")),
                    )
                    coEvery { generateTableUseCase.invoke(2, 2) } returns generated

                    viewModel.uiState.test {
                        awaitItem() // Loading
                        viewModel.command(Command.LoadData(2, 2))
                        awaitItem() // Data

                        viewModel.command(Command.ToggleCellColor(CellId(1, 0)))

                        val data = awaitItem().shouldBeInstanceOf<UiState.Data>()
                        data.title shouldBe TextResource.idWithArgs(
                            R.string.table_title_table_loaded,
                            generated.size,
                            generated.first().size,
                        )
                        data.cells shouldBe generated.toUi()
                        data.selectedCell shouldBe CellId(1, 0)
                        data.editDialogUiState shouldBe EditDialogUiState.Hidden
                    }
                }
            }
        }

        given("TableViewModel showing a table with a cell already selected") {
            `when`("ToggleCellColor is dispatched for the same cell") {
                then("the selection is cleared") {
                    val generated = listOf(listOf(cell(0, 0, "A")))
                    coEvery { generateTableUseCase(1, 1) } returns generated

                    viewModel.uiState.test {
                        awaitItem() // Loading
                        viewModel.command(Command.LoadData(1, 1))
                        awaitItem() // Data
                        viewModel.command(Command.ToggleCellColor(CellId(0, 0)))
                        awaitItem().shouldBeInstanceOf<UiState.Data>()
                            .selectedCell shouldBe CellId(0, 0)

                        viewModel.command(Command.ToggleCellColor(CellId(0, 0)))

                        val data = awaitItem().shouldBeInstanceOf<UiState.Data>()
                        data.title shouldBe TextResource.idWithArgs(
                            R.string.table_title_table_loaded,
                            generated.size,
                            generated.first().size,
                        )
                        data.cells shouldBe generated.toUi()
                        data.selectedCell shouldBe null
                        data.editDialogUiState shouldBe EditDialogUiState.Hidden
                    }
                }
            }
        }

        given("TableViewModel showing a table") {
            `when`("EditCell is dispatched for a cell") {
                then("the edit dialog becomes Visible for that cell") {
                    val generated = listOf(listOf(cell(0, 0, "A")))
                    coEvery { generateTableUseCase(1, 1) } returns generated

                    viewModel.uiState.test {
                        awaitItem() // Loading
                        viewModel.command(Command.LoadData(1, 1))
                        awaitItem() // Data

                        viewModel.command(Command.EditCell(CellId(0, 0)))

                        val data = awaitItem().shouldBeInstanceOf<UiState.Data>()
                        data.title shouldBe TextResource.idWithArgs(
                            R.string.table_title_table_loaded,
                            generated.size,
                            generated.first().size,
                        )
                        data.cells shouldBe generated.toUi()
                        data.selectedCell shouldBe null
                        data.editDialogUiState shouldBe
                            EditDialogUiState.Visible(CellId(0, 0))
                    }
                }
            }
        }

        given("TableViewModel with the edit dialog open for a cell") {
            `when`("ApplyCellEdit is dispatched with new text") {
                then("the cell's text is updated and the dialog is hidden") {
                    val generated = listOf(
                        listOf(cell(0, 0, "A"), cell(0, 1, "B")),
                    )
                    coEvery { generateTableUseCase(1, 2) } returns generated
                    val editedCellId = CellId(0, 1)

                    viewModel.uiState.test {
                        awaitItem() // Loading
                        viewModel.command(Command.LoadData(1, 2))
                        awaitItem() // Data
                        viewModel.command(Command.EditCell(editedCellId))
                        val withDialogState =
                            awaitItem().shouldBeInstanceOf<UiState.Data>()
                        withDialogState.editDialogUiState
                            .shouldBeInstanceOf<EditDialogUiState.Visible>()
                            .cellId shouldBe editedCellId

                        viewModel.command(Command.ApplyCellEdit(editedCellId, "Updated"))

                        val data = awaitItem().shouldBeInstanceOf<UiState.Data>()
                        data.title shouldBe TextResource.idWithArgs(
                            R.string.table_title_table_loaded,
                            generated.size,
                            generated.first().size,
                        )
                        data.cells[0][0].text shouldBe "A"
                        data.cells[0][1].text shouldBe "Updated"
                        data.selectedCell shouldBe null
                        data.editDialogUiState shouldBe EditDialogUiState.Hidden
                    }
                }
            }

            `when`("CancelCellEdit is dispatched") {
                then("the dialog is hidden and the cell text is unchanged") {
                    val generated = listOf(listOf(cell(0, 0, "A")))
                    coEvery { generateTableUseCase(1, 1) } returns generated
                    val editedCellId = CellId(0, 0)

                    viewModel.uiState.test {
                        awaitItem() // Loading
                        viewModel.command(Command.LoadData(1, 1))
                        awaitItem() // Data

                        viewModel.command(Command.EditCell(CellId(0, 0)))
                        val withDialogState =
                            awaitItem().shouldBeInstanceOf<UiState.Data>()
                        withDialogState.editDialogUiState
                            .shouldBeInstanceOf<EditDialogUiState.Visible>()
                            .cellId shouldBe editedCellId

                        viewModel.command(Command.CancelCellEdit)

                        val data = awaitItem().shouldBeInstanceOf<UiState.Data>()
                        data.title shouldBe TextResource.idWithArgs(
                            R.string.table_title_table_loaded,
                            generated.size,
                            generated.first().size,
                        )
                        data.cells[0][0].text shouldBe "A"
                        data.selectedCell shouldBe null
                        data.editDialogUiState shouldBe EditDialogUiState.Hidden
                    }
                }
            }
        }
    },
)
