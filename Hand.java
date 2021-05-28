/**
 * This abstract class provides the functionality of modelling a hand of cards.
 * It is a subclass of the CardList class, and builds on top of it.
 * It provides extended functionality as well, such as determining the player
 * whose hand of cards is stored, comparing with another hand, showing the top card 
 * of the hand, and checking the validity and type of hand.
 * 
 * @author Anchit Mishra
 */
public abstract class Hand extends CardList  {

    /**
     * A private object of the CardGamePlayer storing the player to whom the hand belongs.
     */
    private CardGamePlayer player;

    /**
     * This parameterised constructor builds a hand of cards given a particular player
     * and list of cards.
     * 
     * @param       player  An object of type CardGamePlayer specifying the player to whom the hand belongs
     * @param       cards   A list of the cards from which the hand is to be created
     */
    public Hand(CardGamePlayer player, CardList cards)   {
        this.player = player;
        removeAllCards();
        // now, we add the cards from the CardList to the private ArrayList<Card>
        for (int i = 0; i < cards.size(); i++)  {
            addCard(cards.getCard(i));
        }
    }

    /**
     * A public method for retrieving the player who plays the hand.
     * 
     * @return      An object of type CardGamePlayer specifying the details of the player playing the hand
     */
    public CardGamePlayer getPlayer()  {
        return this.player;
    }

    /**
     * A method for retrieving the top card of the hand. 
     * Since every type of hand has different criteria for determining the top card, we
     * design the function logic to return the top card corresponding to the type of hand.
     * 
     * @return      An object of type Card specifying the topmost card in the hand
     */
    public Card getTopCard()    {
        // since the hand is always sorted as per game logic,
        // the last card is the top card (except for FullHouse and Quad hands, which override this method)
        if (size() > 0) {
            return getCard(size() - 1);
        }
        // empty hand, no top card exists
        return null;
    }

    /**
     * A method for comparing whether the hand beats another hand that is specified as an argument
     * 
     * @param       hand    A Hand object specifying the hand which the stored hand is to be compared against
     * 
     * @return      A boolean value of 'true' if the hand beats the specified hand
     *                              or 'false' if the hand loses to the specified hand
     */
    public boolean beats(Hand hand) {
        if (this.player.equals(hand.player)) {
            // if the current player was the one who played the last hand on the table
            // they can play any hand of their choice, hence the hand they play
            // will always beat the last hand on the table
            return true;
        }
        if (this.size() != hand.size())  {
            // except for the case of same player, only a hand of the same
            // number of cards can beat the hand on the table
            return false;
        } 
        int currentHandStrength = getHandStrength();
        int comparedHandStrength = hand.getHandStrength();
        if (currentHandStrength != 0)  {
            // this is the case for 5 card hands
            if (currentHandStrength > comparedHandStrength)   {
                return true;
            }
            else if (currentHandStrength < comparedHandStrength)  {
                return false;
            }
            // both hands have same ranking
            // we check if they are flush hands, in which case the suits must be compared and not the top cards
            if (currentHandStrength == 2)    {
                if (getCard(0).getSuit() > hand.getCard(0).getSuit())   {
                    return true;
                }
                else if (getCard(0).getSuit() < hand.getCard(0).getSuit())  {
                    return false;
                }
            }
        }
        // we simply check the top cards in case of Single, Pair, Triple and 
        // if even the 5 card hands are equivalent
        if (getTopCard().compareTo(hand.getTopCard()) == 1)  {
            return true;
        }
        else    {
            return false;
        }
    }

    /**
     * A method to retrieve an integer that differentiates the 5-card hands from each other.
     * For all other hands such as Single, Pair and Triple, it simply returns 0, since there is
     * no ambiguity in their comparisons due to size differences. The higher the value returned, the more
     * powerful the hand
     * 
     * @return      An integer value of 0 in case the hand is a Single, Double or Pair and
     *                                  the relative hand strength in case the hand is a 5 card hand
     */
    private int getHandStrength() {
        switch (getType())  {
            case "Straight":        return 1; 
            case "Flush":           return 2;
            case "FullHouse":       return 3;
            case "Quad":            return 4; 
            case "StraightFlush":   return 5;
            default:                return 0;
        }
    }


    /**
     * An abstract method used to specify whether the hand is valid or not.
     * 
     * @return      A boolean value of 'true' if the hand is a valid hand
     *                              or 'false' if the hand is an invalid hand
     */
    abstract boolean isValid();

    /**
     * A method for returning a string specifying the type of this hand.
     * 
     * @return      A String value specifying the type of this hand
     */
    abstract String getType();

}