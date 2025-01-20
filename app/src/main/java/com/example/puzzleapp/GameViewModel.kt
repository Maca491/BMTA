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
        val cells = _cellsFlow.value
        val rotatedPattern = shape.pattern.rotate(shape.orientation)

        // Check if the shape can be placed
        if (!canPlaceShape(cells, rotatedPattern, baseRow, baseCol)) {
            println("Cannot place shape at row=$baseRow, col=$baseCol.")
            return false
        }

        // Place the shape on the grid
        rotatedPattern.forEach { (dx, dy) ->
            val targetRow = baseRow + dx
            val targetCol = baseCol + dy
            if (targetRow in cells.indices && targetCol in cells[targetRow].indices) {
                cells[targetRow][targetCol].value = true
            }
        }

        // Remove the placed shape from available shapes
        val updatedShapes = _availableShapesFlow.value.toMutableList()
        if (updatedShapes.contains(shape)) {
            updatedShapes.remove(shape)
            _availableShapesFlow.value = updatedShapes
        } else {
            println("Error: Shape not found in availableShapes.")
        }

        // Add a new random shape
        replaceShape(shape)

        return true
    }

    private fun canPlaceShape(
        cells: Array<Array<MutableState<Boolean>>>,
        rotatedPattern: List<Pair<Int, Int>>,
        baseRow: Int,
        baseCol: Int
    ): Boolean {
        return rotatedPattern.all { (dx, dy) ->
            val targetRow = baseRow + dx
            val targetCol = baseCol + dy
            targetRow in cells.indices &&
                    targetCol in cells[targetRow].indices &&
                    !cells[targetRow][targetCol].value
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
            Shape(listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1), "Yellow", Orientation.UP),
            Shape(listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0), "Green", Orientation.UP),
            Shape(listOf(0 to 0, 0 to 1, 0 to 2, 1 to 1), "Purple", Orientation.UP)
        )
        return allShapes.random()
    }

    private fun generateInitialShapes(): List<Shape> {
        return listOf(
            Shape(listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1), "Yellow", Orientation.UP),
            Shape(listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0), "Green", Orientation.UP),
            Shape(listOf(0 to 0, 0 to 1, 0 to 2, 1 to 1), "Purple", Orientation.UP)
        )
    }

    companion object {
        const val GRID_SIZE = 10
    }
}
