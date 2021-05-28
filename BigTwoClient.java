import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * This class provides the functionality of modelling a Big Two card game that supports
 * 4 players connected over the internet, and performs all tasks relevant to
 * the game logic. It implements the CardGame interface, since Big Two is a card game.
 *
 * @author Anchit Mishra
 */
public class BigTwoClient implements CardGame, NetworkGame {

    /**
     * The public constructor for the BigTwo class. It creates a new Big Two game.
     */
    public BigTwoClient() {

        // the game has 4 players
        numOfPlayers = 4;
        // set the currentIdx variable to -1 for a new game
        currentIdx = -1;
        playerList = new ArrayList<CardGamePlayer>();
        // the playerList has a maximum of 4 players in the game, we set them all up
        for (int i = 0; i < numOfPlayers; i++) {
            CardGamePlayer player = new CardGamePlayer("");
            playerList.add(player);
        }
        // create a new list for hands on table
        handsOnTable = new ArrayList<Hand>();
        // create a BigTwoConsole and start it off
        BigTwoTable bigTwoTable = new BigTwoTable(this);
        table = bigTwoTable;
        table.disable();
        // get an input for the player name
        String playerName = "";
        // don't allow empty input
        while ((playerName.isBlank()) || (playerName.isEmpty()))  {
             playerName = JOptionPane.showInputDialog("Enter Player Name:", "");
             if (playerName == null)    {
                 // cancel option, quit game
                 System.exit(1);
             }
        }
        setPlayerName(playerName);
        connectedStatus = false;
        // make a connection with the game server
        makeConnection();
        table.repaint();

    }

    /**
     * An integer value denoting the num of players in the game to make some
     * logical implementations cleaner.
     */
    private int numOfPlayers;

    /**
     * A Deck object to store a deck of cards for a Big Two game.
     */
    private Deck deck;

    /**
     * An ArrayList object to store the list of players in the game.
     */
    private ArrayList<CardGamePlayer> playerList;


    /**
     * An ArrayList object to store the hands played on the table.
     */
    private ArrayList<Hand> handsOnTable;

    /**
     * An integer variable specifying the ID of the local player.
     */
    private int playerID;

    /**
     * A String variable specifying the name of the local player.
     */
    private String playerName;

    /**
     * A String variable specifying the IP of the game server.
     */
    private String serverIP;

    /**
     * An integer variable specifying the game server's TCP port.
     */
    private int serverPort;

    /**
     * A Socket connection to the game server.
     */
    private Socket sock;

    /**
     * An ObjectOutputStream for sending messages to the server.
     */
    private ObjectOutputStream oos;

    /**
     * A boolean variable stating whether the client is connected to the server or not.
     */
    private boolean connectedStatus;

    /**
     * An integer value denoting the index of the player whose turn it is.
     */
    private int currentIdx;

    /**
     * A BigTwoTable object to create the GUI for the game and handle all user actions.
     */
    private BigTwoTable table;

    /**
     * This method retrieves the num of players playing the game.
     *
     * @return          An integer storing the num of players playing the game
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * This method retrieves the deck of the card game.
     *
     * @return          A Deck object storing the deck used in the game
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * This method retrieves the list of players playing the game.
     *
     * @return          An ArrayList storing the players playing the BigTwo game
     */
    public ArrayList<CardGamePlayer> getPlayerList() {
        return playerList;
    }

    /**
     * This method retrieves the hands played on the table.
     *
     * @return          An ArrayList storing the hands on the table
     */
    public ArrayList<Hand> getHandsOnTable() {
        return handsOnTable;
    }

    /**
     * This method retrieves the last hand that on the table to make some logic-related tasks more readable.
     *
     * @return          A Hand object storing the last hand that was played on the table
     */
    private Hand getLastHandOnTable()   {

        if (handsOnTable.size() != 0)   {
            // there is at least one hand on table
            return handsOnTable.get(handsOnTable.size() - 1);
        }

        // the table is empty
        return null;

    }

    /**
     * This method retrieves the currentIdx, i.e. the index of the current player.
     *
     * @return          An integer currentIdx, storing the index of the current player
     */
    public int getCurrentIdx() {
        return currentIdx;
    }

    /**
     * This method starts/restarts the game with a given shuffled deck of cards. It performs all relevant tasks
     * such as clearing cards from the players and table, distributing the cards to all players, identifying the
     * player who starts and setting this player to the active player for both the BigTwo object and the table.
     *
     * @param        deck A Deck object representing the deck of cards which is used in the game
     */
    public void start(Deck deck) {

        // set game deck
        this.deck = deck;
        // remove all cards from the players
        for (CardGamePlayer player: playerList)    {
            player.removeAllCards();
        }
        // remove all cards from the table
        handsOnTable.clear();
        // distribute cards amongst players
        for (int i = 0; i < 13; i++)    {
            for (CardGamePlayer player: playerList) {
                player.addCard(deck.removeCard(0));
            }
        }

        // sort the cards in hand for all players
        for (CardGamePlayer player: playerList) {
            player.sortCardsInHand();
        }

        // find the player with three of diamonds
        BigTwoCard threeOfDiamonds = new BigTwoCard(0, 2);
        for (int index = 0; index < numOfPlayers; index++) {
            if (playerList.get(index).getCardsInHand().contains(threeOfDiamonds))  {
                currentIdx = index;
                break;
            }
        }
        table.setActivePlayer(playerID);
        table.printMsg(playerList.get(getCurrentIdx()).getName() + "'s turn:\n");
        updateGameStatus();

    }

    /**
     * A method that makes a move with the player of the specified playerID using the specified card indices.
     *
     * @param           playerID an integer value denoting the playerID of the player who makes the move
     * @param           cardIdx an integer array containing the selected indices
     */
    public void makeMove(int playerID, int[] cardIdx) {
        // now becomes clearer since networking is implemented
        // send a message to the server that the client is trying to make a move
        sendMessage(new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx));
    }

    /**
     * A method that checks the move made by the specified player.
     *
     * @param           playerID an integer value denoting the playerID of the player who makes the move
     * @param           cardIdx an integer array containing the selected indices
     */
    public void checkMove(int playerID, int[] cardIdx) {

        // first, disable table while checking
        table.disable();
        Hand lastHandOnTable = getLastHandOnTable();

        CardGamePlayer currentPlayer = playerList.get(playerID);
        CardList cardsPlayed = currentPlayer.play(cardIdx);

        if (cardsPlayed == null)  {
            // pass attempted or input indices are out of range
            if (lastHandOnTable == null || lastHandOnTable.getPlayer().equals(currentPlayer))   {
                // a player cannot pass on the first turn or if the last hand on table is theirs (error check)
                table.printMsg("Not a legal move!!!\n");
            }
            else {
                // turn passed
                table.printMsg("{Pass}\n");
                currentIdx = (currentIdx + 1) % numOfPlayers;
                table.printMsg(playerList.get(getCurrentIdx()).getName() + "'s turn:\n");
                }
            updateGameStatus();
            return;
        }

        cardsPlayed.sort();

        // check for three of diamonds in case of first player move
        BigTwoCard threeOfDiamonds = new BigTwoCard(0, 2);
        if (lastHandOnTable == null && !cardsPlayed.getCard(0).equals(threeOfDiamonds))    {
            // the first move does not include three of diamonds: illegal!
            table.printMsg(cardsPlayed.toString() + " <== Not a legal move!!!\n");
            updateGameStatus();
            return;
        }

        // the cards played are legal as per preliminary check, we now compose a hand from them
        Hand newHand = composeHand(currentPlayer, cardsPlayed);

        if (newHand == null) {
            // no hand was possible from the given cards
            table.printMsg(cardsPlayed.toString() + " <== Not a legal move!!!\n");
            updateGameStatus();
            return;
        }
        else if (lastHandOnTable != null && !newHand.beats(lastHandOnTable)) {
            // the new hand loses to the last hand on table
            table.printMsg("{" + newHand.getType() + "}" + cardsPlayed.toString() + " <== Not a legal move!!!\n");
            updateGameStatus();
            return;
        }

        // the new hand beats the last hand, and takes the last hand's place
        handsOnTable.add(newHand);
        currentPlayer.removeCards(cardsPlayed);
        currentIdx = (getCurrentIdx() + 1) % numOfPlayers;
        table.printMsg("{" + newHand.getType() + "}" + newHand.toString() + "\n");

        // check if the move ends the game
        if (endOfGame()) {
            table.resetSelected();
            printEndMessage();
        }
        else {
            table.printMsg(getPlayerList().get(getCurrentIdx()).getName() + "'s turn:\n");
            updateGameStatus();
        }

    }

    /**
     * A method that determines whether or not the game has ended at any given time.
     *
     * @return     A boolean value of 'true' if the game has ended and
     *                                'false' if the game has not ended
     */
    public boolean endOfGame() {
        // check for first player with 0 cards
        for (int i = 0; i < numOfPlayers; i++)   {
            if (playerList.get(i).getNumOfCards() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * A method that prints out the num of cards left with each losing player at the end of the Big Two game
     * as well as declares the winner of the game.
     */
    private void printEndMessage()   {

        String endGameMessage = "Game ends.\n";
        String titleMessage = "";
        for (int i = 0; i < numOfPlayers; i++)   {
            CardGamePlayer player = playerList.get(i);
            if (player.getNumOfCards() != 0) {
                if (i == playerID)  {
                    titleMessage = "You Lose!";
                }
                endGameMessage += "Player " + i + " has " + player.getNumOfCards() + " cards in hand.\n";
            }
            else    {
                if (i == playerID)  {
                    titleMessage = "You Win!";
                }
                endGameMessage += "Player " + i + " wins the game.\n";
            }
        }

        JOptionPane.showMessageDialog(null, endGameMessage, titleMessage, JOptionPane.INFORMATION_MESSAGE);
        sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));

    }

    /**
     * A method that updates the game status once the move has been checked by checkMove().
     */
    private void updateGameStatus() {
        table.resetSelected();
        table.enable();
    }

    /**
     * A method for returning a valid hand from the specified list of cards of a player. It returns
     * a null value if no valid hand can be constructed from the list of cards.
     *
     * @param      player   An object of type CardGamePlayer containing information about the player
     * @param      cards    A list of the cards that the given player has
     *
     * @return     An object of type Hand, containing the hand returned from the given set of cards
     */
    public static Hand composeHand(CardGamePlayer player, CardList cards)   {

        switch(cards.size())    {
            case 1: return (new Single(player, cards)); // single created
            case 2: return ((new Pair(player, cards)).isValid() ? (new Pair(player, cards)) : null); // pair created if valid
            case 3: return ((new Triple(player, cards)).isValid() ? (new Triple(player, cards)) : null); // triple created if valid
            case 5: // check the highest level 5 card hand that can be made
                if (new StraightFlush(player, cards).isValid())  {
                    return new StraightFlush(player, cards); // straight flush possible
                }
                else if (new Quad(player, cards).isValid()) {
                    return new Quad(player, cards); // quad possible
                }
                else if (new FullHouse(player, cards).isValid())    {
                    return new FullHouse(player, cards); // full house possible
                }
                else if (new Flush(player, cards).isValid())    {
                    return new Flush(player, cards); // flush possible
                }
                else if (new Straight(player, cards).isValid()) {
                    return new Straight(player, cards); // straight possible
                }
            default: return null; // no hand possible
        }

    }

    /**
     * A method for getting the playerID of the local player.
     *
     * @return          An integer variable denoting the playerID of the local player.
     */
    public int getPlayerID()    {
        return playerID;
    }

    /**
     * A method for setting the playerID of the local player.
     *
     * @param       playerID An integer variable denoting the player ID to be assigned to the local player
     */
    public void setPlayerID(int playerID)   {
        this.playerID = playerID;
    }

    /**
     * A method for retrieving the name of the local player.
     *
     * @return      A String object containing the name of the local player
     */
    public String getPlayerName()   {
        return playerName;
    }

    /**
     * A method for setting the name of the local player.
     *
     * @param       playerName A String object denoting the name to be assigned to the local player
     */
    public void setPlayerName(String playerName)    {
        this.playerName = playerName;
    }

    /**
     * A method for obtaining the IP address of the game server.
     *
     * @return      A String object containing the IP address of the game server
     */
    public String getServerIP() {
        return serverIP;
    }

    /**
     * A method for setting the IP address of the game server.
     *
     * @param       serverIP A String object denoting the IP address to be assigned for the game server
     */
    public void setServerIP(String serverIP)    {
        this.serverIP = serverIP;
    }

    /**
     * A method for obtaining the TCP port of the game server.
     *
     * @return      An integer variable containing the TCP port of the game server
     */
    public int getServerPort()  {
        return serverPort;
    }

    /**
     * A method for setting the TCP port of the game server.
     *
     * @param       serverPort An integer variable denoting the TCP port to be assigned for the game server
     */
    public void setServerPort(int serverPort)    {
        this.serverPort = serverPort;
    }

    /**
     * A method for making a socket connection with the game server.
     */
    public void makeConnection() {
        // set the server IP and TCP port
        setServerIP("127.0.0.1");
        setServerPort(2396);
        // create a new Socket to connect to the game server
        try {
            sock = new Socket(getServerIP(), getServerPort());
            connectedStatus = true;
        } catch(IOException e)    {
            System.out.println("Error in creating a new socket to connect to server!");
            // we print that the connection was lost
            for (int i = 0; i < getPlayerList().size(); i++)   {
                getPlayerList().get(i).removeAllCards();
            }
            JOptionPane.showMessageDialog(null, "Cannot connect to the server!");
            connectedStatus = false;
            table.printMsg("Cannot connect to the server!\n");
            e.printStackTrace();
            return;
        }
        // create the ObjectOutputStream object for the socket
        try {
            oos = new ObjectOutputStream(sock.getOutputStream());
        }
        catch (IOException e) {
            System.out.println("Cannot get ObjectOutputStream object for the socket!");
            e.printStackTrace();
            return;
        }
        // create a new thread for handling the server interactions and assign it to the runnable ServerHandler class
        Runnable serverHandlerRunnable = new ServerHandler();
        Thread serverHandlerThread = new Thread(serverHandlerRunnable);
        serverHandlerThread.start();

        // send a message to join the server
        sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, playerName));

    }

    /**
     * A method for parsing messages received from the game server.
     *
     * @param       message A GameMessage object that denotes the message received from the server
     */
    public synchronized void parseMessage(GameMessage message)   {

        // we define separate cases for different classes of messages
        int messageType = message.getType();
        switch(messageType) {

            // successfully established connection
            case CardGameMessage.PLAYER_LIST:
                setPlayerID(message.getPlayerID());
                String [] stringPlayerList = (String []) message.getData();
                // assign the player names to the client player list
                for (int i = 0; i < stringPlayerList.length; i++) {
                    if (stringPlayerList[i] != null)
                        playerList.get(i).setName(stringPlayerList[i]);
                    else
                        playerList.get(i).setName("");
                }
                break;

            //  new player joined the server
            case CardGameMessage.JOIN:
                // set the name of the newly joined player
                playerList.get(message.getPlayerID()).setName((String) message.getData());
                // in case the new player is the same as the local player, send a ready message
                if (message.getPlayerID() == playerID)  {
                    sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
                }
                else    {
                    table.printMsg((String) message.getData() + " joins the game.\n");
                }
                break;

            // server is full, display an error
            case CardGameMessage.FULL:
                table.printMsg("The server is full! Unable to join the game!\n");
                try {
                    sock.close();
                }
                catch(Exception exception)  {
                    System.out.println("Error while trying to close the socket connection!\n");
                    exception.printStackTrace();
                }
                connectedStatus = false;
                break;

            // someone quit the game
            case CardGameMessage.QUIT:
                // remove the player who quit the game
                table.printMsg(playerList.get(message.getPlayerID()).getName() + " left the game.\n");
                playerList.get(message.getPlayerID()).setName("");
                // game is currently in progress
                if (!endOfGame())   {
                    // stop the game
                    table.resetSelected();
                    table.disable();
                    sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
                }
                break;

            // the server says a player is ready
            case CardGameMessage.READY:
                table.printMsg("Player Name: " + playerList.get(message.getPlayerID()).getName() + " is ready.\n");
                break;

            // the server prompts to start a game
            case CardGameMessage.START:
                table.printMsg("All players are ready. Starting game. \n");
                start((Deck) message.getData());
                break;

            // the server says that a move has been made by a player
            case CardGameMessage.MOVE:
                checkMove(message.getPlayerID(), (int[]) message.getData());
                break;

            // the server says that a message has been sent in game chat
            case CardGameMessage.MSG:
                table.printChatMsg((String) message.getData());
                break;

            // default case for messages, just to catch any errors in the code
            default:
                table.printMsg("Error encountered! Some messages may not have been parsed correctly!\n");
                table.resetSelected();
                printEndMessage();

        }
        table.repaint();

    }

    /**
     * A method for sending messages from the local client to the game server.
     *
     * @param       message A GameMessage object that denotes the message to be sent to the server
     */
    public void sendMessage(GameMessage message)    {
        try {
            oos.writeObject(message);
        }
        catch (IOException e) {
            System.out.println("Could not perform message write on the ObjectOutputStream object!");
            e.printStackTrace();
        }
    }

    /**
     * This class is an inner class which serves the purpose of handling interactions with the game server. An object of
     * the class is created by a thread when the game connection is made for the first time. This class implements
     * the Runnable interface.
     *
     * @author Anchit Mishra
     */
    private class ServerHandler implements Runnable {

        /**
         * An ObjectInputStream object for receiving inputs from the server via the socket.
         */
        private ObjectInputStream ois;

        /**
         * An implementation of the run() method from the Runnable interface. This method is responsible for the
         * thread execution of all tasks that the ServerHandler class is responsible for.
         */
        public synchronized void run()   {

            // create the ObjectInputStream object for receiving messages
            try {
                ois = new ObjectInputStream(sock.getInputStream());
            }
            catch (IOException e) {
                System.out.println("Could not attach the ObjectInputStream object to the Socket!");
                e.printStackTrace();
            }
            // a variable for storing messages received from the game server
            GameMessage receivedMessage;
            try {
                // wait till a message is received
                while ((receivedMessage = (GameMessage) ois.readObject()) != null)  {
                    parseMessage(receivedMessage);
                }
                ois.close();
            }
            catch (IOException | ClassNotFoundException e) {
                System.out.println("Could not read the message from ObjectInputStream!");
                // we print that the connection was lost
                JOptionPane.showMessageDialog(null, "Cannot connect to the server!");
                table.printMsg("Cannot connect to the server!\n");
                connectedStatus = false;
                for (int i = 0; i < getPlayerList().size(); i++)   {
                    getPlayerList().get(i).removeAllCards();
                }
                e.printStackTrace();
            }
        }

    }

    /**
     * A method to check whether the current user is already connected to the server or not.
     * This is to make sure no local user is able to connect twice to the server and play
     * as two different players.
     *
     * @return     A boolean value of 'true', if the client is connected to the server,
     *                             or 'false', if the client is not connected to the server.
     */
    public boolean isConnected()    {
        return connectedStatus;
    }

    /**
     * A method for starting a client for playing Big Two. It creates an instance of the Big Two Client.
     *
     * @param      args    A string of arguments supplied to the main function
     */
    public static void main(String[] args)  {
        // create a new BigTwo card game
        new BigTwoClient();
    }

}
