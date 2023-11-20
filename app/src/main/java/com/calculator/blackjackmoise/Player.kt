package com.calculator.blackjackmoise

import com.moise.cartas.Deck

class Player {
    var cardsInHand = mutableListOf<Card>()
    var points = 0

    fun startingHand(){
        cardsInHand.add(Deck.giveCard())
        cardsInHand.add(Deck.giveCard())
    }

    fun hit(): MutableList<Card> {
        if (cardsInHand.size == 0){
            startingHand()
        }else{
            cardsInHand.add(Deck.giveCard())
        }
        return cardsInHand
    }
    fun checkPoints(){
        for (card in cardsInHand){
            points+= card.maxPoint
        }
    }

}