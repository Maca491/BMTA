package com.example.puzzleapp

import kotlinx.serialization.Serializable

@Serializable
data class Shape(
    val pattern: List<Pair<Int, Int>>,
    val color: String, // Použijte String místo Color pro snadnou serializaci
    val orientation: Orientation
)

enum class Orientation {
    UP, RIGHT, DOWN, LEFT
}

val LShape = Shape(
    pattern = listOf(0 to 0, 1 to 0, 2 to 0, 2 to 1),
    color = "Blue",
    orientation = Orientation.UP
)

val IShape = Shape(
    pattern = listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3),
    color = "Red",
    orientation = Orientation.UP
)

val I_Shape = Shape(
    pattern = listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0),
    color = "Green",
    orientation = Orientation.UP
)

val OShape = Shape(
    pattern = listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1),
    color = "Yellow",
    orientation = Orientation.UP
)

val BigShape = Shape(
    pattern = listOf(0 to 0, 0 to 1, 0 to 2, 1 to 0, 1 to 1, 1 to 2, 2 to 0, 2 to 1, 2 to 2),
    color = "Cyan",
    orientation = Orientation.UP
)

val TShape = Shape(
    pattern = listOf(1 to 0, 1 to 1, 0 to 1, 1 to 2),
    color = "Magenta",
    orientation = Orientation.UP
)

val ZShape = Shape(
    pattern = listOf(0 to 0, 0 to 1, 1 to 1, 1 to 2),
    color = "Cyan",
    orientation = Orientation.UP
)

val SShape = Shape(
    pattern = listOf(0 to 1, 0 to 2, 1 to 0, 1 to 1),
    color = "Red",
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
