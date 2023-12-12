package com.calculator.blackjackmoise

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calculator.blackjackmoise.model.Routes
import com.moise.cartas.Deck
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val player1 = Player(1)
            val player2 = Player(2)

            NavHost(navController = navController, startDestination = Routes.MainMenu.route){
                composable(Routes.MainMenu.route){MainMenu(navController)}
                composable(Routes.MultiplayerScreen.route){ MultiplayerScreen(navController,player1,player2,false) }

            }
        }
    }
}

@Composable
fun MainMenu(navController: NavController){
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
                        Deck.resetDeck()
                        Deck.createDeck()
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
    }

}

@Composable
fun gameOver(navController: NavController,player1: Player, player2: Player, nextRound:()->Unit){
    var winner by rememberSaveable { mutableIntStateOf(0) }
    var draw by rememberSaveable { mutableStateOf(false) }
    if (player2.winOrLoose() ==1 || player1.winOrLoose() ==-1){
        player2.tokens += player1.tokens
        player1.tokens = 0
        winner = 2
    }else if (player2.winOrLoose() ==-1 || player1.winOrLoose() ==1){
        player1.tokens += player2.tokens
        player2.tokens = 0
        winner = 1
    }else if (player2.winOrLoose() ==-1 && player1.winOrLoose() ==-1){
        draw = true
    }else if (player2.winOrLoose() ==1 && player1.winOrLoose() ==1){
        draw = true
    }
    else{
        if (player1.closesToWinning() < player2.closesToWinning()){
            player1.tokens += player2.tokens
            player2.tokens = 0
            winner =1
        }else{
            player2.tokens += player1.tokens
            player1.tokens = 0
            winner = 2
        }
    }
    Box() {
        Column(modifier = Modifier.fillMaxSize()) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                if (draw){
                    Text(text = "Draw !!",
                        fontSize = 26.sp,
                        modifier = Modifier.padding(5.dp),
                        color = Color.White)
                }else{
                    Text(text = "Player $winner wins",
                        fontSize = 26.sp,
                        modifier = Modifier.padding(5.dp),
                        color = Color.White)
                }
            }
            Row {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Player 1 cards",
                            fontSize = 15.sp,
                            modifier = Modifier
                                .weight(1f)
                                .padding(5.dp, top = 20.dp),
                            color = Color.White,
                            textAlign = TextAlign.Start
                        )

                        Text(
                            text = "Points: ${player1.points}",
                            fontSize = 15.sp,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 10.dp, top = 20.dp),
                            color = Color.White,
                            textAlign = TextAlign.End
                        )
                    }
                    LazyRow(){
                        items(player1.cardsInHand) {
                                item -> ImageCreator(card = item, width = 140, height = 180, offsetX = 0, offsetY = 0)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Player 2 cards", fontSize = 15.sp, modifier = Modifier
                                .weight(1f)
                                .padding(5.dp, top = 20.dp),
                            color = Color.White,
                            textAlign = TextAlign.Start
                        )

                        Text(
                            text = "Points: ${player2.points}", fontSize = 15.sp, modifier = Modifier
                                .weight(1f)
                                .padding(end = 10.dp, top = 20.dp),
                            color = Color.White,
                            textAlign = TextAlign.End
                        )
                    }
                    LazyRow(){
                        items(player2.cardsInHand) {
                                item -> ImageCreator(card = item, width = 140, height = 180, offsetX = 0, offsetY = 0)
                        }
                        Log.d("player2",player2.cardsInHand.size.toString())
                    }
                }
            }
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
                        nextRound()
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
                        player1.fullReset()
                        player2.fullReset()
                }) {
                    Text(text = "Exit")

                }
            }
        }

    }
}

@Composable
fun MultiplayerScreen(navController: NavController, player1: Player, player2: Player,hasBet: Boolean){
    var playAgain by rememberSaveable { mutableStateOf(false) }
    var tokens by rememberSaveable { mutableIntStateOf(0) }
    var points by rememberSaveable { mutableIntStateOf(0) }
    val currentPlayer = MutableLiveData<Player>()
    var whoseTurn by rememberSaveable { mutableStateOf(true) }
    player1.startingHand()
    player2.startingHand()
    if (whoseTurn){
        currentPlayer.value = player1
    }else{
        currentPlayer.value= player2
    }
    var hasBet by rememberSaveable { mutableStateOf(hasBet) }
    var gameOver by rememberSaveable { mutableStateOf(false) }
    var color by rememberSaveable { mutableStateOf(0xff0000ff) }

    Log.d("GameOver",Deck.deck.cardList.size.toString())

    Box(
        modifier = Modifier.paint(
        painter = painterResource(id = R.drawable.darkgreen_background),
            contentScale = ContentScale.FillWidth
    )){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)){
                if (currentPlayer.value!!.playerNumber==1){
                    color = 0xFF0030DB
                }else{
                    color = 0xF7E40000
                }
                if (currentPlayer.value!!.hasBet && !currentPlayer.value!!.hasStood && !gameOver){
                    Text(text = "Tokens : ${currentPlayer.value!!.tokens}",
                        fontSize = 26.sp,
                        modifier = Modifier.padding(5.dp),
                        color = Color.White)
                    Text(text = "Player ${currentPlayer.value!!.playerNumber}",
                        fontSize = 26.sp,
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center,
                        color = Color(color = color)
                    )
                    Text(text = "Points : $points",
                        fontSize = 26.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        textAlign = TextAlign.End,
                        color = Color.White)
                }
                else if(!currentPlayer.value!!.hasBet && !currentPlayer.value!!.hasStood && !gameOver){
                    Text(text = "Tokens : $tokens",
                        fontSize = 26.sp,
                        modifier = Modifier.padding(5.dp),
                        color = Color.White)
                    Text(text = "Player ${currentPlayer.value!!.playerNumber}",
                        fontSize = 26.sp,
                        modifier = Modifier.padding(5.dp),
                        textAlign = TextAlign.Center,
                        color = Color(color=color)
                    )
                }
            }

            Log.d("hasbet",hasBet.toString())

            if (currentPlayer.value!!.winOrLoose() == -1 || currentPlayer.value!!.winOrLoose() == 1){
                gameOver = true
                gameOver(navController,player1, player2 , nextRound = {
                    player1.semiReset()
                    player2.semiReset()
                   playAgain = true

                })
            }
            if (playAgain){
                MultiplayerScreen(navController , player1 , player2,true  )
            }


            else if (!currentPlayer.value!!.hasBet && !gameOver) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center){
                    ImageCreator(Card(PlayingCards.ace,Suits.spades,0,0,"backside"),155,245,0,0)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    SelectTokens(currentPlayer.value!!, hasBet,
                        updateHasBet = { currentPlayer.value!!.hasBet = true
                            if (currentPlayer.value!!.playerNumber == 2){
                                hasBet = true
                                whoseTurn = !whoseTurn

                            }else{
                                tokens = 0
                                whoseTurn = !whoseTurn
                            }

                        },
                        updateTokens = { tokens += it
                            currentPlayer.value!!.addTotalTokens( it)})
                }

            }

            else if(!currentPlayer.value!!.hasStood && !gameOver) {
                Multiplayer(currentPlayer.value!!,
                    updatePoints = {
                        points = currentPlayer.value!!.checkPoints()

                    },
                    stand = {
                        currentPlayer.value!!.hasStood = true
                        whoseTurn = !whoseTurn
                    }, changeTurn = {
                        whoseTurn = !whoseTurn
                    })

                tokens= 0
            }
            else{
                gameOver(navController,player1, player2, nextRound = {
                    player1.semiReset()
                    player2.semiReset()})
            }
        }
    }





}





@Composable
        /**
         * Function that generates the image of the card and the 2 buttons
         */
fun Multiplayer(player:Player,updatePoints: (Int) -> Unit,stand: () -> Unit,changeTurn: () -> Unit){
    //variable that stores the card to be displayed
    val dealersCards by remember {  mutableStateOf(mutableListOf(getDealersCards())) }
    var playersCards by remember {  mutableStateOf(mutableListOf<Card>()) }
    val points  by rememberSaveable {  mutableStateOf(0) }
    Column (
        modifier = Modifier
            .fillMaxSize(),
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
            }, changeTurn = {
                changeTurn()
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
fun ShowCards(playersCards:MutableList<Card>, updatePlayersCards: (MutableList<Card>) -> Unit,points:Int,updatePoints:(Int)->Unit,stand:()->Unit,changeTurn:()->Unit) {
    var delay by rememberSaveable {  mutableStateOf( false)}
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
                delay = true

            }) {
            Text(text = "Hit")
        }
        if (delay){
            Delay (changeTurn = {
                changeTurn()
                delay = false
                Log.d("changeClicked",delay.toString())
            })

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
fun Delay(changeTurn: () -> Unit){
    LaunchedEffect(Unit) {

        launch {
            delay(600)
            changeTurn()
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