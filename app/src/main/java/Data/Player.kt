package Data

import Data.Card
import Data.Deck

/**
 * @property cardsInHand List of Cards the contains all the cards the player has at a time
 * @property points Total points the cards the player has sum up too
 * @param playerNumber An identification to know who the current player is
 */
class Player(var playerNumber: String) {
    var cardsInHand = mutableListOf<Card>()
    var points = 0

    /**
     * Function that gives the player two cards to start off with
     * @return Returns a list with the 2 starting cards
     */
    fun startingHand(): MutableList<Card> {
        //if the list is empty, it gives the player 2 cards to start off with
        if (cardsInHand.isEmpty()){
            cardsInHand.add(Deck.giveCard())
            cardsInHand.add(Deck.giveCard())
        }
        checkPoints()
        return cardsInHand
    }

    /**
     * Function the gives the player a card
     * @return Returns the list with the new card
     */
    fun hit(): MutableList<Card> {
        cardsInHand.add(Deck.giveCard())
        return cardsInHand
    }


    /**
     * This function check to see if the player has one or lost
     * @return Returns an Int, 1 if the player has won, -1 if the player has lost and 0 if the player has less then 21 points
     */
    fun winOrLoose(): Int {
        return if (points==21){
            1
        } else if(points>21){
            -1
        }else{
            0
        }
    }

    /**
     * This function returns a number the shows how close the player was to getting 21
     * @return Returns the difference between the current points and 21
     */
    fun closesToWinning(): Int {
        return 21 - points
    }

    /**
     * This function checks too see how many points the player has, based on the cards on the list
     * @return Returns the total points
     */
    fun checkPoints(): Int {
        //List the stores all the aces that the player has
        val aces = mutableListOf<Card>()
        //Resets the value of the total points
        points = 0
        // iterates over all the cards
        for (card in cardsInHand){
            //if the cards is not an ace then the points are added normally
            if(card.name.toString() !="ace"){
                points+= card.maxPoint
            }
            //if the card is an ace its added to the list
            else{
                aces.add(card)
            }
        }
        //iterates over all the aces
        for (card in aces){
            // if the total points till this point are less or eqal to 10, the value of the ace is 11
            points += if (points <= 10){
                card.maxPoint
            }
            //otherwise the value is 1
            else{
                card.minPoint
            }
        }
        return points
    }
}