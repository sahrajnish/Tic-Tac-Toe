package com.example.tictactoe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.cast.tv.cac.UserAction

class GameViewModel: ViewModel() {
    var state by mutableStateOf(GameState())

    val boardItems: MutableMap<Int, BoardCellValue> = mutableMapOf(
        1 to BoardCellValue.NONE,
        2 to BoardCellValue.NONE,
        3 to BoardCellValue.NONE,
        4 to BoardCellValue.NONE,
        5 to BoardCellValue.NONE,
        6 to BoardCellValue.NONE,
        7 to BoardCellValue.NONE,
        8 to BoardCellValue.NONE,
        9 to BoardCellValue.NONE
    )

    fun onAction(action: UserActions) {
        when(action) {
            is UserActions.BoardTapped -> {
                addValueToBoard(action.cellNo)
            }
            UserActions.PlayAgainButtonClicked -> {
                gameReset()
            }

            UserActions.NewGameButtonClicked -> {
                newGame()
            }
        }
    }

    private fun gameReset() {
        boardItems.forEach { (i, _) ->
            boardItems[i] = BoardCellValue.NONE
        }
        state = state.copy(
            hintText = if (state.prevTurn == BoardCellValue.CIRCLE) {
                "Player 'X' turn"
            } else "Player 'O' turn",
            currentTurn = if(state.prevTurn == BoardCellValue.CIRCLE) {
                BoardCellValue.CROSS
            } else BoardCellValue.CIRCLE,
            victoryTYpe = VictoryTYpe.NONE,
            hasWon = false
        )
    }

    private fun newGame() {
        boardItems.forEach { (i, _) ->
            boardItems[i] = BoardCellValue.NONE
        }
        state = state.copy(
            playerCircleCount = 0,
            playerCrossCount = 0,
            drawCount = 0,
            hintText = "Player 'O' turn",
            prevTurn = BoardCellValue.NONE,
            currentTurn = BoardCellValue.CIRCLE,
            victoryTYpe = VictoryTYpe.NONE,
            hasWon = false
        )
    }

    private fun addValueToBoard(cellNo: Int) {
        if (boardItems[cellNo] != BoardCellValue.NONE) {
            return
        }
        if(state.currentTurn == BoardCellValue.CIRCLE) {
            boardItems[cellNo] = BoardCellValue.CIRCLE
            if(checkForVictory(BoardCellValue.CIRCLE)) {
                state = state.copy(
                    prevTurn = BoardCellValue.CROSS,
                    hintText = "Player 'O' Won",
                    playerCircleCount = state.playerCircleCount + 1,
                    currentTurn = BoardCellValue.NONE,
                    hasWon = true
                )
            } else if(hasBoardFull()) {
                state = state.copy(
                    prevTurn = BoardCellValue.CIRCLE,
                    hintText = "Game Draw",
                    drawCount = state.drawCount + 1
                )
            } else {
                state = state.copy(
                    hintText = "Player 'X' turn",
                    currentTurn = BoardCellValue.CROSS
                )
            }
        } else if (state.currentTurn == BoardCellValue.CROSS) {
            boardItems[cellNo] = BoardCellValue.CROSS
            if(checkForVictory(BoardCellValue.CROSS)) {
                state = state.copy(
                    prevTurn = BoardCellValue.CIRCLE,
                    hintText = "Player 'X' Won",
                    playerCrossCount = state.playerCrossCount + 1,
                    currentTurn = BoardCellValue.NONE,
                    hasWon = true
                )
            } else if(hasBoardFull()) {
                state = state.copy(
                    prevTurn = BoardCellValue.CROSS,
                    hintText = "Game Draw",
                    drawCount = state.drawCount + 1
                )
            } else {
                state = state.copy(
                    hintText = "Player 'O' turn",
                    currentTurn = BoardCellValue.CIRCLE
                )
            }
        }
    }

    private fun checkForVictory(BoardCellValue: BoardCellValue): Boolean {
        when {
            boardItems[1] == BoardCellValue && boardItems[2] == BoardCellValue && boardItems[3] == BoardCellValue -> {
                state = state.copy(victoryTYpe = VictoryTYpe.HORIZONTAL1)
                return true
            }

            boardItems[4] == BoardCellValue && boardItems[5] == BoardCellValue && boardItems[6] == BoardCellValue -> {
                state = state.copy(victoryTYpe = VictoryTYpe.HORIZONTAL2)
                return true
            }

            boardItems[7] == BoardCellValue && boardItems[8] == BoardCellValue && boardItems[9] == BoardCellValue -> {
                state = state.copy(victoryTYpe = VictoryTYpe.HORIZONTAL3)
                return true
            }

            boardItems[1] == BoardCellValue && boardItems[4] == BoardCellValue && boardItems[7] == BoardCellValue -> {
                state = state.copy(victoryTYpe = VictoryTYpe.VERTICAL1)
                return true
            }

            boardItems[2] == BoardCellValue && boardItems[5] == BoardCellValue && boardItems[8] == BoardCellValue -> {
                state = state.copy(victoryTYpe = VictoryTYpe.VERTICAL2)
                return true
            }

            boardItems[3] == BoardCellValue && boardItems[6] == BoardCellValue && boardItems[9] == BoardCellValue -> {
                state = state.copy(victoryTYpe = VictoryTYpe.VERTICAL3)
                return true
            }

            boardItems[1] == BoardCellValue && boardItems[5] == BoardCellValue && boardItems[9] == BoardCellValue -> {
                state = state.copy(victoryTYpe = VictoryTYpe.DIAGONAL1)
                return true
            }

            boardItems[3] == BoardCellValue && boardItems[5] == BoardCellValue && boardItems[7] == BoardCellValue -> {
                state = state.copy(victoryTYpe = VictoryTYpe.DIAGONAL2)
                return true
            }
            else -> return false
        }
    }

    private fun hasBoardFull(): Boolean {
        if(boardItems.containsValue(BoardCellValue.NONE)) return false
        return true
    }
}