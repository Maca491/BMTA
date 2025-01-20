    package com.example.puzzleapp

    import android.content.ClipDescription
    import androidx.compose.foundation.ExperimentalFoundationApi
    import androidx.compose.foundation.background
    import androidx.compose.foundation.border
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.aspectRatio
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.remember
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.unit.dp
    import androidx.compose.foundation.draganddrop.dragAndDropTarget
    import androidx.compose.runtime.MutableState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.draganddrop.DragAndDropEvent
    import androidx.compose.ui.draganddrop.DragAndDropTarget
    import androidx.compose.ui.draganddrop.mimeTypes
    import androidx.compose.ui.draganddrop.toAndroidDragEvent
    import com.example.puzzleapp.GameViewModel.Companion.GRID_SIZE
    import kotlinx.serialization.decodeFromString
    import kotlinx.serialization.json.Json

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun GameCell(
        row: Int,
        col: Int,
        isOccupied: MutableState<Boolean>,
        onShapeDropped: (Shape, Int, Int) -> Boolean
    ) {
        val dragAndDropTarget = remember {
            object : DragAndDropTarget {
                override fun onDrop(event: DragAndDropEvent): Boolean {
                    val clipData = event.toAndroidDragEvent().clipData
                    return if (clipData != null && clipData.itemCount > 0) {
                        val dragData = clipData.getItemAt(0).text?.toString()
                        if (!dragData.isNullOrEmpty()) {
                            val shape = Json.decodeFromString<Shape>(dragData)
                            return onShapeDropped(shape, row, col)
                        }
                        false
                    } else {
                        false
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .border(1.dp, Color.Black)
                .background(if (isOccupied.value) Color.Blue else Color.Gray)
                .dragAndDropTarget(
                    shouldStartDragAndDrop = { event ->
                        event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                    },
                    target = dragAndDropTarget
                )
        )
    }


