package com.example.puzzleapp

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset


@Composable
fun ShapeSelection(
    shapes: List<Shapes>,
    onShapeSelected: (Shapes, Offset) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        shapes.forEach { shape ->
            var dragOffset by remember { mutableStateOf(Offset.Zero) }

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .offset { IntOffset(dragOffset.x.toInt(), dragOffset.y.toInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragOffset += dragAmount
                            },
                            onDragEnd = {
                                onShapeSelected(shape, dragOffset)
                                dragOffset = Offset.Zero
                            }
                        )
                    }
                    .background(Color.LightGray)
            ) {
                ShapePreview(shape)
            }
        }
    }
}
