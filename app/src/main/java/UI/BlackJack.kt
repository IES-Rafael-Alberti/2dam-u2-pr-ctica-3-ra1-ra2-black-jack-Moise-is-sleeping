package UI

import Data.Card
import Data.Player
import Data.PlayingCards
import Data.Routes
import Data.Suits
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.calculator.blackjackmoise.R


/**
 * Function the creates the main menu
 * @param navController the navController
 * @param viewModel the object view-model
 *
 * */
@Composable
fun MainMenu(navController: NavController, viewModel: ViewModel){
    Box(
        modifier = Modifier.paint(
            painter = painterResource(id = R.drawable.darkgreen_background),
            contentScale = ContentScale.FillWidth
        )){
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),horizontalArrangement = Arrangement.End){
                Image(painter = painterResource(id = R.drawable.settings),
                    contentDescription = "logo",
                    modifier = Modifier.width(90.dp))
            }
            Row (
                modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center){
                Image(painter = painterResource(id = R.drawable.blacktitle),
                    contentDescription = "logo",
                    modifier = Modifier.width(700.dp))
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Image(painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    modifier = Modifier.width(250.dp))
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Button(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(60.dp)
                        .width(160.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(Color.Black),onClick = {
                        navController.navigate(Routes.MultiplayerScreen.route)
                        viewModel.startGame(false)
                        Log.d("_Player",viewModel.currentPLayer.value!!.playerNumber)
                    }) {
                    Text(text = "Multiplayer")
                }
                Button(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(60.dp)
                        .width(160.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(Color.Black),onClick = {
                        navController.navigate(Routes.SinglePlayerScreen.route)
                        viewModel.startGame(true)
                    }) {
                    Text(text = "SinglePlayer")
                }
            }
        }
    }

}

/**
 * Function that creates the screen for the singlePlayer mode
 * @param navController the navController
 * @param viewModel the object viewModel
 */
@Composable
fun SinglePlayerScreen(navController: NavController, viewModel: ViewModel){
    val gameOver : Boolean by viewModel.gameOver.observeAsState(initial = false)
    Box(
        modifier = Modifier.paint(
            painter = painterResource(id = R.drawable.darkgreen_background),
            contentScale = ContentScale.FillWidth
        )
    ) {
        if (gameOver){
            GameOverScreen(navController, viewModel.checkWinner(), viewModel)
        }else{
            InGameScreen(
                viewModel,
                gameOver = {
                    viewModel.gameOver(it)
                    Log.d("gameOver",it.toString())
                }
            )
        }
    }

}


/**
 * Function that creates the screen for the multiplayer mode
 * @param navController the navController
 * @param viewModel the object viewModel
 */
@Composable
fun MultiplayerScreen(navController: NavController, viewModel: ViewModel) {
    val gameOver : Boolean by viewModel.gameOver.observeAsState(initial = false)
    Box(
        modifier = Modifier.paint(
            painter = painterResource(id = R.drawable.darkgreen_background),
            contentScale = ContentScale.FillWidth
        )
    ) {
        if (gameOver){
            GameOverScreen(navController, viewModel.checkWinner(), viewModel)
        }else{
            InGameScreen(
                viewModel,
                gameOver = {
                    viewModel.gameOver(it)
                }
            )
        }
    }


}

/**
 * Function the creates the screen in game, regardless of the mode
 * @param viewModel the object viewModel
 * @param gameOver lambda function that checks if the game is over
 */
@Composable
fun InGameScreen(viewModel: ViewModel,gameOver:(Boolean)->Unit){
    var points by rememberSaveable { mutableIntStateOf(viewModel.currentPLayer.value!!.points) }
    var playerNumber by rememberSaveable { mutableStateOf(viewModel.currentPLayer.value!!.playerNumber) }
    val displayCard: Boolean by viewModel.displayCard.observeAsState(initial = false)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        ) {
            Text(
                text = playerNumber,
                fontSize = 26.sp,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center,
                color = Color(color = viewModel.color())
            )

            Text(
                text = "Points : $points",
                fontSize = 26.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                textAlign = TextAlign.End,
                color = Color.White
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ImageCreator(Card(PlayingCards.ace, Suits.spades, 0, 0, "backside"), 155, 245, 0, 0)
        }
        if (displayCard){
            RecivedCard(
                card = viewModel.lastCard(),
                width = 500,
                height = 500,
                offsetX = 0,
                offsetY =0 ,
                viewModel,
                playerPoints = { points = it },
                playerNumber = { playerNumber = it },)
        }
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
        ){
            DisplayCards(viewModel.playerCards())
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            Buttons(
                viewModel,
                gameOver = {gameOver(it)},
                playerPoints = { points = it },
                playerNumber = { playerNumber = it },
            )
        }
    }
}


/**
 * Function that displays a dialog, whenever a card is hit
 * @param card the card that has been received
 * @param width the width
 * @param height the height
 * @param offsetX the x offset
 * @param offsetY the y offset
 * @param viewModel the object viewModel
 * @param playerPoints lambda function that updates the player points
 * @param playerNumber lambda function that updates the number for the current player
 */
@Composable
fun RecivedCard(card : Card, width :Int , height : Int , offsetX :Int , offsetY :Int ,viewModel: ViewModel,playerPoints:(Int)->Unit,playerNumber:(String)->Unit){
    val ai: Boolean by viewModel.ai.observeAsState(initial = false)
    val toast: Boolean by viewModel.toast.observeAsState(initial = false)
    Dialog(onDismissRequest = {  }) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            ImageCreator(card,width,height,offsetX,offsetY)
            Button(
                modifier = Modifier
                    .padding(10.dp)
                    .height(60.dp)
                    .width(160.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(Color.Black),
                onClick = {

                    if (!ai){
                        viewModel.changePLayer()
                    }else{
                        viewModel.displayToast()
                    }
                    playerPoints(viewModel.playerInfo()[1]as Int)
                    playerNumber(viewModel.playerInfo()[0].toString())
                }) {
                Text(text = "Close")
                if (toast){
                    Toast.makeText(LocalContext.current,viewModel.aiTurn(), Toast.LENGTH_LONG).show()
                    viewModel.dontDisplayToast()
                }
            }
        }
    }

}

/**
 * Function that displays the buttons on screen when in-game
 * @param viewModel the object view-model
 * @param gameOver lambda function that checks if the game is over
 * @param playerPoints lambda function that updates the player points
 * @param playerNumber lambda function that updates the number for the current player
 */
@Composable
fun Buttons(viewModel: ViewModel,gameOver:(Boolean)->Unit,playerPoints:(Int)->Unit,playerNumber:(String)->Unit) {
    val ai: Boolean by viewModel.ai.observeAsState(initial = false)
    val disable: Boolean by viewModel.displayCard.observeAsState(initial = false)
    Row (
        modifier = Modifier
            .padding(top = 80.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Button(
            modifier = Modifier
                .padding(10.dp)
                .height(60.dp)
                .width(160.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(Color.Black),
            onClick = {
                viewModel.getCard()
                playerPoints(viewModel.playerInfo()[1] as Int)
            }, enabled = !disable) {
            Text(text = "Hit")
        }


        Button(
            modifier = Modifier
                .padding(10.dp)
                .height(60.dp)
                .width(160.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(Color.Black),
            onClick = {
                viewModel.playerHasStood()
                if (!ai){
                    viewModel.changePLayer()
                }
                playerPoints(viewModel.playerInfo()[1] as Int)
                playerNumber(viewModel.playerInfo()[0].toString())
                viewModel.stand()
            },enabled = !disable) {
            Text(text = "Stand")
        }
        gameOver(viewModel.checkGameOver())
    }
}

/**
 * Function that displays the screen when the game is over
 * @param viewModel the object view-model
 * @param navController the object navController
 * @param gameOverText the text to be displayed when the game is over
 */
@Composable
fun GameOverScreen(navController: NavController, gameOverText:String, viewModel: ViewModel){
    Box() {
        Column(modifier = Modifier.fillMaxSize()) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(text = gameOverText,
                    fontSize = 26.sp,
                    modifier = Modifier.padding(5.dp),
                    color = Color.White)
            }
            GameOverCards(viewModel.player1.value!!)
            GameOverCards(viewModel.player2.value!!)
            GameOverButtons(navController,viewModel)
        }
    }
}


/**
 * Function that displays all the cards the player got at the end of the game
 * @param player the player whose cards are gonna be displayed
 */
@Composable
fun GameOverCards(player: Player){
    Row {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${player.playerNumber} cards",
                    fontSize = 15.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp, top = 20.dp),
                    color = Color.White,
                    textAlign = TextAlign.Start
                )

                Text(
                    text = "Points: ${player.points}",
                    fontSize = 15.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp, top = 20.dp),
                    color = Color.White,
                    textAlign = TextAlign.End
                )
            }
            LazyRow(){
                items(player.cardsInHand) {
                        item -> ImageCreator(card = item, width = 140, height = 180, offsetX = 0, offsetY = 0)
                }
            }

        }
    }
}

/**
 * Function that displays the buttons when the game is over
 * @param viewModel the object view-model
 * @param navController the object navController
 */

@Composable
fun GameOverButtons(navController: NavController, viewModel: ViewModel){
    Row(modifier = Modifier
        .fillMaxSize()
        .padding(top = 40.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center) {
        Button(modifier = Modifier
            .padding(10.dp)
            .height(60.dp)
            .width(160.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(Color.Black),onClick = {
                viewModel.startGame(viewModel.ai.value!!)
                navController.navigate(Routes.MultiplayerScreen.route)

            }) {
            Text(text = "Play Again")

        }
        Button(modifier = Modifier
            .padding(10.dp)
            .height(60.dp)
            .width(160.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(Color.Black),onClick = {
                navController.navigate(Routes.MainMenu.route)

            } ) {
            Text(text = "Exit")
        }
    }
}

/**
 * Function that an image based on the card received
 * @param card the card that has been received
 * @param width the width
 * @param height the height
 * @param offsetX the x offset
 * @param offsetY the y offset
 */
@Composable
fun ImageCreator(card: Card, width:Int, height:Int, offsetX:Int, offsetY:Int){
    Image(
        //uses the function getCardId to extract the id of the card
        painter = painterResource(id = getCardId(card)),
        contentDescription = "image",
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
            .offset(x = offsetX.dp, y = offsetY.dp))
}

/**
 * Function that extracts the id drawable from a card
 * @param card the card whose id is needed
 * @return Returns the id drawable of the card
 */
@SuppressLint("DiscouragedApi")
@Composable
fun getCardId(card:Card): Int {
    val context = LocalContext.current
    val id = context.resources.getIdentifier(card.idDrawable, "drawable", context.packageName)
    return id
}

/**
 * Function that creates images for all the cards in a list
 * @param cards the list of cards whose image is to be displayed
 */
@Composable
fun DisplayCards(cards:MutableList<Card>){
    var counter = 5
    for (card in cards ){
        ImageCreator(card, 150 , 250 , counter, 0 )
        counter+=40
    }
}