package com.example.puzzleapp

import kotlinx.serialization.Serializable

@Serializable
data class ShapePart(
    val dx: Int,
    val dy: Int,
    val directions: List<Direction> // Směry, kterými pokračuje tvar
)

@Serializable
data class Shape(
    val parts: List<ShapePart>,
    val color: String,
    val orientation: Orientation
)

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

enum class Orientation {
    UP, RIGHT, DOWN, LEFT
}

fun List<ShapePart>.rotate(orientation: Orientation): List<ShapePart> {
    return when (orientation) {
        Orientation.UP -> this
        Orientation.RIGHT -> this.map { ShapePart(it.dy, -it.dx, rotateDirections(it.directions, Orientation.RIGHT)) }
        Orientation.DOWN -> this.map { ShapePart(-it.dx, -it.dy, rotateDirections(it.directions, Orientation.DOWN)) }
        Orientation.LEFT -> this.map { ShapePart(-it.dy, it.dx, rotateDirections(it.directions, Orientation.LEFT)) }
    }
}

fun rotateDirections(directions: List<Direction>, orientation: Orientation): List<Direction> {
    return directions.map { direction ->
        when (orientation) {
            Orientation.UP -> direction
            Orientation.RIGHT -> when (direction) {
                Direction.UP -> Direction.RIGHT
                Direction.RIGHT -> Direction.DOWN
                Direction.DOWN -> Direction.LEFT
                Direction.LEFT -> Direction.UP
            }
            Orientation.DOWN -> when (direction) {
                Direction.UP -> Direction.DOWN
                Direction.RIGHT -> Direction.LEFT
                Direction.DOWN -> Direction.UP
                Direction.LEFT -> Direction.RIGHT
            }
            Orientation.LEFT -> when (direction) {
                Direction.UP -> Direction.LEFT
                Direction.RIGHT -> Direction.UP
                Direction.DOWN -> Direction.RIGHT
                Direction.LEFT -> Direction.DOWN
            }
        }
    }
}
