package com.example.puzzleapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ShapePreview(shape: Shape) {
    val cellSize = 40 // Size of each cell in dp
    val rotatedPattern = shape.pattern.rotate(shape.orientation)

    val width = (rotatedPattern.maxOfOrNull { it.second } ?: 0) + 1
    val height = (rotatedPattern.maxOfOrNull { it.first } ?: 0) + 1

    Box(
        modifier = Modifier
            .size(cellSize.dp * width, cellSize.dp * height)
    ) {
        rotatedPattern.forEach { (dx, dy) ->
            val offsetX = dy * cellSize
            val offsetY = dx * cellSize
            Box(
                modifier = Modifier
                    .offset(offsetX.dp, offsetY.dp)
                    .size(cellSize.dp)
                    .background(getColorFromName(shape.color))
                    .border(2.dp, Color.Black) // Debug border
            )
            println("Rendering ShapePreview with pattern: ${shape.pattern} and color: ${shape.color}")
        }
    }
}

fun getColorFromName(colorName: String): Color {
    return when (colorName.lowercase()) {
        "red" -> Color.Red
        "blue" -> Color.Blue
        "green" -> Color.Green
        "yellow" -> Color.Yellow
        "cyan" -> Color.Cyan
        "magenta" -> Color.Magenta
        "gray" -> Color.Gray
        "black" -> Color.Black
        "white" -> Color.White
        else -> Color.Transparent // Výchozí barva, pokud není rozpoznána
    }
}

