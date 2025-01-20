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
    val rotatedParts = shape.parts.rotate(shape.orientation) // Používáme ShapePart

    // Zjištění šířky a výšky tvaru
    val width = (rotatedParts.maxOfOrNull { it.dy } ?: 0) + 1
    val height = (rotatedParts.maxOfOrNull { it.dx } ?: 0) + 1

    Box(
        modifier = Modifier
            .size(cellSize.dp * width, cellSize.dp * height)
    ) {
        rotatedParts.forEach { part ->
            val offsetX = part.dy * cellSize
            val offsetY = part.dx * cellSize
            Box(
                modifier = Modifier
                    .offset(offsetX.dp, offsetY.dp)
                    .size(cellSize.dp)
                    .background(getColorFromName(shape.color))
                    .border(2.dp, Color.Black) // Debug border
            )
        }
        println("Rendering ShapePreview with parts: ${shape.parts} and color: ${shape.color}")
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

