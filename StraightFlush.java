/**
 * This class implements the Straight Flush hand type from the game of Big Two. It is used to model 
 * the attributes and behaviour of a Straight Flush hand, and extend the base functionality
 * of the Hand class.
 * 
 * @author Anchit Mishra
 */
public class StraightFlush extends Hand {

    /**
     * A constructor method that takes two arguments - the details of the player
     * to whom the hand belongs, as well as the list of cards stored.
     * It initialises the player and cards instance variables.
     * 
     * @param       player  The CardGamePlayer to whom the hand belongs
     * @param       cards   The CardList of cards that are in the han
     */
    public StraightFlush(CardGamePlayer player, CardList cards)    {
        super(player, cards);
        sort();
    }

    /**
     * A definition of the abstract method from the Hand class to check whether the hand is a valid Straight Flush or not.
     * A Straight Flush in Big Two is defined as a hand consisting of 5 cards of the same suit and with consecutive ranks.
     * 
     * @return      A boolean value of 'true' if the hand is a valid Straight Flush and
     *                                 'false' if the hand is an invalid Straight Flush
     */
    public boolean isValid()    {
        if (size() != 5)    {
            // a Straight Flush must consist of exactly 5 cards
            return false;
        }
        else    {
            // note that the cards are already sorted as per game logic
            // we check whether the criteria of a Straight Flush are met
            int baseSuit = getCard(size() - 1).getSuit();
            for (int i = 0; i < size() - 1; i++)    {
                int currentRank = (getCard(i).getRank() + 11) % 13;
                int nextRank = (getCard(i + 1).getRank() + 11) % 13;
                int comparedSuit = getCard(i).getSuit();
                if ((currentRank + 1 != nextRank) || (baseSuit != comparedSuit))    {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * A definition of the abstract method from the Hand class that returns the type of this hand. 
     * It simply returns "StraightFlush" for the StraightFlush class.
     * 
     * @return      A String value that denotes the type of hand (i.e. "StraightFlush")
     */
    public String getType() {
        return "StraightFlush";
    }
}