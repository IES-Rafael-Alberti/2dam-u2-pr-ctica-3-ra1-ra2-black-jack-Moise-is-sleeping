package com.moise.cartas

import android.util.Log
import com.calculator.blackjackmoise.Card
import com.calculator.blackjackmoise.PlayingCards
import com.calculator.blackjackmoise.Suits


/**
 * @property cardList A a list of all the cards in the deck
 */
class Deck {
    var cardList = arrayListOf<Card>()

    /**
     * @property deck The object Deck
     */
    companion object{
        val deck = Deck()

        /**
         * Function the creates an Array with all the cards on the deck
         * @return An array list of the cards
         */
        fun createDeck(): ArrayList<Card> {
            //val that contains all the values of the enum class playing cards
            val numbers = PlayingCards.values()
            //val that contains all the values of the enum class suits
            val suites = Suits.values()
            //for loop the iterates over the 4 suits
            for (suite in suites ){
                //counter that gives the number of points to each card
                var counter = 2
                //counter that lets me set the last 3 cards max and min to 10
                var secondaryCounter = 0
                //for loop that iterates over all the numbers
                for (number in numbers){
                    //if the secondary counter is less the for but the counter is 10
                    if (counter == 10 && secondaryCounter <4){
                        //it sets the max and min value of the card
                        val card = Card(number,suite,counter,counter,number.toString()+"_of_$suite")
                        // adds it to the card list
                        deck.cardList.add(card)
                        // and keep the value of the counter to 10
                        counter = 10
                        //and the secondary counter goes up
                        secondaryCounter+=1
                        //if the secondary counter is 4
                    }else if(counter == 10 && secondaryCounter ==4){
                        //the counter is set to 11
                        counter = 11
                        //and the max point is 11 and the min point is 1
                        val card = Card(number,suite,counter-10,counter,number.toString()+"_of_$suite")
                        //and the card is added to the list
                        deck.cardList.add(card)
                    }else{
                        //all other cards have the max and min point set to their corresponding numbers
                        val card = Card(number,suite,counter,counter,number.toString()+"_of_$suite")
                        //and the card is added to the list
                        deck.cardList.add(card)
                        counter+=1
                    }
                }
            }
            return deck.cardList
        }

        /**
         * Shuffles the cardlist
         */
        fun shuffle() {
            //shuffles the card
            deck.cardList.shuffle()
        }

        /**
         * Function takes the last card of the deck and returns it
         */
        fun giveCard(): Card {
            shuffle()
            //if the cardlist is not empty, it sends the the last card in the deck then deletes it
            if(deck.cardList.size != 0 ){
                val card = deck.cardList.last()
                deck.cardList.removeAt(deck.cardList.size-1)
                return card
            }
            //otherwise it send the back of the card
            else{
                return Card(PlayingCards.ace,Suits.spades,0,0,"backside")
            }

        }
        fun resetDeck(){
            deck.cardList.clear()
        }


    }
}