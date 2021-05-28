/**
 * This class is used to model a card in the game of Big Two.
 * It provides the attributes and methods necessary for a card in Big Two, 
 * and inherits the properties of a regular card (i.e. from the Card subclass)
 * 
 * @author Anchit Mishra
 */
public class BigTwoCard extends Card    {

    /**
     * A public constructor used to assign the specified suit and rank to 
     * a Big Two card.
     * 
     * @param       suit    An integer in the range from 0 to 3 (inclusive) specifying card suit
     *                      <p>
     *                      0 = Diamond, 1 = Club, 2 = Heart, 3 = Spade
     * @param       rank    An integer in the range from 0 to 12 (inclusive) specfiying card rank
     *                      <p>
     *                      0 = 'A', 1 = '2', 2 = '3' ... 9 = '10', 10 = 'J', 11 = 'Q', 12 = 'K'
     */                     
    public BigTwoCard(int suit, int rank)    {
        super(suit, rank);   
    }

    /**
     * A public method overriden from the parent class Card that compares the card object with another card 
     * passed as an argument and determines their relative ranking. Card rankings in Big Two are different
     * from regular card rankings, therefore we need to override the method in the parent class.
     * 
     * @param       card    An object of type Card that specifies the card to be compared against
     * 
     * @return              A negative integer, zero, or positive integer if the given card is 
     *                      less than, equal to, or greater than the argument card respectively
     */
    public int compareTo(Card card)    {
        // get rank and suit of the current card
        int currentSuit = this.getSuit();
        int currentRank = (this.getRank() + 11) % 13;
        // get rank and suit of the parameter card
        int cardRank = (card.getRank() + 11) % 13;
        int cardSuit = card.getSuit();

        // first, we check the rank ordering of the cards
        if (currentRank > cardRank)   {
            return 1;
        }
        else if (currentRank < cardRank)  {
            return -1;
        }
        // then, we check the suit ordering of the cards
        else if (currentSuit > cardSuit)  {
            return 1;
        }
        else if (currentSuit < cardSuit)  {
            return -1;
        }
        // if everything matches, then we have the same card
        else    {
            return 0;
        }
    }

}