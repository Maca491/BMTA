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
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridSize),
        modifier = Modifier.fillMaxSize()
    ) {
        items(gridSize * gridSize) { index ->
            val row = index / gridSize
            val col = index % gridSize

            GameCell(row = row, col = col)
        }
    }
}