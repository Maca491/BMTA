package com.example.puzzleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.example.puzzleapp.ui.theme.PuzzleAppTheme
import androidx.compose.runtime.livedata.observeAsState

const val GRID_SIZE = 10 // Define the grid size for the game

class MainActivity : ComponentActivity() {
    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PuzzleAppTheme {
                // Observe the game state
                val cells by gameViewModel.cells.observeAsState(
                    Array(GRID_SIZE) { Array(GRID_SIZE) { mutableStateOf(false) } }
                )
                val availableShapes by gameViewModel.availableShapes.observeAsState(emptyList())
                var draggingShape by remember { mutableStateOf<Shapes?>(null) }
                var dragOffset by remember { mutableStateOf(Offset.Zero) }
                val score = remember { mutableStateOf(0) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        Column(modifier = Modifier.padding(innerPadding)) {
                            // Render the game board
                            GameBoard(
                                cells = cells,
                                draggingShape = draggingShape,
                                dragOffset = dragOffset,
                                onDrop = { shape, x, y ->
                                    // Handle the drop action
                                    gameViewModel.onShapeDropped(shape, x, y)
                                    draggingShape = null
                                },
                                score = score,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            // Render available shapes with draggable functionality
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                availableShapes.forEach { shape ->
                                    DraggableShape(
                                        shape = shape,
                                        initialOffset = dragOffset,
                                        onDrag = { offset ->
                                            dragOffset = offset
                                            draggingShape = shape
                                        },
                                        onDrop = { offset ->
                                            val gridX = (offset.x / 80.dp.value).toInt()
                                            val gridY = (offset.y / 80.dp.value).toInt()
                                            gameViewModel.onShapeDropped(shape, gridX, gridY)
                                            draggingShape = null
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}
