/**
 * This class implements the Flush hand type from the game of Big Two. It is used to model 
 * the attributes and behaviour of a Flush hand, and extend the base functionality
 * of the Hand class.
 * 
 * @author Anchit Mishra
 */
public class Flush extends Hand {

    /**
     * A constructor method that takes two arguments - the details of the player
     * to whom the hand belongs, as well as the list of cards stored.
     * It initialises the player and cards instance variables.
     * 
     * @param       player  The CardGamePlayer to whom the hand belongs
     * @param       cards   The CardList of cards that are in the hand
     */
    public Flush(CardGamePlayer player, CardList cards)    {
        super(player, cards);
        sort();
    }

    /**
     * A definition of the abstract method from the Hand class to check whether the hand is a valid Flush or not.
     * A Flush in Big Two is defined as a hand consisting of 5 cards of the same suit.
     * 
     * @return      A boolean value of 'true' if the hand is a valid Flush and
     *                                 'false' if the hand is an invalid Flush 
     */
    public boolean isValid()    {
        if (size() != 5)    {
            // a Flush must consist of exactly 5 cards
            return false;
        }
        else    {
            // we check whether the cards are of the same suit
            int baseSuit = getCard(0).getSuit();
            for (int i = 1; i < size(); i++)    {
                int comparedSuit = getCard(i).getSuit();
                if (baseSuit != comparedSuit)   {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * A definition of the abstract method from the Hand class that returns the type of this hand. 
     * It simply returns "Flush" for the Flush class.
     * 
     * @return      A String value that denotes the type of hand (i.e. "Flush")
     */
    public String getType() {
        return "Flush";
    }
}