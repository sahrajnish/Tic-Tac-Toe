package com.example.tictactoe

sealed class UserActions {
    object PlayAgainButtonClicked : UserActions()
    object NewGameButtonClicked: UserActions()
    data class BoardTapped(val cellNo: Int) : UserActions()
}