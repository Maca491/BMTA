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
import androidx.compose.ui.unit.IntOffset

@Composable
fun GameWithShapes(gridSize: Int, modifier: Modifier = Modifier) {
    val cells = remember { Array(gridSize) { Array(gridSize) { mutableStateOf(false) } } }
    val score = remember { mutableStateOf(0) }

    // Všechny dostupné tvary
    val allShapes = listOf(IShape, OShape, TShape, BigShape, I_Shape, SShape, ZShape, LShape)

    // Aktuálně vybrané tvary
    val availableShapes = remember { mutableStateListOf<Shapes>() }
    var selectedShape by remember { mutableStateOf<Shapes?>(null) }

    // Pokud seznam aktuálně vybraných tvarů je prázdný, vybereme náhodně nové 3
    if (availableShapes.isEmpty()) {
        availableShapes.addAll(allShapes.shuffled().take(3)) // Vybereme náhodně 3 tvary
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
            onDrop = { startX, startY ->
                selectedShape?.let { shape ->
                    // Pokus o umístění tvaru
                    if (placeShape(cells, shape, startX, startY, score)) {
                        selectedShape = null // Reset výběru po položení
                        availableShapes.remove(shape) // Odebereme použitý tvar
                    }
                }
            },
            Modifier.weight(1f)
        )

        // Nabídka tvarů
        ShapeSelection(shapes = availableShapes, onShapeSelected = { shape ->
            selectedShape = shape // Nastavení vybraného tvaru
        })

        // DraggableShape pro aktivní tvar, který se přetahuje
        selectedShape?.let { shape ->
            DraggableShape(
                shape = shape,
                onDrop = { startX, startY ->
                    if (placeShape(cells, shape, startX, startY, score)) {
                        selectedShape = null // Reset vybraného tvaru po vložení
                        availableShapes.remove(shape) // Odebereme tvar z nabídky
                        true // Signalizujeme úspěšné vložení
                    } else {
                        false // Signalizujeme neúspěch
                    }
                },
                onCancel = {
                    selectedShape = null // Reset vybraného tvaru, pokud se nevloží
                }
            )
        }


    }
}

@Composable
fun GameBoard(
    cells: Array<Array<MutableState<Boolean>>>,
    score: MutableState<Int>,
    onDrop: (Int, Int) -> Unit, // Callback pro položení tvaru
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

            val color by animateColorAsState(
                targetValue = if (cells[row][col].value) Color.Green else Color.Gray
            )

            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .border(1.dp, Color.Black)
                    .background(color)
                    .clickable {
                        // Po kliknutí zavoláme callback s pozicí
                        onDrop(row, col)
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
                        onShapeSelected(shape) // Aktivace tvaru pro přetahování
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
                .background(Color.Green) // Barva čtverce
        )
    }
}

@Composable
fun DraggableShape(
    shape: Shapes,
    onDrop: (Int, Int) -> Boolean, // Vrací true, pokud se tvar vloží
    onCancel: () -> Unit // Vrací tvar zpět do výběru
) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    val gridSize = 80.dp // Velikost čtverce mřížky

    Box(
        modifier = Modifier
            .offset { IntOffset(offset.x.toInt(), offset.y.toInt()) }
            .size(gridSize * 4) // Nastavíme dostatečnou velikost pro pohyb tvaru
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume() // Spotřebujeme gesto
                        offset += Offset(dragAmount.x, dragAmount.y) // Aktualizujeme pozici
                    },
                    onDragEnd = {
                        val gridX = (offset.x / gridSize.toPx()).toInt()
                        val gridY = (offset.y / gridSize.toPx()).toInt()

                        // Pokud je možné tvar vložit, vložíme ho, jinak reset
                        if (!onDrop(gridX, gridY)) {
                            offset = Offset.Zero // Reset pozice
                            onCancel() // Vrátíme tvar do výběru
                        }
                    },
                    onDragCancel = {
                        offset = Offset.Zero // Reset pozice při zrušení
                        onCancel() // Vrátíme tvar do výběru
                    }
                )
            }
    ) {
        ShapePreview(shape = shape) // Zobrazíme náhled tvaru
    }
}



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