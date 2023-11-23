package com.calculator.blackjackmoise

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calculator.blackjackmoise.model.Routes
import com.moise.cartas.Deck



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Deck.createDeck()
        var choosePlayer = 0
        setContent {
            val navController = rememberNavController()
            val player1 by rememberSaveable { mutableStateOf( Player()) }
            val player2 by rememberSaveable { mutableStateOf( Player()) }

            NavHost(navController = navController, startDestination = Routes.MainMenu.route){
                composable(Routes.MainMenu.route){MainMenu(navController)}
                composable(Routes.MultiplayerScreen.route){ MultiplayerScreen(navController,player1,player2) }

            }
        }
    }
}

@Composable
fun MainMenu(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .padding(10.dp)
                .height(60.dp)
                .width(160.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(Color.Black),onClick = {
            navController.navigate(Routes.MultiplayerScreen.route)
        }) {
            Text(text = "Multiplayer")
        }
        Button(
            modifier = Modifier
                .padding(10.dp)
                .height(60.dp)
                .width(160.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(Color.Black),onClick = { /*TODO*/ }) {
            Text(text = "SinglePlayer")
        }
    }
}

@Composable
fun MultiplayerScreen(navController: NavController, player1: Player, player2: Player){
    var tokens by rememberSaveable { mutableStateOf(0) }
    var hasBet by rememberSaveable { mutableStateOf(false) }
    var points by rememberSaveable { mutableStateOf(0) }
    var currentPlayer  by remember { mutableStateOf(Player())}
    var whoseTurn by rememberSaveable { mutableStateOf(false) }

    if (whoseTurn){
        currentPlayer = player1
    }else{
        currentPlayer = player2
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)){
            if (hasBet){
                Text(text = "Tokens : $tokens",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(5.dp))
                Text(text = "Points : $points",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    textAlign = TextAlign.End)
            }
            else{
                Text(text = "Tokens : $tokens",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(5.dp))
            }
        }
        if (currentPlayer.winOrLoose() == -1){
            Loose()
        }
        if (!hasBet) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                ImageCreator(Card(PlayingCards.ace,Suits.spades,0,0,"backside"),155,245,0,0)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                SelectTokens(currentPlayer, hasBet,
                    updateHasBet = { hasBet = true },
                    updateTokens = { tokens += it })
            }
        } else {
            Multiplayer(currentPlayer,
                updatePoints = {
                    points = currentPlayer.checkPoints()
                },
                stand = {
                    whoseTurn= true
                })
        }
    }
}
@Preview
@Composable
fun Loose(){
    Dialog(
        onDismissRequest = { /*TODO*/ }
    ) {
        Image(
            //uses the function getCardId to extract the id of the card
            painter = painterResource(R.drawable.youloose2),
            contentDescription = "image",
            modifier = Modifier
                .width(900.dp)
                .height(800.dp))

    }
}

@Composable
        /**
         * Function that generates the image of the card and the 2 buttons
         */
fun Multiplayer(player:Player,updatePoints: (Int) -> Unit,stand: () -> Unit){
    //variable that stores the card to be displayed
    val dealersCards by remember {  mutableStateOf(mutableListOf(getDealersCards())) }
    var playersCards by remember {  mutableStateOf(mutableListOf<Card>()) }
    var points  by rememberSaveable {  mutableStateOf(0) }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            ImageCreator(Card(PlayingCards.ace,Suits.spades,0,0,"backside"),155,245,0,0)
            ImageCreator(dealersCards[0][1],150,250,-70,0)
            playersCards = player.startingHand()
            updatePoints(points)
        }
        ShowCards(playersCards,
            updatePlayersCards = {
                playersCards = player.hit()
                },
            points,
            updatePoints = {
                updatePoints(points)
            }, stand = {
                stand()
            }

                    )
    }
}

@Composable
fun SelectTokens(player: Player, hasBet:Boolean, updateHasBet:(Boolean)->Unit,updateTokens :(Int)->Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            DisplayTokens(player, updateTokenPoints = {
                updateTokens( it)
            })
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){
            Button(
                modifier = Modifier
                    .padding(10.dp)
                    .height(60.dp)
                    .width(160.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(Color.Black),
                onClick = {
                    updateHasBet(hasBet)
                    Log.d("Hasbet","selecttokens $hasBet")
                }) {
                Text(text = "Deal")
            }
        }
    }
}

@Composable
fun ShowCards(playersCards:MutableList<Card>, updatePlayersCards: (MutableList<Card>) -> Unit,points:Int,updatePoints:(Int)->Unit,stand:()->Unit) {
    Row(modifier = Modifier
        .height(195.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Box{
            PlayersCards(playersCards)
        }
    }
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
                updatePlayersCards(playersCards)
                updatePoints(points)
                //the selected card is the last card of the d
            }) {
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
                stand()
            }) {
            Text(text = "Stand")
        }
    }
}

@Composable
fun PlayersCards(cards:MutableList<Card>){
    var counter = 5
    for (card in cards ){
        ImageCreator(card, 150 , 250 , counter, 0 )
        counter+=40
    }


}

@Composable
fun DisplayTokens(player: Player,updateTokenPoints:(Int)->Unit) {
    val tokens = listOf("pokerchip20","pokerchip50","pokerchip100")
        for (token in tokens) {
            Image(
                painter = painterResource(id = getokenId(name = token)),
                contentDescription = "Token",
                modifier = Modifier
                    .clickable {
                        player.addTotalTokens(
                            token
                                .substring(9)
                                .toInt()
                        )
                        updateTokenPoints(
                            token
                                .substring(9)
                                .toInt()
                        )

                        Log.d("tokens", "DisplayTokens ${player.tokens}")
                    }
                    .height(90.dp)
                    .width(90.dp)
                    .padding(10.dp))

        }
}



/**
 * Function that receives the card in order to extract the drawable id
 * @param card the card to be analyzed
 * @return the id of the card
 */
@Composable
fun getCardId(card:Card): Int {
    val context = LocalContext.current
    val id = context.resources.getIdentifier(card.idDrawable, "drawable", context.packageName)
    return id
}
@Composable
fun getokenId(name:String): Int {
    val context = LocalContext.current
    val id = context.resources.getIdentifier(name, "drawable", context.packageName)
    return id
}


/**
 * Function that creates the image
 * @param card Card to be displayed
 */
@Composable
fun ImageCreator(card: Card,width:Int,height:Int,offsetX:Int,offsetY:Int){
    Image(
        //uses the function getCardId to extract the id of the card
        painter = painterResource(id = getCardId(card)),
        contentDescription = "image",
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
            .offset(x = offsetX.dp, y = offsetY.dp))
}


fun getDealersCards(): MutableList<Card> {
    return mutableListOf<Card>(Deck.giveCard(),Deck.giveCard())
}