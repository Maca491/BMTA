package com.example.puzzleapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

const val GRID_SIZE = 10 // Definujte velikost mřížky pro hru

class GameViewModel : ViewModel() {
    private val _cellsFlow = MutableStateFlow(
        Array(GRID_SIZE) { Array(GRID_SIZE) { mutableStateOf(false) } }
    )
    val cellsFlow: StateFlow<Array<Array<MutableState<Boolean>>>> = _cellsFlow.asStateFlow()

    private val _availableShapesFlow = MutableStateFlow(generateInitialShapes())
    val availableShapesFlow: StateFlow<List<Shape>> = _availableShapesFlow.asStateFlow()

    fun canPlaceShape(
        shape: Shape,
        x: Int,
        y: Int
    ): Boolean {
        val cells = _cellsFlow.value
        val rotatedPattern = shape.pattern.rotate(shape.orientation)
        return rotatedPattern.all { (dx, dy) ->
            val newX = x + dx
            val newY = y + dy
            newX in cells.indices && newY in cells[newX].indices && !cells[newX][newY].value
        }
    }

    fun onShapeDropped(shape: Shape, x: Int, y: Int) {
        if (canPlaceShape(shape, x, y)) {
            val cells = _cellsFlow.value
            val rotatedPattern = shape.pattern.rotate(shape.orientation)
            rotatedPattern.forEach { (dx, dy) ->
                val newX = x + dx
                val newY = y + dy
                cells[newX][newY].value = true
            }
            _cellsFlow.value = cells // Aktualizujte stav
        }
    }

    fun replaceShape(shape: Shape) {
        val shapes = _availableShapesFlow.value.toMutableList()
        shapes.remove(shape)
        shapes.add(generateRandomShape())
        _availableShapesFlow.value = shapes
    }

    private fun generateRandomShape(): Shape {
        val allShapes = listOf(IShape, OShape, TShape, BigShape, I_Shape, SShape, ZShape, LShape)
        return allShapes.random()
    }

    private fun generateInitialShapes(): List<Shape> {
        return List(3) { generateRandomShape() }
    }
}
