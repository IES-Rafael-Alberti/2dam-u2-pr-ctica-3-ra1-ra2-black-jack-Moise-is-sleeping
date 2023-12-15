package UI

import Data.Card
import Data.Deck
import Data.Player
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Class that manages all the ingame logic
 *
 * @property _player1 livedata of the object player1
 * @property player1 observes the state of _player1
 * @property _player2 livedata of the object player2
 * @property player2 observes the state of _player2
 * @property currentPLayer livedata of the the current player, that can be either player 1 or 2
 * @property _hasStood variable that checks if one of the players has stood
 * @property _switchPlayer vartiable that shows when to switch player
 * @property _standCounter variable that checks how many times the players have stood
 * @property _displayCard variable the shows when to show the pop up of the card that has been hit
 * @property _gameOver variable that shows when the game has been finished
 * @property gameOver observes the state of _gameOver
 * @property _ai variable that shows if the ai is playing
 * @property gameOver observes the state of _ai
 * @property _aiHasStood variable the shows if the ai has stood
 * @property _toast variable that shows when to display the toast
 * @property toast observes the state of _toast
 */
class ViewModel (application: Application) : AndroidViewModel(application) {


    private val _player1 = MutableLiveData<Player>()
    var player1 : LiveData<Player> = _player1
    private val _player2 = MutableLiveData<Player>()
    var player2 : LiveData<Player> = _player2
    //esta puesto asi porque me dijiste que no lo cambiara
    var currentPLayer = MutableLiveData<Player>()
    private var _hasStood = MutableLiveData<Boolean>()
    private var _switchPlayer : Boolean = true
    private var _standCounter : Int = 0
    private var _displayCard = MutableLiveData<Boolean>()
    var displayCard : LiveData<Boolean> = _displayCard
    private var _gameOver = MutableLiveData<Boolean>()
    var gameOver : LiveData<Boolean> = _gameOver
    private var _ai = MutableLiveData<Boolean>()
    var ai : LiveData<Boolean> = _ai
    private var _aiHasStood = MutableLiveData<Boolean>()
    private var _toast = MutableLiveData<Boolean>()
    var toast : MutableLiveData<Boolean> = _toast

    /**
     * Function turns the variable _toast true
     */
    fun displayToast(){
        _toast.value = true
    }
    /**
     * Function turns the variable _toast false
     */
    fun dontDisplayToast(){
        _toast.value = false
    }

    /**
     * Function creates the objects players and assigns them to the variables _player1 and _player2,
     * as well as giving each player its starting hand
     */
    private fun createPlayers(){
        _player1.value = Player("Player 1")
        //if the ai is being used then the name of player 2 is ai
        if (_ai.value!!){
            _player2.value = Player("AI")
        }else{
            _player2.value = Player("Player 2")
        }
        startingHand(_player1.value!!)
        startingHand(_player2.value!!)
        switch()
    }

    /**
     * This functin gives the player the starting hand
     * @param player the player thats gonna get the starting hand
     */
    private fun startingHand(player: Player){
        player.startingHand()
    }

    /**
     * Reset the deck and creates a new one
     */
    private fun newDeck(){
        Deck.resetDeck()
        Deck.createDeck()
    }

    /**
     * Resets all the variables, creates a new deck and creates new players
     * @param ai variable that tells the function if the ai is to be used or not
     */
    fun startGame(ai:Boolean){
        _aiHasStood.value = false
        _hasStood.value = false
        _switchPlayer = true
        _displayCard.value = false
        _ai.value = ai
        _standCounter = 0
        _gameOver.value = false
        newDeck()
        createPlayers()
    }

    /**
     * function that checks If the player has stood, then makes the ai keep playing its turn
     */
    fun playerHasStood(){
        //if the ai is playing and hasnt stood
        if (_ai.value!! && !_aiHasStood.value!!){
            // it keeps playing until it has stood
            while (_standCounter<2){
                if(aiTurn() == "Ai has stood !!"){
                    //if the ai has stood the loop is broken
                    break
                }
            }
        }

    }

    /**
     * Function that tell the current player what value to take
     */
    fun switch(){
        //if its true, then current player is player 1
        if (_switchPlayer){
            currentPLayer = _player1

        }
        //Otherwise its player 2
        else{
            currentPLayer = _player2
        }
    }

    /**
     * Function that changes the value of the current player
     */
    fun changePLayer(){
        // if on of the players has stood then the turn doesnt change
        if (!_hasStood.value!!){
            _switchPlayer =! _switchPlayer
        }
        _displayCard.value = false
        switch()
    }


    /**
     * Function that cointains the logic for the ai, in order for it to know what to do on each turn
     * @return Returns a string that says what the ai has done
     */
    fun aiTurn(): String {
        var text = ""
        //if tha ai is being used
        if (_ai.value!!){
            //it changes the value of the current player to player 2
            changePLayer()
            // if it has less the 17 points it hits
            if (currentPLayer.value!!.checkPoints() <=17){
                currentPLayer.value!!.hit()
                text = "Ai has hit !!"
            }
            //otherwise its stands
            else{
                _aiHasStood.value = true
                stand()
                text = "Ai has stood !!"
            }
            //Updates the number of points that the ai has
            currentPLayer.value!!.checkPoints()
            //Checks if the game is now over
            if (checkGameOver()){
                _gameOver.value = true
            }
            //Changes the value of the current player back to player 1
            changePLayer()
        }
        return text
    }

    /**
     * Function that returns the info of the current player
     * @return Returns a list with the number of the current player and the points
     */
    fun playerInfo(): MutableList<Any> {
        return mutableListOf(currentPLayer.value!!.playerNumber,currentPLayer.value!!.points)
    }

    /**
     * Function that returns the cards the current player has
     * @return Returns a list with the cards that the current player has
     */
    fun playerCards(): MutableList<Card> {
        return currentPLayer.value!!.cardsInHand
    }

    /**
     * Function that gives the current player a card, displays the card and updates the points
     */
    fun getCard(){
        _displayCard.value = true
        currentPLayer.value!!.hit()
        currentPLayer.value!!.checkPoints()

    }

    /**
     * Function the returns the last card the player received
     */
    fun lastCard(): Card {
        return currentPLayer.value!!.cardsInHand.last()
    }

    /**
     * Function the returns a color based on which player is on screen
     * @return Returns a color in the form of a Long
     */
    fun color(): Long {
        val color_: Long
        if (_switchPlayer){
            color_ = 0xFF0030DB
        }else{
            color_ = 0xFFDB0007
        }
        return color_
    }

    /**
     * Function the checks if the game is over
     * @return Returns true if one of the players has won or lost
     */
    fun checkGameOver(): Boolean {
        return currentPLayer.value!!.winOrLoose() == 1 || currentPLayer.value!!.winOrLoose() == -1
    }

    /**
     * Function that checks which of the players has won
     * @return Returns a a string that says how the game went
     */
    fun checkWinner(): String {
        var winner = ""
        var draw = false
        //if player 2 has 21 while player 1 has less or if player 1 has more then 21 and player 2 has less
        if (_player2.value!!.winOrLoose() ==1 && _player1.value!!.winOrLoose() == 0 || _player1.value!!.winOrLoose() ==-1 && _player2.value!!.winOrLoose() == 0){
            //player 2 is the winner
            winner = _player2.value!!.playerNumber
        }
        //if player 1 has 21 while player 2 has less or if player 2 has more then 21 and player 1 has less
        else if (_player2.value!!.winOrLoose() ==-1 && _player1.value!!.winOrLoose() == 0 || _player1.value!!.winOrLoose() ==1 && _player2.value!!.winOrLoose() == 0){
            //player 1 is the winner
            winner = _player1.value!!.playerNumber
        }
        //if both the players have more then 21
        else if (_player2.value!!.winOrLoose() ==-1 && _player1.value!!.winOrLoose() ==-1){
            //its a draw
            draw = true
        }
        //if both the players have 21
        else if (_player2.value!!.winOrLoose() ==1 && _player1.value!!.winOrLoose() ==1){
            draw = true
        }
        else{
            //if both the players have less then 21, the one closest to 21 is the winner
            if (_player1.value!!.closesToWinning() < _player2.value!!.closesToWinning()){
                winner = _player1.value!!.playerNumber
            }else if (_player2.value!!.closesToWinning() < _player1.value!!.closesToWinning()){
                winner = _player2.value!!.playerNumber
            }else{
                draw = true
            }
        }
        // returns draw if its a draw otherwise, returns the number of the winner
        if (draw ){
            return "Draw !!"
        }else{
            return "$winner is the winner !!"
        }
    }

    /**
     * Function that updates the game over variable
     * @param gameOver a boolean that contains tha value to be assigned to _gameOver
     */
    fun gameOver(gameOver :Boolean){
        _gameOver.value = gameOver
    }


    /**
     * Function that checks if both the players have stood
     */
    fun stand(){
        //the ai isnt playing the variable _hasStood is updated
        if (!_ai.value!!){
            _hasStood.value = true
        }
        _standCounter += 1
        //if the counter reaches 2, the game is over
        if (_standCounter == 2){
            _gameOver.value = true
        }
    }


}