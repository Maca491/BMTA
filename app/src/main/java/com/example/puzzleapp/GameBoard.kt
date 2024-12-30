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

@Composable
fun GameBoard(gridSize: Int, modifier: Modifier = Modifier) {
    // Ukládáme stav mřížky pomocí `remember` a `mutableStateOf`.
    val cells = remember { Array(gridSize) { Array(gridSize) { mutableStateOf(false) } } }

    // LazyVerticalGrid pro vykreslení mřížky
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridSize),
        modifier = modifier.fillMaxSize()
    ) {
        items(gridSize * gridSize) { index ->
            val row = index / gridSize
            val col = index % gridSize

            Box(
                modifier = Modifier
                    .aspectRatio(1f) // Čtvercová buňka
                    .background(if (cells[row][col].value) Color.Green else Color.Gray)
                    .clickable {
                        // Přepnutí stavu buňky
                        cells[row][col].value = !cells[row][col].value
                    }
            )
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

