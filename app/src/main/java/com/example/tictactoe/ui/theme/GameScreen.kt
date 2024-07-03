package com.example.tictactoe.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.provider.FontsContractCompat.Columns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe.BoardCellValue
import com.example.tictactoe.GameState
import com.example.tictactoe.GameViewModel
import com.example.tictactoe.UserActions
import com.example.tictactoe.VictoryTYpe
import java.nio.file.WatchEvent

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel
) {
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Player 'O': ${state.playerCircleCount}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Draw: ${state.drawCount}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Player 'X': ${state.playerCrossCount}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = "Tic Tac Toe",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive,
            color = BlueCustom
        )

        Text(
            text = state.hintText,
            fontSize = 30.sp,
            fontStyle = FontStyle.Italic
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))
                .background(GrayBackground),
            contentAlignment = Alignment.Center
        ) {
            BoardBase()
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .aspectRatio(1f),
                columns = GridCells.Fixed(3)
            ) {
                viewModel.boardItems.forEach { (cellNo, boardCellValue) ->
                    item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                viewModel.onAction(UserActions.BoardTapped(cellNo))
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AnimatedVisibility(
                            visible = viewModel.boardItems[cellNo] != BoardCellValue.NONE,
                            enter = scaleIn(tween(1000))
                        ) {
                            if(boardCellValue == BoardCellValue.CIRCLE) {
                                Circle()
                            } else if(boardCellValue == BoardCellValue.CROSS) {
                                Cross()
                            }
                        }
                    }
                }
            }
        }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(
                    visible = state.hasWon,
                    enter = fadeIn(tween(2000))
                ) {
                    DrawVictoryLine(state = state)
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                viewModel.onAction(
                    UserActions.PlayAgainButtonClicked
                )
            },
            shape = RoundedCornerShape(5.dp),
            elevation = ButtonDefaults.buttonElevation(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BlueCustom,
                contentColor = Color.White
            )
        ) {
            Text(text = "Play Again", fontSize = 16.sp)
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                viewModel.onAction(
                    UserActions.NewGameButtonClicked
                )
            },
            shape = RoundedCornerShape(5.dp),
            elevation = ButtonDefaults.buttonElevation(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = RedCustom,
                contentColor = Color.White
            )
        ) {
            Text(text = "New Game", fontSize = 16.sp)
        }

    }
}

@Composable
fun DrawVictoryLine(state: GameState) {
    when(state.victoryTYpe) {
        VictoryTYpe.VERTICAL1 -> WinVerticalLine1()
        VictoryTYpe.VERTICAL2 -> WinVerticalLine2()
        VictoryTYpe.VERTICAL3 -> WinVerticalLine3()
        VictoryTYpe.HORIZONTAL1 -> WinHorizontalLine1()
        VictoryTYpe.HORIZONTAL2 -> WinHorizontalLine2()
        VictoryTYpe.HORIZONTAL3 -> WinHorizontalLine3()
        VictoryTYpe.DIAGONAL1 -> WinDiagonalLine1()
        VictoryTYpe.DIAGONAL2 -> WinDiagonalLine2()
        VictoryTYpe.NONE -> {}
    }
}

@Preview
@Composable
fun Prev() {
    GameScreen(
        viewModel = GameViewModel()
    )
}