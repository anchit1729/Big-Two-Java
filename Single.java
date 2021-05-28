/**
 * This class implements the Single hand from the game of Big Two. It is used to model 
 * the attributes and behaviour of a Single hand, and extend the base functionality
 * of the Hand class.
 * 
 * @author Anchit Mishra
 */
public class Single extends Hand {

    /**
     * A constructor method that takes two arguments - the details 
     * of the player to whom the hand belongs, as well as the list of cards stored
     * It initialises the player and cards instance variables.
     * 
     * @param   player  The CardGamePlayer to whom the hand belongs
     * @param   cards   The CardList of cards that are in the hand
     */
    public Single(CardGamePlayer player, CardList cards)    {
        super(player, cards);
    }
    
    /**
     * A definition of the abstract method from the Hand class to check whether the hand is a valid Single or not.
     * A Single in Big Two is defined to be a hand consisting of only one card.
     * 
     * @return      A boolean value of 'true' if the hand is a valid Single and
     *                                 'false' if the hand is an invalid Single
     */
    public boolean isValid()   {
        if (this.size() == 1) {
            return true;
        }
        else    {
            return false;
        }
    }

    /**
     * A definition of the abstract method from the Hand class to retrieve the type of the current hand. 
     * It simply returns a string "Single" for the Single class.
     * 
     * @return      A String value that denotes the type of hand (i.e. "Single")
     */
    public String getType() {
        return "Single";
    }

}