package com.example.puzzleapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun GameBoard(gridSize: Int) {
    val cells = remember {
        Array(gridSize) { Array(gridSize) { mutableStateOf(false) } }
    }

    fun canPlaceShape(shape: Shape, baseRow: Int, baseCol: Int): Boolean {
        val rotatedParts = shape.parts.rotate(shape.orientation)
        return rotatedParts.all { part ->
            val targetRow = baseRow + part.dx
            val targetCol = baseCol + part.dy
            targetRow in 0 until gridSize && // Je buňka v mřížce?
                    targetCol in 0 until gridSize &&
                    !cells[targetRow][targetCol].value // Není buňka již obsazena?
        }
    }

    fun handlePartDropped(shape: Shape, baseRow: Int, baseCol: Int): Boolean {
        if (!canPlaceShape(shape, baseRow, baseCol)) {
            println("Cannot place shape at row=$baseRow, col=$baseCol")
            return false
        }

        // Pokud validace proběhla, aktualizujeme všechny buňky
        val rotatedParts = shape.parts.rotate(shape.orientation)
        rotatedParts.forEach { part ->
            val targetRow = baseRow + part.dx
            val targetCol = baseCol + part.dy
            cells[targetRow][targetCol].value = true
        }
        return true
    }




    LazyVerticalGrid(
        columns = GridCells.Fixed(gridSize),
        modifier = Modifier.fillMaxSize()
    ) {
        items(gridSize * gridSize) { index ->
            val row = index / gridSize
            val col = index % gridSize
            GameCell(
                row = row,
                col = col,
                isOccupied = cells[row][col],
                onShapeDropped = ::handlePartDropped
            )
        }
    }
}

