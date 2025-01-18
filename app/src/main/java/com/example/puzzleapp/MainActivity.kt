package com.example.puzzleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.puzzleapp.ui.theme.PuzzleAppTheme

class MainActivity : ComponentActivity() {
    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PuzzleAppTheme {
                val boardOffset = remember { mutableStateOf(Offset.Zero) }
                val boardSize = remember { mutableStateOf(IntSize.Zero) }
                val cells by gameViewModel.cellsFlow.collectAsStateWithLifecycle()
                val availableShapes by gameViewModel.availableShapesFlow.collectAsStateWithLifecycle()
                val density = resources.displayMetrics.density // Získejte hustotu obrazovky
                val cellSizePx = 80 * density // Vypočítejte velikost buňky v pixelech

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        Column(modifier = Modifier.padding(innerPadding)) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .onGloballyPositioned { layoutCoordinates ->
                                        boardOffset.value = layoutCoordinates.positionInRoot()
                                        boardSize.value = IntSize(
                                            layoutCoordinates.size.width,
                                            layoutCoordinates.size.height
                                        )
                                    }
                            ) {
                                GameBoard(
                                    cells = cells,
                                    onDrop = { shape, x, y ->
                                        gameViewModel.onShapeDropped(shape, x, y)
                                    },
                                    boardOffset = boardOffset.value,
                                    cellSizePx = cellSizePx,
                                    gridSize = 10
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            // Zobrazte dostupné tvary s možností přetahování
                            // Zobrazte dostupné tvary s možností přetahování
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                if (availableShapes.isEmpty()) {
                                    println("Shapes are empty") // Debug
                                } else {
                                    println("Available shapes in MainActivity: $availableShapes")
                                    availableShapes.forEach { shape ->
                                        DraggableShape (
                                            shape = shape
                                        )
                                    }
                                }
                            }

                        }
                    }
                )
            }
        }
    }
}
