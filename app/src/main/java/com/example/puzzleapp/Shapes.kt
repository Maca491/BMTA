package com.example.puzzleapp

data class Shapes(val pattern: List<Pair<Int, Int>>)

val LShape = Shapes(
    pattern = listOf(
        0 to 0, 1 to 0, 2 to 0, 2 to 1
    )
)
