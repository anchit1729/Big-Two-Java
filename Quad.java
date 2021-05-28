/**
 * This class implements the Quad hand type from the game of Big Two. It is used to model 
 * the attributes and behaviour of a Quad hand, and extend the base functionality
 * of the Hand class.
 * 
 * @author Anchit Mishra
 */
public class Quad extends Hand  {

    /**
     * A constructor method that takes two arguments - the details of the player
     * to whom the hand belongs, as well as the list of cards stored.
     * It initialises the player and cards instance variables.
     * 
     * @param       player  The CardGamePlayer to whom the hand belongs
     * @param       cards   The CardList of cards that are in the hand
     */
    public Quad(CardGamePlayer player, CardList cards) {
        super(player, cards);
        sort();
    }

    /**
     * A method that overrides the getTopCard() method defined in the Hand superclass. This is because
     * the Quad hand has a slightly different check for the top card, since the 
     * card with the highest suit in the quadruplet is defined to be the top card for a Quad hand.
     * 
     * @return      A Card object specifying the top card of the Quad hand
     */
    public Card getTopCard()    {
        // the cards are already sorted as per the game logic
        // we find the quadruplet first, then return the card with the highest suit in the quadruplet
        if (getCard(0).getRank() == getCard(3).getRank())   {
            // we return the card at index 3, since it has the highest suit in the quadruplet
            return getCard(3);
        }
        else if (getCard(1).getRank() == getCard(4).getRank())   {
            // we return the card at index 4, since it has the highest suit in the quadruplet
            return getCard(4);
        }
        // error case, ideally execution should not fall through to here
        return null;
    }

    /**
     * A definition of the abstract method from the Hand class to check whether the hand is a valid Quad or not.
     * A Quad in Big Two is defined as a hand consisting of 4 cards of the same rank, and 1 different card.
     * 
     * @return      A boolean value of 'true' if the hand is a valid Quad and
     *                                 'false' if the hand is an invalid Quad
     */
    public boolean isValid()    {
        if (size() != 5)    {
            // a Quad must consist of exactly 5 cards
            return false;
        }
        else    {
            // the cards are already sorted as per game logic
            // we check whether the criteria for a Quad are met
            if (getCard(0).getRank() == getCard(3).getRank() || getCard(1).getRank() == getCard(4).getRank())   {
                // the first 4 cards are of the same rank or the last 4 cards are of the same rank
                return true;
            }
            return false;
        }
    }

    /**
     * A definition of the abstract method from the Hand class that returns the type of this hand. 
     * It simply returns "Quad" for the Quad class.
     * 
     * @return      A String value that denotes the type of hand (i.e. "Quad")
     */
    public String getType() {
        return "Quad";
    }
}