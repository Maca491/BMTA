package com.example.puzzleapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class GameViewModel : ViewModel() {

    private val _scoreFlow = MutableStateFlow(0)
    val scoreFlow: StateFlow<Int> = _scoreFlow.asStateFlow()

    private val _cellsFlow = MutableStateFlow(
        Array(GRID_SIZE) { Array(GRID_SIZE) { mutableStateOf(false) } }
    )
    val cellsFlow: StateFlow<Array<Array<MutableState<Boolean>>>> = _cellsFlow.asStateFlow()

    private val _availableShapesFlow = MutableStateFlow(generateInitialShapes())
    val availableShapesFlow: StateFlow<List<Shape>> = _availableShapesFlow.asStateFlow()

    fun onShapeDropped(shape: Shape, baseRow: Int, baseCol: Int): Boolean {
        val rotatedParts = shape.parts.rotate(shape.orientation)
        val cells = _cellsFlow.value

        // Validace, zda lze tvar vložit
        if (!canPlaceShape(cells, rotatedParts, baseRow, baseCol)) {
            println("Cannot place shape at row=$baseRow, col=$baseCol.")
            return false
        }

        // Aktualizace stavu buněk
        var scoreIncrement = 0
        rotatedParts.forEach { part ->
            val targetRow = baseRow + part.dx
            val targetCol = baseCol + part.dy
            if (targetRow in cells.indices && targetCol in cells[targetRow].indices) {
                cells[targetRow][targetCol].value = true
                scoreIncrement++
            }
        }

        // Aktualizace skóre
        _scoreFlow.value += scoreIncrement
        checkAndClearFullRowsAndColumns()
        return true
    }

    private fun checkAndClearFullRowsAndColumns() {
        val cells = _cellsFlow.value
        var clearedRows = 0
        var clearedColumns = 0

        // Kontrola a vyčištění plných řádků
        for (row in cells.indices) {
            if (cells[row].all { it.value }) {
                clearedRows++
                for (col in cells[row].indices) {
                    cells[row][col].value = false
                }
            }
        }

        // Kontrola a vyčištění plných sloupců
        for (col in cells[0].indices) {
            if (cells.all { row -> row[col].value }) {
                clearedColumns++
                for (row in cells.indices) {
                    cells[row][col].value = false
                }
            }
        }

        // Aktualizace skóre
        val totalCleared = clearedRows + clearedColumns
        if (totalCleared > 0) {
            _scoreFlow.value += totalCleared * GRID_SIZE * 100
        }
    }

    private fun canPlaceShape(
        cells: Array<Array<MutableState<Boolean>>>,
        parts: List<ShapePart>,
        baseRow: Int,
        baseCol: Int
    ): Boolean {
        return parts.all { part ->
            val targetRow = baseRow + part.dx
            val targetCol = baseCol + part.dy
            targetRow in cells.indices && targetCol in cells[targetRow].indices && !cells[targetRow][targetCol].value
        }
    }


    private fun replaceShape(shape: Shape) {
        val shapes = _availableShapesFlow.value.toMutableList()
        shapes.remove(shape)
        shapes.add(generateRandomShape())
        _availableShapesFlow.value = shapes
    }

    private fun generateRandomShape(): Shape {
        val allShapes = listOf(
            Shape(
                parts = listOf(
                    ShapePart(0, 0, listOf(Direction.RIGHT, Direction.DOWN)),
                    ShapePart(0, 1, listOf(Direction.LEFT)),
                    ShapePart(1, 0, listOf(Direction.UP)),
                    ShapePart(1, 1, listOf(Direction.UP))
                ),
                color = "Yellow",
                orientation = Orientation.UP
            ),
            Shape(
                parts = listOf(
                    ShapePart(0, 0, listOf(Direction.DOWN)),
                    ShapePart(1, 0, listOf(Direction.UP, Direction.DOWN)),
                    ShapePart(2, 0, listOf(Direction.UP, Direction.DOWN)),
                    ShapePart(3, 0, listOf(Direction.UP))
                ),
                color = "Green",
                orientation = Orientation.UP
            ),
            Shape(
                parts = listOf(
                    ShapePart(0, 0, listOf(Direction.RIGHT)),
                    ShapePart(0, 1, listOf(Direction.LEFT, Direction.RIGHT)),
                    ShapePart(0, 2, listOf(Direction.LEFT)),
                    ShapePart(1, 1, listOf(Direction.UP))
                ),
                color = "Purple",
                orientation = Orientation.UP
            )
        )
        return allShapes.random()
    }

    private fun generateInitialShapes(): List<Shape> {
        return listOf(
            Shape(
                parts = listOf(
                    ShapePart(0, 0, listOf(Direction.RIGHT, Direction.DOWN)),
                    ShapePart(0, 1, listOf(Direction.LEFT)),
                    ShapePart(1, 0, listOf(Direction.UP)),
                    ShapePart(1, 1, listOf(Direction.UP))
                ),
                color = "Yellow",
                orientation = Orientation.UP
            ),
            Shape(
                parts = listOf(
                    ShapePart(0, 0, listOf(Direction.DOWN)),
                    ShapePart(1, 0, listOf(Direction.UP, Direction.DOWN)),
                    ShapePart(2, 0, listOf(Direction.UP, Direction.DOWN)),
                    ShapePart(3, 0, listOf(Direction.UP))
                ),
                color = "Green",
                orientation = Orientation.UP
            ),
            Shape(
                parts = listOf(
                    ShapePart(0, 0, listOf(Direction.RIGHT)),
                    ShapePart(0, 1, listOf(Direction.LEFT, Direction.RIGHT)),
                    ShapePart(0, 2, listOf(Direction.LEFT)),
                    ShapePart(1, 1, listOf(Direction.UP))
                ),
                color = "Purple",
                orientation = Orientation.UP
            )
        )
    }

    companion object {
        const val GRID_SIZE = 10
    }
}
