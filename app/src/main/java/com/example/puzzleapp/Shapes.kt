package com.example.puzzleapp

data class Shapes(val pattern: List<Pair<Int, Int>>)

val LShape = Shapes(
    pattern = listOf(
        0 to 0, 1 to 0, 2 to 0, 2 to 1
    )
)
val IShape = Shapes(
    pattern = listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0)
)

val OShape = Shapes(
    pattern = listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1)
)

val TShape = Shapes(
    pattern = listOf(0 to 0, 1 to -1, 1 to 0, 1 to 1)
)

val ZShape = Shapes(
    pattern = listOf(0 to 0, 0 to 1, 1 to 1, 1 to 2)
)

val SShape = Shapes(
    pattern = listOf(0 to 1, 0 to 2, 1 to 0, 1 to 1)
)

