package Data

sealed class Routes(val route : String) {
    object SinglePlayerScreen : Routes("SinglePlayerScreen")
    object MultiplayerScreen : Routes("MultiplayerScreen")
    object MainMenu : Routes("MainMenu")


}
