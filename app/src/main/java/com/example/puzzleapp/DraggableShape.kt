package com.example.puzzleapp

import android.content.ClipData
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.toAndroidDragEvent
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
                        val serializedShape = Json.encodeToString(shape)
                        val clipData = ClipData.newPlainText("shapeData", serializedShape)

                        // Logování obsahu ClipData
                        if (clipData.itemCount > 0) {
                            val itemText = clipData.getItemAt(0).text?.toString()
                            println("ClipData created with item text: $itemText")
                        } else {
                            println("ClipData is empty!")
                        }

                        println("Starting drag for shape: $serializedShape")

                        // Zahájení přenosu dat
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

