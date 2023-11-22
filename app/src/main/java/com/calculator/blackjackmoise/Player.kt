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
        checkPoints()
        return cardsInHand
    }

    fun hit(): MutableList<Card> {
        cardsInHand.add(Deck.giveCard())

        return cardsInHand
    }

    fun winOrLose(): Int {
        if (points==21){
            return 1
        }
        else if(points>21){
            return -1
        }else{
            return 0
        }
    }
    fun checkPoints(): Int {
        val aces = mutableListOf<Card>()
        points = 0
        for (card in cardsInHand){
            if(card.name.toString() !="ace"){
                points+= card.maxPoint
            }
            else{
                aces.add(card)
            }
        }
        for (card in aces){
            if (points <= 10){
                points+=card.maxPoint
            }else{
                points+=card.minPoint
            }
        }
        return points
    }

}