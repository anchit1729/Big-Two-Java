/**
 * This class is used to model a deck of cards used for the Big Two game. 
 * It extends the base functionality provided by the Deck class, and adds on 
 * extra charaacteristics relevant to the Big Two game.
 * 
 * @author Anchit Mishra
 */
public class BigTwoDeck extends Deck {
    /**
     * The initialize() method is overridden in the BigTwoDeck class in order to 
     * initialize a deck of Big Two cards. The method removes all 52 cards from the 
     * deck, creates 52 Big Two cards and adds them to the deck. 
     */
    public void initialize()    {
        // first, we remove all 52 cards from the deck
        removeAllCards(); 
        // now, we create 52 Big Two cards and add them to the deck
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++)    {
                BigTwoCard bigTwoCard = new BigTwoCard(i, j);
                addCard(bigTwoCard);
            }
        }
    }
}