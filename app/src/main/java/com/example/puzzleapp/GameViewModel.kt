package com.example.puzzleapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class GameViewModel : ViewModel() {
    private val _cells = MutableLiveData(
        Array(GRID_SIZE) { Array(GRID_SIZE) { mutableStateOf(false) } }
    )
    val cells: LiveData<Array<Array<MutableState<Boolean>>>> = _cells

    private val _availableShapes = MutableLiveData<List<Shapes>>(generateShapes())
    val availableShapes: LiveData<List<Shapes>> = _availableShapes

    fun onShapeDropped(shape: Shapes, x: Int, y: Int) {
        val currentCells = _cells.value ?: return

        // Ověření, zda lze tvar umístit na zadané souřadnice
        if (canPlaceShape(currentCells, shape, x, y)) {
            // Aktualizace stavu herní plochy
            shape.pattern.forEach { (dx, dy) ->
                val newX = x + dx
                val newY = y + dy
                if (newX in 0 until GRID_SIZE && newY in 0 until GRID_SIZE) {
                    currentCells[newX][newY].value = true
                }
            }
            // Aktualizace LiveData
            _cells.value = currentCells
        }
    }

    private fun canPlaceShape(
        cells: Array<Array<MutableState<Boolean>>>,
        shape: Shapes,
        x: Int,
        y: Int
    ): Boolean {
        return shape.pattern.all { (dx, dy) ->
            val newX = x + dx
            val newY = y + dy
            newX in 0 until GRID_SIZE && newY in 0 until GRID_SIZE && !cells[newX][newY].value
        }
    }
}

