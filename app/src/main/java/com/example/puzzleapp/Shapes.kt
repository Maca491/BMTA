package com.example.puzzleapp

import androidx.compose.ui.graphics.Color

data class Shape(
    val pattern: List<Pair<Int, Int>>,
    val color: Color,
    val orientation: Orientation = Orientation.UP
)

enum class Orientation {
    UP, RIGHT, DOWN, LEFT
}

val LShape = Shape(
    pattern = listOf(0 to 0, 1 to 0, 2 to 0, 2 to 1),
    color = Color.Blue,
    orientation = Orientation.UP
)

val IShape = Shape(
    pattern = listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3),
    color = Color.Red,
    orientation = Orientation.UP
)

val I_Shape = Shape(
    pattern = listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0),
    color = Color.Green,
    orientation = Orientation.UP
)

val OShape = Shape(
    pattern = listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1),
    color = Color.Yellow,
    orientation = Orientation.UP
)

val BigShape = Shape(
    pattern = listOf(0 to 0, 0 to 1, 0 to 2, 1 to 0, 1 to 1, 1 to 2, 2 to 0, 2 to 1, 2 to 2),
    color = Color.Cyan,
    orientation = Orientation.UP
)

val TShape = Shape(
    pattern = listOf(1 to 0, 1 to 1, 0 to 1, 1 to 2),
    color = Color.Magenta,
    orientation = Orientation.UP
)

val ZShape = Shape(
    pattern = listOf(0 to 0, 0 to 1, 1 to 1, 1 to 2),
    color = Color.Cyan,
    orientation = Orientation.UP
)

val SShape = Shape(
    pattern = listOf(0 to 1, 0 to 2, 1 to 0, 1 to 1),
    color = Color.Red,
    orientation = Orientation.UP
)

fun List<Pair<Int, Int>>.rotate(orientation: Orientation): List<Pair<Int, Int>> {
    return when (orientation) {
        Orientation.UP -> this
        Orientation.RIGHT -> this.map { (x, y) -> Pair(y, -x) }
        Orientation.DOWN -> this.map { (x, y) -> Pair(-x, -y) }
        Orientation.LEFT -> this.map { (x, y) -> Pair(-y, x) }
    }
}
