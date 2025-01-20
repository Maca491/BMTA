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
        col: Int
    ) {
        var isOccupied by remember { mutableStateOf(false) } // Reaktivní stav

        val dragAndDropTarget = remember {
            object : DragAndDropTarget {
                override fun onDrop(event: DragAndDropEvent): Boolean {
                    val clipData = event.toAndroidDragEvent().clipData
                    return if (clipData != null && clipData.itemCount > 0) {
                        val dragData = clipData.getItemAt(0).text?.toString()
                        if (!dragData.isNullOrEmpty()) {
                            val shape = Json.decodeFromString<Shape>(dragData)
                            println("Dropped shape at row=$row, col=$col: $shape")

                            val shapeFits = shape.pattern.rotate(shape.orientation).all { (dx, dy) ->
                                val targetRow = row + dx
                                val targetCol = col + dy
                                targetRow in 0 until GameViewModel.GRID_SIZE && targetCol in 0 until GameViewModel.GRID_SIZE && !isOccupied
                            }

                            if (shapeFits) {
                                shape.pattern.rotate(shape.orientation).forEach { (dx, dy) ->
                                    val targetRow = row + dx
                                    val targetCol = col + dy
                                    // Logika pro aktualizaci stavu buňky
                                    println("Marking cell at row=$targetRow, col=$targetCol as occupied")
                                }
                                isOccupied = true
                                true
                            } else {
                                println("Shape does not fit at row=$row, col=$col")
                                false
                            }
                        } else {
                            println("Drop data is null or empty at row=$row, col=$col")
                            false
                        }
                    } else {
                        println("ClipData is null or empty at row=$row, col=$col")
                        false
                    }
                }

                override fun onEntered(event: DragAndDropEvent) {
                    println("Drag entered cell at row=$row, col=$col")
                }

                override fun onExited(event: DragAndDropEvent) {
                    println("Drag exited cell at row=$row, col=$col")
                }

                override fun onStarted(event: DragAndDropEvent) {
                    println("Drag started at row=$row, col=$col")
                }

                override fun onEnded(event: DragAndDropEvent) {
                    println("Drag ended at row=$row, col=$col")
                }
            }
        }

        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .border(1.dp, Color.Black)
                .background(if (isOccupied) Color.Blue else Color.Gray)
                .dragAndDropTarget(
                    shouldStartDragAndDrop = { event ->
                        event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                    },
                    target = dragAndDropTarget
                )
        )

        fun changeOccupied(): Boolean{
            return !isOccupied
        }
    }