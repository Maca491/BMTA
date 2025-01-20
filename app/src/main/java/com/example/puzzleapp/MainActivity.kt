package com.example.puzzleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
                val cells by gameViewModel.cellsFlow.collectAsStateWithLifecycle()
                val availableShapes by gameViewModel.availableShapesFlow.collectAsStateWithLifecycle()
                val gridSize = 10
                val score by gameViewModel.scoreFlow.collectAsStateWithLifecycle()


                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(490.dp)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                GameBoard(
                                    gridSize = gridSize,
                                    score = score,
                                    cells = cells,
                                    onShapeDropped = gameViewModel::onShapeDropped
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                availableShapes.forEach { shape ->
                                    DraggableShape(shape = shape)
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}
