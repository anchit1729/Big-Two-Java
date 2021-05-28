/**
 * This class implements the Triple hand type from the game of Big Two. It is used to model 
 * the attributes and behaviour of a Triple hand, and extend the base functionality
 * of the Hand class.
 * 
 * @author Anchit Mishra
 */
public class Triple extends Hand    {
    
    /**
     * A constructor method that takes two arguments - the details of the player
     * to whom the hand belongs, as well as the list of cards stored.
     * It initialises the player and cards instance variables.
     * 
     * @param       player  The CardGamePlayer to whom the hand belongs
     * @param       cards   The CardList of cards that are in the hand
     */
    public Triple(CardGamePlayer player, CardList cards)   {
        super(player, cards);
        sort();
    }

    /**
     * A definition of the abstract method from the Hand class to check whether the hand is a valid Triple or not.
     * A Triple in Big Two is defined as a hand consisting of three cards of the same rank.
     * 
     * @return      A boolean value of 'true' if the hand is a valid Triple and
     *                                 'false' if the hand is an invalid Triple
     */
    public boolean isValid()    {
        if (size() != 3)    {
            // a Triple must consist of exactly three cards
            return false;
        }
        else    {
            // we check whether all the cards have the same rank
            // since the hand is sorted, the first and last card should have same rank
            // if the hand is a valid Triple
            int firstCardRank = getCard(0).getRank();
            int lastCardRank = getCard(size() - 1).getRank();
            return (firstCardRank == lastCardRank);
        }
    }

    /**
     * A definition of the abstract method from the Hand class that returns the type of this hand.
     * It simply returns the string "Triple" for the Triple class.
     * 
     * @return      A String value that denotes the type of hand (i.e. "Triple")
     */
    public String getType() {
        return "Triple";
    }
}