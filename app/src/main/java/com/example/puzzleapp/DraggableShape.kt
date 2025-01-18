package com.example.puzzleapp

import android.content.ClipData
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableShape(
    shape: Shape
) {
    val clipData = try {
        ClipData.newPlainText("shape", Json.encodeToString(shape))
    } catch (e: Exception) {
        null
    }

    if (clipData != null) {
        println("Rendering DraggableShape with pattern: ${shape.pattern} and color: ${shape.color}")
        Box(
            modifier = Modifier
                .dragAndDropSource {
                    detectTapGestures(onPress = {
                        println("Starting drag for shape: $shape")
                        startTransfer(
                            DragAndDropTransferData(clipData)
                        )
                    })
                }
        ) {
            println("Calling ShapePreview for shape: ${shape.pattern}")
            ShapePreview(shape = shape)
        }
    } else {
        println("ClipData is null for shape: $shape")
    }
}

