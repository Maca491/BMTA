package com.example.puzzleapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class GameViewModel : ViewModel() {

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
        rotatedParts.forEach { part ->
            val targetRow = baseRow + part.dx
            val targetCol = baseCol + part.dy
            if (targetRow in cells.indices && targetCol in cells[targetRow].indices) {
                cells[targetRow][targetCol].value = true
            }
        }

        // Odebrání tvaru ze seznamu dostupných tvarů
        val updatedShapes = _availableShapesFlow.value.toMutableList()
        updatedShapes.remove(shape)
        _availableShapesFlow.value = updatedShapes

        return true
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
        const val GRID_SIZE = 6
    }
}
