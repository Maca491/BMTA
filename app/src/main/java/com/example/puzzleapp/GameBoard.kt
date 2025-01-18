package com.example.puzzleapp

import android.content.ClipDescription
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameBoard(
    cells: Array<Array<MutableState<Boolean>>>,
    onDrop: (Shape, Int, Int) -> Unit,
    boardOffset: Offset,
    cellSizePx: Float,
    gridSize: Int
) {
    var isDraggingOver by remember { mutableStateOf(false) }

    val dropTarget = remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val androidEvent = event.toAndroidDragEvent() // Převod na Android drag event
                val draggedData = androidEvent.clipData.getItemAt(0).text?.toString()

                if (draggedData.isNullOrEmpty()) {
                    println("Received empty draggedData")
                    return false
                }

                val shape = try {
                    Json.decodeFromString<Shape>(draggedData)
                } catch (e: Exception) {
                    println("Failed to decode Shape: ${e.message}")
                    return false
                }

                println("Drop received for shape: $shape")

                // Získání pozice z Android drag eventu
                val x = ((androidEvent.x - boardOffset.x) / cellSizePx).toInt()
                val y = ((androidEvent.y - boardOffset.y) / cellSizePx).toInt()

                if (canPlaceShape(cells, shape, x, y)) {
                    println("Placing shape at ($x, $y)")
                    onDrop(shape, x, y)
                    return true
                } else {
                    println("Cannot place shape at ($x, $y)")
                }
                return false
            }




            override fun onEntered(event: DragAndDropEvent) {
                isDraggingOver = true
            }

            override fun onExited(event: DragAndDropEvent) {
                isDraggingOver = false
            }

            override fun onEnded(event: DragAndDropEvent) {
                isDraggingOver = false
            }
        }
    }

    Box(
        modifier = Modifier
            .dragAndDropTarget(
                shouldStartDragAndDrop = { event ->
                    event.mimeTypes()
                        .contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                },
                target = dropTarget
            )
            .background(if (isDraggingOver) Color.LightGray else Color.Transparent)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(gridSize),
            modifier = Modifier.fillMaxSize()
        ) {
            items(gridSize * gridSize) { index ->
                val row = index / gridSize
                val col = index % gridSize
                val isOccupied = cells[row][col].value

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .border(1.dp, Color.Black)
                        .background(if (isOccupied) Color.Green else Color.Gray)
                )
            }
        }
    }
}


fun canPlaceShape(
    cells: Array<Array<MutableState<Boolean>>>,
    shape: Shape,
    x: Int,
    y: Int
): Boolean {
    return shape.pattern.all { (dx, dy) ->
        val targetX = x + dx
        val targetY = y + dy
        targetX in cells.indices && // Kontrola, že X je v mřížce
                targetY in cells[targetX].indices && // Kontrola, že Y je v mřížce
                !cells[targetX][targetY].value // Buňka není obsazená
    }
}
