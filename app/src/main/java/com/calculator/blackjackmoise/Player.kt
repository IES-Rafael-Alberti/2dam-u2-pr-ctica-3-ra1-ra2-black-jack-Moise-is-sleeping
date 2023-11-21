package com.calculator.blackjackmoise

import com.moise.cartas.Deck

class Player {
    var cardsInHand = mutableListOf<Card>()
    var points = 0
    var tokens = 0

    fun addTotalTokens(amount : Int){
        tokens += amount
    }

    fun startingHand(): MutableList<Card> {
        if (cardsInHand.isEmpty()){
            cardsInHand.add(Deck.giveCard())
            cardsInHand.add(Deck.giveCard())
        }
        return cardsInHand
    }

    fun hit(): MutableList<Card> {
        cardsInHand.add(Deck.giveCard())

        return cardsInHand
    }
    fun checkPoints(){
        for (card in cardsInHand){
            points+= card.maxPoint
        }
    }

}