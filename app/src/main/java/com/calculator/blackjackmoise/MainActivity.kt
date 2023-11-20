package com.calculator.blackjackmoise

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Routes.MainMenu.route){
                composable(Routes.MainMenu.route){MainMenu(navController)}
                composable(Routes.MultiplayerScreen.route){ MultiplayerScreen(navController) }
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
fun MultiplayerScreen(navController: NavController){
    val player1 = Player()
    Multiplayer(player1)
}




@Composable
        /**
         * Function that generates the image of the card and the 2 buttons
         */
fun Multiplayer(player:Player){
    //variable that stores the card to be displayed
    val dealersCards by remember {  mutableStateOf(mutableListOf(getDealersCards())) }
    var playersCards by remember {  mutableStateOf(mutableListOf<Card>()) }
    var a by remember {  mutableStateOf(  0)}
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

            if (a <30){
                ImageCreator(Card(PlayingCards.ace,Suits.spades,0,0,"backside"),155,245,0,0)
                ImageCreator(dealersCards[0][1],150,250,-70,0)
            }else{
                ImageCreator(dealersCards[0][0],155,245,0,0)
                ImageCreator(dealersCards[0][1],150,250,-70,0)
            }

        }
        Row(
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
                    playersCards = player.hit()
                    a+=1
                    //the selected card is the last card of the d
                    Log.d("CardList","In the button"+playersCards.toString())
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
                    //Clears the deck
                    Deck.deck.cardList.clear()
                    //And creates it again
                    Deck.createDeck()
                    //And sets the selected card to default

                }) {
                Text(text = "Stand")
            }
        }
    }
}
@Composable
fun PlayersCards(cards:MutableList<Card>){
    var counter = 5
    Log.d("CardList","In the function"+cards.toString())
    for (card in cards ){
        ImageCreator(card, 150 , 250 , counter, 0 )
        counter+=40
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