package com.example.puzzleapp

import androidx.compose.ui.graphics.Color

data class Shapes(val pattern: List<Pair<Int, Int>>, val color: Color)

// Define this function in an appropriate file, e.g., ShapesUtil.kt
fun generateShapes(): List<Shapes> {
    // Initialize and return a list of Shapes
    return listOf(IShape, OShape, TShape, BigShape, I_Shape, SShape, ZShape, LShape)
}

val LShape = Shapes(
    pattern = listOf(0 to 0, 1 to 0, 2 to 0, 2 to 1),
    color = Color.Blue
)

val IShape = Shapes(
    pattern = listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3),
    color = Color.Red
)

val I_Shape = Shapes(
    pattern = listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0),
    color = Color.Green
)

val OShape = Shapes(
    pattern = listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1),
    color = Color.Yellow
)

val BigShape = Shapes(
    pattern = listOf(0 to 0, 0 to 1, 0 to 2, 1 to 0, 1 to 1, 1 to 2, 2 to 0, 2 to 1, 2 to 2),
    color = Color.Cyan
)

val TShape = Shapes(
    pattern = listOf(1 to 0, 1 to 1, 0 to 1, 1 to 2),
    color = Color.Magenta
)

val ZShape = Shapes(
    pattern = listOf(0 to 0, 0 to 1, 1 to 1, 1 to 2),
    color = Color.Cyan
)

val SShape = Shapes(
    pattern = listOf(0 to 1, 0 to 2, 1 to 0, 1 to 1),
    color = Color.Red
)
