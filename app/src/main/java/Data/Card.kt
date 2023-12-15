package Data

/**
 * @property name The name of the card
 * @property suite The suite of the card
 * @property minPoint The minimum amount of points the card can have
 * @property maxPoint The maximum amount of points the card can have
 * @property idDrawable The drawble id in order to display the image
 */
class Card(var name : PlayingCards, var suite : Suits, var minPoint :Int, var maxPoint :Int, var idDrawable :String) {

}