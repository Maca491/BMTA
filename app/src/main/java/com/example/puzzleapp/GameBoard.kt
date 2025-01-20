package com.example.puzzleapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameBoard(
    gridSize: Int,
    score: Int,
    cells: Array<Array<MutableState<Boolean>>>,
    onShapeDropped: (Shape, Int, Int) -> Boolean
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Zobrazení skóre
        Text(
            text = "Score: $score",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineSmall
        )

        // Hrací plocha
        LazyVerticalGrid(
            columns = GridCells.Fixed(gridSize),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(gridSize * gridSize) { index ->
                val row = index / gridSize
                val col = index % gridSize
                GameCell(
                    row = row,
                    col = col,
                    isOccupied = cells[row][col],
                    onShapeDropped = { shape, baseRow, baseCol ->
                        onShapeDropped(shape, baseRow, baseCol)
                    }
                )
            }
        }
    }
}
