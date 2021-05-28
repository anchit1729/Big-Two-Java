/**
 * This class implements the FullHouse hand type from the game of Big Two. It is used to model 
 * the attributes and behaviour of a FullHouse hand, and extend the base functionality
 * of the Hand class.
 * 
 * @author Anchit Mishra
 */
public class FullHouse extends Hand  {

    /**
     * A constructor method that takes two arguments - the details of the player
     * to whom the hand belongs, as well as the list of cards stored.
     * It initialises the player and cards instance variables.
     * 
     * @param       player  The CardGamePlayer to whom the hand belongs
     * @param       cards   The CardList of cards that are in the hand
     */
    public FullHouse(CardGamePlayer player, CardList cards)    {
        super(player, cards);
        sort();
    }

    /**
     * A method that overrides the getTopCard() method defined in the Hand superclass. This is because
     * the Full House hand has a slightly more complicated check for the top card, since the 
     * card with the highest suit in the triple is defined to be the top card for a Full House hand.
     * 
     * @return      A Card object specifying the top card of the FullHouse hand
     */
    public Card getTopCard()    {
        // the cards are already sorted as per game logic
        // we find the triple first, then return the highest suit from the triple
        if (getCard(0).getRank() == getCard(1).getRank() && getCard(2).getRank() == getCard(4).getRank())   {
            // the card at index 4 is the card in the triple with highest suit
            return getCard(4);
        }
        else if (getCard(0).getRank() == getCard(2).getRank() && getCard(3).getRank() == getCard(4).getRank())   {
            // the card at index 2 is the card in the triple with highest suit
            return getCard(2);
        }
        // error case, ideally execution should not fall through to here since the hand is a valid triple
        return null;
    }

    /**
     * A definition of the abstract method from the Hand class to check whether the hand is a valid Full House or not.
     * A Full House in Big Two is defined as a hand consisting of 5 cards, 2 of one rank and 3 of another.
     * 
     * @return      A boolean value of 'true' if the hand is a valid Full House and
     *                                 'false' if the hand is an invalid Full House
     */
    public boolean isValid()    {
        if (size() != 5)    {
            // a Full House must consist of exactly 5 cards
            return false;
        }
        else    {
            // the cards are already sorted as per game logic
            // we check whether the criteria for a Full House are met
            if ((getCard(0).getRank() == getCard(1).getRank() && getCard(2).getRank() == getCard(4).getRank()) || (getCard(0).getRank() == getCard(2).getRank() && getCard(3).getRank() == getCard(4).getRank()))   {
                // the first 3 cards form a triple, the last 2 form a pair or the first 2 form a pair, last 3 form a triple
                return true;
            }
            return false;
        }
    }

    /**
     * A definition of the abstract method from the Hand class that returns the type of this hand. 
     * It simply returns "FullHouse" for the FullHouse class.
     * 
     * @return      A String value that denotes the type of hand (i.e. "FullHouse")
     */
    public String getType() {
        return "FullHouse";
    }
}