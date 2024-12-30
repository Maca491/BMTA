package com.example.puzzleapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.MutableState
import androidx.compose.foundation.border
import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.getValue

@Composable
fun GameBoard(gridSize: Int, modifier: Modifier = Modifier) {
    val cells = remember { Array(gridSize) { Array(gridSize) { mutableStateOf(false) } } }

    LazyVerticalGrid(
        columns = GridCells.Fixed(gridSize),
        modifier = modifier.fillMaxSize()
    ) {
        items(gridSize * gridSize) { index ->
            val row = index / gridSize
            val col = index % gridSize

            val color by animateColorAsState(
                targetValue = if (cells[row][col].value) Color.Green else Color.Gray
            )

            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .border(1.dp, Color.Black)
                    .background(color)
                    .clickable {
                        cells[row][col].value = !cells[row][col].value
                        checkAndClearLines(cells) // Kontrola a vyčištění
                    }
            )
        }
    }
}



fun checkAndClearLines(cells: Array<Array<MutableState<Boolean>>>) {
    val gridSize = cells.size
    val rowsToClear = mutableSetOf<Int>()
    val colsToClear = mutableSetOf<Int>()

    // Zaznamenej řádky k vyčištění
    for (row in 0 until gridSize) {
        if (cells[row].all { it.value }) {
            rowsToClear.add(row)
        }
    }

    // Zaznamenej sloupce k vyčištění
    for (col in 0 until gridSize) {
        if ((0 until gridSize).all { row -> cells[row][col].value }) {
            colsToClear.add(col)
        }
    }

    // Vyčisti označené řádky
    for (row in rowsToClear) {
        for (col in 0 until gridSize) {
            cells[row][col].value = false
        }
    }

    // Vyčisti označené sloupce
    for (col in colsToClear) {
        for (row in 0 until gridSize) {
            cells[row][col].value = false
        }
    }
}


fun canPlaceShape(board: Array<Array<MutableState<Boolean>>>, shape: Shapes, x: Int, y: Int): Boolean {
    return shape.pattern.all { (dx, dy) ->
        val newX = x + dx
        val newY = y + dy
        newX in board.indices && newY in board[newX].indices && !board[newX][newY].value
    }
}

