package com.example.puzzleapp

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun GameBoard(
    cells: Array<Array<MutableState<Boolean>>>,
    draggingShape: Shapes?,
    dragOffset: Offset,
    onDrop: (Shapes, Int, Int) -> Unit,
    score: MutableState<Int>, // Added score parameter
    modifier: Modifier = Modifier
) {
    val gridSize = cells.size
    val density = LocalDensity.current
    val cellSizePx = with(density) { 40.dp.toPx() } // Ensure proper alignment

    Column(modifier = modifier) {
        // Display score at the top of the game board
        Text(
            text = "Score: ${score.value}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(gridSize),
            modifier = Modifier.fillMaxSize()
        ) {
            items(gridSize * gridSize) { index ->
                val row = index / gridSize
                val col = index % gridSize

                val isOccupied = cells[row][col].value
                val isPreview = draggingShape != null && canPlaceShape(
                    cells, draggingShape, (dragOffset.x / cellSizePx).toInt(), (dragOffset.y / cellSizePx).toInt()
                )

                val color by animateColorAsState(
                    targetValue = when {
                        isOccupied -> Color.Green
                        isPreview -> Color.DarkGray
                        else -> Color.Gray
                    }
                )

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .border(1.dp, Color.Black)
                        .background(color)
                )
            }
        }
    }
}

@Composable
fun DraggableShape(
    shape: Shapes,
    initialOffset: Offset = Offset.Zero,
    onDrag: (Offset) -> Unit,
    onDrop: (Offset) -> Unit
) {
    var offset by remember { mutableStateOf(initialOffset) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offset += dragAmount
                        onDrag(offset)
                    },
                    onDragEnd = {
                        onDrop(offset)
                    }
                )
            }
    ) {
        ShapePreview(shape)
    }
}

@Composable
fun ShapePreview(shape: Shapes) {
    val size = 20 // Size of each grid cell in the shape

    Box(
        modifier = Modifier.size(size.dp * 4) // Corrected to use Dp
    ) {
        shape.pattern.forEach { (dx, dy) ->
            val offsetX = size * dx;
            val offsetY = size * dy;
            Box(
                modifier = Modifier
                    .offset(offsetX.dp, offsetY.dp) // Corrected to use Dp
                    .size(size.dp) // Corrected to use Dp
                    .background(shape.color)
            )
        }
    }
}

fun canPlaceShape(
    cells: Array<Array<MutableState<Boolean>>>,
    shape: Shapes,
    x: Int,
    y: Int
): Boolean {
    return shape.pattern.all { (dx, dy) ->
        val newX = x + dx
        val newY = y + dy
        newX in cells.indices && newY in cells[newX].indices && !cells[newX][newY].value
    }
}
