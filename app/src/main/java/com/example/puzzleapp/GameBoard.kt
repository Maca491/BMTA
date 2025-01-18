package com.example.puzzleapp

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun GameBoard(
    cells: Array<Array<MutableState<Boolean>>>,
    draggingShape: Shape?,
    dragOffset: Offset,
    onDrop: (Shape, Int, Int) -> Unit,
    score: MutableState<Int>,
    boardOffset: MutableState<Offset>, // MutableState to allow reassignment
    boardSize: MutableState<IntSize>, // MutableState to allow reassignment
    modifier: Modifier = Modifier
) {
    val gridSize = cells.size
    val density = LocalDensity.current
    val cellSizePx = with(density) { 40.dp.toPx() }

    Box(
        modifier = modifier
            .onGloballyPositioned { layoutCoordinates ->
                boardOffset.value = layoutCoordinates.positionInRoot()
                boardSize.value = IntSize(
                    layoutCoordinates.size.width,
                    layoutCoordinates.size.height
                )
            }
            .border(2.dp, Color.Red) // Ohraničení hrací plochy pro vizuální kontrolu
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(gridSize),
            modifier = Modifier.fillMaxSize()
        ) {
            items(gridSize * gridSize) { index ->
                val row = index / gridSize
                val col = index % gridSize

                val isOccupied = cells[row][col].value
                val isPreview = draggingShape != null && canPlaceShape(
                    cells,
                    draggingShape,
                    ((dragOffset.x - boardOffset.value.x) / cellSizePx).toInt(),
                    ((dragOffset.y - boardOffset.value.y) / cellSizePx).toInt()
                )

                val color by animateColorAsState(
                    targetValue = when {
                        isOccupied -> Color.Green
                        isPreview -> Color.DarkGray
                        else -> Color.Gray
                    }
                )

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .border(1.dp, Color.Black)
                        .background(color)
                )
            }
        }
    }
}

@Composable
fun DraggableShape(
    shape: Shape,
    initialOffset: Offset = Offset.Zero,
    cellSizePx: Float,
    boardOffset: Offset,
    boardSize: IntSize,
    onDrag: (Offset) -> Unit,
    onDrop: (Shape, Int, Int, Boolean) -> Unit
) {
    var offset by remember { mutableStateOf(initialOffset) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offset += dragAmount
                        onDrag(offset)
                    },
                    onDragEnd = {
                        val gridX = ((offset.x - boardOffset.x) / cellSizePx).toInt()
                        val gridY = ((offset.y - boardOffset.y) / cellSizePx).toInt()

                        // Validace pomocí definovaných hranic
                        val shapeBounds = calculateShapeBounds(shape, offset, cellSizePx)
                        val isValidDrop = isShapeWithinBoard(
                            shapeBounds,
                            boardOffset,
                            boardSize
                        )

                        onDrop(shape, gridX, gridY, isValidDrop)

                        if (!isValidDrop) {
                            offset = Offset.Zero // Reset, pokud je drop neplatný
                        }
                    }
                )
            }
    ) {
        ShapePreview(shape)
    }
}

@Composable
fun ShapePreview(shape: Shape) {
    val cellSize = 40 // Velikost každé buňky v dp

    // Získejte velikost a orientaci tvaru
    val rotatedPattern = shape.pattern.rotate(shape.orientation)

    // Vypočítejte celkovou velikost náhledu tvaru
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
                    .background(shape.color)
                    .border(2.dp, Color.Black) // Přidání ohraničení kolem každé buňky
            )
        }
    }
}

fun canPlaceShape(
    cells: Array<Array<MutableState<Boolean>>>,
    shape: Shape,
    x: Int,
    y: Int
): Boolean {
    return shape.pattern.all { (dx, dy) ->
        val newX = x + dx
        val newY = y + dy
        newX in cells.indices && newY in cells[newX].indices && !cells[newX][newY].value
    }
}

fun calculateShapeBounds(shape: Shape, offset: Offset, cellSizePx: Float): Rect {
    // Určete velikost a hranice tvaru podle jeho aktuální polohy
    val left = offset.x
    val top = offset.y
    val right = left + cellSizePx * (shape.pattern.maxOf { it.second } + 1)
    val bottom = top + cellSizePx * (shape.pattern.maxOf { it.first } + 1)
    return Rect(left, top, right, bottom)
}

fun isShapeWithinBoard(shapeBounds: Rect, boardOffset: Offset, boardSize: IntSize): Boolean {
    val boardRect = Rect(
        boardOffset.x,
        boardOffset.y,
        boardOffset.x + boardSize.width,
        boardOffset.y + boardSize.height
    )
    return boardRect.left <= shapeBounds.left &&
            boardRect.top <= shapeBounds.top &&
            boardRect.right >= shapeBounds.right &&
            boardRect.bottom >= shapeBounds.bottom
}
