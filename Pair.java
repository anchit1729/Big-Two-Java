/**
 * This class implements the Pair hand from the game of Big Two. It is used to model 
 * the attributes and behaviour of a Pair hand, and extend the base functionality
 * of the Hand class.
 * 
 * @author Anchit Mishra
 */
public class Pair extends Hand  {
    /**
     * A constructor method that takes two arguments - the details of the player
     * to whom the hand belongs, as well as the list of cards stored.
     * It initialises the player and cards instance variables.
     * 
     * @param       player  The CardGamePlayer to whom the hand belongs
     * @param       cards   The CardList of cards that are in the hand
     */
    public Pair(CardGamePlayer player, CardList cards) {
        super(player, cards);
    }

    /**
     * A definition of the abstract method from the Hand class to check whether the hand is a valid Pair or not.
     * A Pair in Big Two is defined as a hand consisting of two cards of the same rank.
     * 
     * @return      A boolean value of 'true' if the hand is a valid Pair and
     *                                 'false' if the hand is an invalid Pair
     */
    public boolean isValid()    {
        if (size() != 2)    {
            // a Pair must have exactly 2 cards
            return false;
        }
        else    {
            // check whether all cards have the same rank
            int card1Rank = getCard(0).getRank();
            int card2Rank = getCard(1).getRank();
            return (card1Rank == card2Rank);
        }
    }

    /**
     * A definition of the abstract method from the Hand class that returns the type of this hand. 
     * It simply returns the string "Pair" for the Pair class.
     * 
     * @return      A String value that denotes the type of hand (i.e. "Pair")
     */
    public String getType() {
        return "Pair";
    }

}