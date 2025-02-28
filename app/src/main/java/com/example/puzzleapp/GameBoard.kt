package com.example.puzzleapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.MutableState
import androidx.compose.foundation.border
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset


@Composable
fun GameWithShapes(gridSize: Int, modifier: Modifier = Modifier) {
    val cells = remember { Array(gridSize) { Array(gridSize) { mutableStateOf(false) } } }
    val score = remember { mutableStateOf(0) }
    val allShapes = listOf(IShape, OShape, TShape, BigShape, I_Shape, SShape, ZShape, LShape)
    val availableShapes = remember { mutableStateListOf<Shapes>() }
    var draggingShape by remember { mutableStateOf<Shapes?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var previewStart by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    // Získání hustoty obrazovky
    val density = LocalDensity.current
    val cellSizePx = with(density) { 80.dp.toPx() } // Převod dp na px

    // Naplnění výběru tvarů, pokud je prázdný
    if (availableShapes.isEmpty()) {
        availableShapes.addAll(allShapes.shuffled().take(3))
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Skóre
        Text(
            text = "Skóre: ${score.value}",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineSmall
        )

        // Herní mřížka
        GameBoard(
            cells = cells,
            score = score,
            previewStart = previewStart,
            draggingShape = draggingShape,
            onDrop = { startX, startY ->
                draggingShape?.let { shape ->
                    if (placeShape(cells, shape, startX, startY, score)) {
                        draggingShape = null
                        previewStart = null
                        true // Signalizace úspěšného vložení
                    } else {
                        availableShapes.add(shape) // Vrátíme tvar do nabídky
                        draggingShape = null
                        previewStart = null
                        false // Signalizace neúspěchu
                    }
                } ?: false
            },
            Modifier.weight(1f)
        )

        // Nabídka tvarů
        ShapeSelection(
            shapes = availableShapes,
            onShapeSelected = { shape ->
                draggingShape = shape
                availableShapes.remove(shape) // Tvar zmizí z nabídky
            }
        )

        // Přetahování aktivního tvaru
        /*draggingShape?.let { shape ->
            DraggableShape(
                shape = shape,
                initialOffset = dragOffset,
                onDrag = { offset ->
                    dragOffset = offset // Aktualizace pozice během přetahování
                    val gridX = (offset.x / cellSizePx).toInt()
                    val gridY = (offset.y / cellSizePx).toInt()
                    previewStart = if (canPlaceShape(cells, shape, gridX, gridY)) {
                        gridX to gridY
                    } else {
                        null
                    }
                },
                onDrop = { startX, startY ->
                    if (placeShape(cells, shape, startX, startY, score)) {
                        draggingShape = null
                        previewStart = null
                    } else {
                        availableShapes.add(shape) // Vrátíme tvar do nabídky
                        draggingShape = null
                        previewStart = null
                        dragOffset = Offset.Zero
                    }
                }
            )
        }*/
    }
}

@Composable
fun GameBoard(
    cells: Array<Array<MutableState<Boolean>>>,
    score: MutableState<Int>,
    previewStart: Pair<Int, Int>?,
    draggingShape: Shapes?,
    onDrop: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridSize = cells.size

    LazyVerticalGrid(
        columns = GridCells.Fixed(gridSize),
        modifier = modifier.fillMaxSize()
    ) {
        items(gridSize * gridSize) { index ->
            val row = index / gridSize
            val col = index % gridSize
            val isPreview =
                draggingShape != null && previewStart != null && draggingShape.pattern.any { (dx, dy) ->
                    previewStart.first + dx == row && previewStart.second + dy == col
                }

            val color by animateColorAsState(
                targetValue = when {
                    cells[row][col].value -> Color.Green
                    isPreview -> Color.DarkGray
                    else -> Color.Gray
                }
            )

            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .border(1.dp, Color.Black)
                    .background(color)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                onDrop(row, col)
                            }
                        )
                    }
            )
        }
    }
}


@Composable
fun ShapeSelection(
    shapes: List<Shapes>,
    onShapeSelected: (Shapes) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        shapes.forEach { shape ->
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clickable {
                        onShapeSelected(shape) // Předání tvaru do přetahování
                    }
            ) {
                ShapePreview(shape)
            }
        }
    }
}



@Composable
fun ShapePreview(shape: Shapes) {
    val size = 20.dp // Velikost jednotlivého čtverce

    // Projdeme všechny souřadnice tvaru
    shape.pattern.forEach { (dx, dy) ->
        Box(
            modifier = Modifier
                .offset(x = (dy * 20).dp, y = (dx * 20).dp) // Oprava zaměněných os
                .size(size) // Nastavení velikosti každého čtverce
                .background(shape.color) // Barva čtverce
        )
    }
}
/*
@Composable
fun DraggableShape(
    shape: Shapes,
    initialOffset: Offset = Offset.Zero,
    onDrag: (Offset) -> Unit,
    onDrop: (Int, Int) -> Unit
) {
    var offset by remember { mutableStateOf(initialOffset) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offset.x.toInt(), offset.y.toInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offset += dragAmount
                        onDrag(offset)
                    },
                    onDragEnd = {
                        val gridX = (offset.x / 80.dp.toPx()).toInt()
                        val gridY = (offset.y / 80.dp.toPx()).toInt()
                        onDrop(gridX, gridY)
                    }
                )
            }
    ) {
        ShapePreview(shape = shape)
    }
}
*/




fun checkAndClearLines(cells: Array<Array<MutableState<Boolean>>>, score: MutableState<Int>) {
    val gridSize = cells.size
    val rowsToClear = mutableSetOf<Int>()
    val colsToClear = mutableSetOf<Int>()

    // Zaznamenej řádky k vyčištění
    for (row in 0 until gridSize) {
        if (cells[row].all { it.value }) {
            rowsToClear.add(row)
        }
    }

    // Zaznamenej sloupce k vyčištění
    for (col in 0 until gridSize) {
        if ((0 until gridSize).all { row -> cells[row][col].value }) {
            colsToClear.add(col)
        }
    }

    // Vyčištění a aktualizace skóre
    for (row in rowsToClear) {
        for (col in 0 until gridSize) {
            cells[row][col].value = false
        }
        score.value += 10 // Přičtení bodů za řádek
    }

    for (col in colsToClear) {
        for (row in 0 until gridSize) {
            cells[row][col].value = false
        }
        score.value += 10 // Přičtení bodů za sloupec
    }
}

fun placeShape(cells: Array<Array<MutableState<Boolean>>>, shape: Shapes, startX: Int, startY: Int, score: MutableState<Int>): Boolean {
    if (canPlaceShape(cells, shape, startX, startY)) {
        // Umístění tvaru do mřížky
        shape.pattern.forEach { (dx, dy) ->
            cells[startX + dx][startY + dy].value = true
        }
        score.value += shape.pattern.size
        // Zavolání funkce pro kontrolu a vyčištění řádků a sloupců
        checkAndClearLines(cells, score)

        return true
    }
    return false
}

fun canPlaceShape(board: Array<Array<MutableState<Boolean>>>, shape: Shapes, x: Int, y: Int): Boolean {
    return shape.pattern.all { (dx, dy) ->
        val newX = x + dx
        val newY = y + dy
        newX in board.indices && newY in board[newX].indices && !board[newX][newY].value
    }
}