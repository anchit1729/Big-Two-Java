/**
 * This class implements the Straight hand type from the game of Big Two. It is used to model 
 * the attributes and behaviour of a Straight hand, and extend the base functionality
 * of the Hand class.
 * 
 * @author Anchit Mishra
 */
public class Straight extends Hand  {

    /**
     * A constructor method that takes two arguments - the details of the player
     * to whom the hand belongs, as well as the list of cards stored.
     * It initialises the player and cards instance variables.
     * 
     * @param       player  The CardGamePlayer to whom the hand belongs
     * @param       cards   The CardList of cards that are in the hand
     */
    public Straight(CardGamePlayer player, CardList cards) {
        super(player, cards);
        sort();
    }

    /**
     * A definition of the abstract method from the Hand class to check whether the hand is a valid Straight or not.
     * A Straight in Big Two is defined as a hand consisting of 5 cards of consecutive ranks.
     * 
     * @return      A boolean value of 'true' if the hand is a valid Straight and
     *                                 'false' if the hand is an invalid Straight 
     */
    public boolean isValid()    {
        if (size() != 5)    {
            // a Straight must consist of exactly 5 cards
            return false;
        }
        else    {
            // we check whether all cards have consecutive ranks
            // note that all cards are sorted as per game logic
            int currentRank;
            int nextRank;
            for (int i = 0; i < size() - 1; i++)    {
                currentRank = (getCard(i).getRank() + 11) % 13;
                nextRank = (getCard(i + 1).getRank() + 11) % 13;
                if (currentRank + 1 != nextRank)   {
                    // no need to check cyclic consecutiveness
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * A definition of the abstract method from the Hand class that returns the type of this hand. 
     * It simply returns "Straight" for the Straight class.
     * 
     * @return      A String value that denotes the type of hand (i.e. "Straight")
     */
    public String getType() {
        return "Straight";
    }
}