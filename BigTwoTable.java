import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.Flow;


/**
 * This class provides the functionality of building the GUI and handling all
 * user actions. It implements the CardGameTable interface.
 *
 * @author Anchit Mishra
 */
public class BigTwoTable implements CardGameTable   {

    /**
     * The public constructor for the BigTwoTable class. It takes in a CardGame object
     * as an argument and uses it to create a BigTwoTable for the passed game.
     *
     * @param       game    A CardGame object which specifies the game for which a
     *                      BigTwoTable is to be created
     */
    public BigTwoTable(BigTwoClient game) {

        this.game = game;
        resetSelected();
        activePlayer = game.getCurrentIdx();
        setupImages();
        setupGUIElements();
        disable();

    }

    /**
     * A BigTwoClient variable that stores the Client that is associated with the BigTwoTable created.
     */
    private BigTwoClient game;



    /**
     * A boolean array variable that specifies the cards that have been selected in the game by the user(s).
     */
    private boolean[] selected;

    /**
     * An integer variable that specifies the index of the current active player (the one whose turn it is).
     */
    private int activePlayer;

    /**
     * A JFrame that specifies the main window of the application.
     */
    private JFrame frame;

    /**
     * A JPanel that specifies the panel for showing the cards of each player as well as the cards
     * that have been played on the table.
     */
    private JPanel bigTwoPanel;

    /**
     * A JButton that specifies a 'Play' button for the active player to play the selected cards.
     */
    private JButton playButton;

    /**
     * A JButton that specifies a 'Pass' button for the active player to pass their turn.
     */
    private JButton passButton;

    /**
     * A JTextArea that displays the current game status as well as prints end of game messages.
     */
    private JTextArea msgArea;

    /**
     * A JTextArea that displays the current game chat messages for the multiplayer chat component of the
     * application.
     */
    private JTextArea chatMsgArea;

    /**
     * A JTextField that takes string input for messages to be sent in the Big Two card game.
     */
    private JTextField chatMsgField;

    /**
     * A 2D Image array storing images for the faces of the cards.
     */
    private Image[][] cardImages;

    /**
     * An Image for storing the image for the back of the cards.
     */
    private Image cardBackImage;

    /**
     * An Image array for storing the images for the player avatars.
     */
    private Image[] avatars;

    /**
     * A method to set the index of the active player (the player whose turn it is).
     *
     * @param       activePlayer    An integer representing the index of the current active player
     */
    public void setActivePlayer(int activePlayer)   {
        this.activePlayer = activePlayer;
    }

    /**
     * A method to get an array of indices of the cards selected.
     *
     * @return      An array of indices representing the cards selected
     */
    public int[] getSelected()  {
        ArrayList<Integer> selectedCardList = new ArrayList<Integer>();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i])    {
                selectedCardList.add(i);
            }
        }
        int[] finalSelectedCardList = new int[selectedCardList.size()];
        for (int i = 0; i < selectedCardList.size(); i++)   {
            finalSelectedCardList[i] = selectedCardList.get(i).intValue();
        }
        return finalSelectedCardList;
    }

    /**
     * A method that resets the list of selected cards to an empty list.
     */
    public void resetSelected()    {
        this.selected = new boolean[13];
    }

    /**
     * A method that serves to repaint the GUI of the game.
     */
    public void repaint()   {
        frame.repaint();
    }

    /**
     * This method prints a specified message to the message area of the CardGameTable.
     *
     * @param       msg A String containing the message to be printed to the message area
     */
    public void printMsg(String msg)    {
        msgArea.append(msg);
    }

    /**
     * This method prints out a specified message to the chat area of the CardGameTable.
     * It is part of the GUI for the multiplayer chat component of the BigTwo application.
     *
     * @param       msg A String containing the message to be printed to the chat area
     */
    public void printChatMsg(String msg)    {
        chatMsgArea.append(msg + "\n");
    }

    /**
     * This method clears the message area of the CardGameTable.
     */
    public void clearMsgArea()  {
        msgArea.setText("");
    }

    /**
     * This method clears the chat message area of the CardGameTable.
     */
    public void clearChatMsgArea()  {
        chatMsgArea.setText("");
    }

    /**
     * This method resets the GUI. It performs a reset on all relevant attributes as well, such as clearing
     * the selected array and emptying the message area. It also enables user interactions.
     */
    public void reset() {
        resetSelected();
        clearMsgArea();
        clearChatMsgArea();
        enable();
    }

    /**
     * This method enables user interactions with the GUI.
     */
    public void enable()    {
        setInteractionsEnabled(true);
    }

    /**
     * This method disables user interactions with the GUI.
     */
    public void disable() {
        setInteractionsEnabled(false);
    }

    /**
     * This method acts as a helper for the enable() and disable() methods, since both methods only differ in their
     * actions by a boolean parameter.
     *
     * @param       toggleOption A boolean value specifying whether interactions are to be enabled (true)
     *                                                                                  or disabled (false)
     */
    private void setInteractionsEnabled(boolean toggleOption)   {
        playButton.setEnabled(toggleOption);
        passButton.setEnabled(toggleOption);
        bigTwoPanel.setEnabled(toggleOption);
    }

    /**
     * This method sets up the images used for the game application such as the avatar icons, the card faces and
     * the card back image. It loads these images into the class objects meant to store them for rendering.
     */
    private void setupImages()  {

        // use suit and rank arrays for easier logic in loading card faces
        char[] rank = {'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k'};
        char[] suit = {'d', 'c', 'h', 's'};

        // string to store the source images directory and subdirectories
        String imageSourceDirectory = "./images/";
        String cardDirectory = imageSourceDirectory + "cards/";
        String avatarDirectory = imageSourceDirectory + "avatars/";

        // load the card faces first
        // cardImages has 4 rows (suits) and 13 columns (cards per suit)
        cardImages = new Image[4][13];
        for (int suitIndex = 0; suitIndex < 4; suitIndex++) {
            for (int rankIndex = 0; rankIndex < 13; rankIndex++)    {
                // use temp variable for better readability
                Image cardImage = new ImageIcon(cardDirectory + rank[rankIndex] + suit[suitIndex] + ".gif").getImage();
                cardImages[suitIndex][rankIndex] = cardImage;
            }
        }

        // load the card back next
        cardBackImage = new ImageIcon(cardDirectory + "/b.gif").getImage();

        // finally, load the avatars
        // not entirely sure how many avatars are meant to be created, will use 4 as a default
        avatars = new Image[4];
        String[] avatarNames = {"batman_128.png", "iron_man_128.png", "captain_america_128.png", "wolverine_128.png"};

        for (int avatarIndex = 0; avatarIndex < 4; avatarIndex++)   {
            // again use temp variable for better readability
            Image avatarImage = new ImageIcon(avatarDirectory + avatarNames[avatarIndex]).getImage().getScaledInstance(84, 84, Image.SCALE_SMOOTH);
            avatars[avatarIndex] = avatarImage;
        }

    }

    /**
     * This method is responsible for setting up all of the elements of the GUI for the application and laying them
     * out as per the specification planned.
     */
    private void setupGUIElements() {

        // set up primary frame first
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(1200, 780));
        frame.setResizable(false);

        // set up the menu bar
        JMenuBar menuBar = new JMenuBar();
        // create menu for menu bar
        JMenu gameMenu = new JMenu("Game Menu");
        // create menu items for menu
        JMenuItem connectMenuItem = new JMenuItem("Connect");
        JMenuItem quitMenuItem = new JMenuItem("Quit");
        connectMenuItem.addActionListener(new ConnectMenuItemListener());
        quitMenuItem.addActionListener(new QuitMenuItemListener());
        // add components in order
        gameMenu.add(connectMenuItem);
        gameMenu.add(quitMenuItem);
        menuBar.add(gameMenu);
        frame.setJMenuBar(menuBar);

        // set up the message area
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.PAGE_AXIS));
        msgArea = new JTextArea(20, 40);
        msgArea.setEnabled(false);
        JScrollPane messageScrollPane = new JScrollPane(msgArea);
        DefaultCaret messageCaret = (DefaultCaret) msgArea.getCaret();
        messageCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        messageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // set up the chat message area
        chatMsgArea = new JTextArea(20, 40);
        chatMsgArea.setLineWrap(true);
        chatMsgArea.setEditable(false);
        chatMsgArea.setWrapStyleWord(true);
        JScrollPane chatMessageScrollPane = new JScrollPane(chatMsgArea);
        DefaultCaret chatMessageCaret = (DefaultCaret) chatMsgArea.getCaret();
        chatMessageCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        chatMessageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatMessageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JLabel chatLabel = new JLabel("Message: ");
        chatMsgField = new JTextField(40);
        chatMsgField.addActionListener(new SendMessageListener());
        messagePanel.add(messageScrollPane);
        messagePanel.add(chatMessageScrollPane);
        JPanel chatInputPanel = new JPanel();
        chatInputPanel.setLayout(new FlowLayout());
        chatInputPanel.add(chatLabel);
        chatInputPanel.add(chatMsgField);
        messagePanel.add(chatInputPanel);
        frame.add(messagePanel, BorderLayout.EAST);

        // set up the BigTwoPanel component
        bigTwoPanel = new BigTwoPanel();
        bigTwoPanel.setSize(new Dimension(600, 650));
        frame.add(bigTwoPanel, BorderLayout.CENTER);

        // set up the Play and Pass buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(600, 40));
        buttonPanel.setLayout(new FlowLayout());
        playButton = new JButton("Play");
        passButton = new JButton("Pass");
        playButton.addActionListener(new PlayButtonListener());
        passButton.addActionListener(new PassButtonListener());
        buttonPanel.add(playButton);
        buttonPanel.add(passButton);
        // enable the buttons and the panel for those who are active players
        if (activePlayer == game.getCurrentIdx())   {
            playButton.setEnabled(true);
            passButton.setEnabled(true);
        }
        else {
            playButton.setEnabled(false);
            passButton.setEnabled(false);
        }
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

    }

    /**
     * This class is an inner class for BigTwoTable that extends the JPanel class and implements
     * the MouseListener interface. It overrides the paintComponent() method from JPanel to draw the CardGameTable,
     * and implements the mouseClicked() method from MouseListener to handle mouse clicks.
     *
     * @author Anchit Mishra
     */
    private class BigTwoPanel extends JPanel implements MouseListener   {

        /**
         * This is the public constructor for the BigTwoPanel class. It sets up the background for the
         * BigTwoPanel and attaches itself as the MouseListener.
         */
        public BigTwoPanel()    {
            setBackground(new Color(0, 128, 0));
            addMouseListener(this);
        }

        /**
         * An integer to store the base X coordinate for the player avatar.
         */
        private final int playerAvatarXCoordinate = 5;

        /**
         * An integer to store the base Y coordinate for the player avatar.
         */
        private final int playerAvatarYCoordinate = 23;

        /**
         * An integer to store the base X coordinate for the player cards.
         */
        private final int playerCardXCoordinate = 100;

        /**
         * An integer to store the base Y coordinate for the player cards in unselected state.
         */
        private final int playerCardYCoordinateNormal = 20;

        /**
         * An integer to store the base Y coordinate for the player cards in selected state.
         */
        private final int playerCardYCoordinateSelected = 5;

        /**
         * An integer to store the offset for stacking cards.
         */
        private final int cardStackingOffset = 30;

        /**
         * An integer to store the base X coordinate for the player name.
         */
        private final int playerNameXCoordinate = 5;

        /**
         * An integer to store the base Y coordinate for the player name.
         */
        private final int playerNameYCoordinate = 15;

        /**
         * An integer to store the offset for moving between individual players.
         */
        private final int playerOffset = 140;

        /**
         * An integer to store the base X coordinate for drawing a line between players.
         */
        private final int lineXCoordinate = 0;

        /**
         * An integer to store the base Y coordinate for drawing a line between players.
         */
        private final int lineYCoordinate = playerCardYCoordinateNormal + 110;

        /**
         * This method overrides the paintComponent method from the JPanel class and basically implements
         * the design of the BigTwoPanel and renders the components for it.
         *
         * @param           g A Graphics object provided by the system for drawing graphics on screen
         */
        protected void paintComponent(Graphics g)   {

            g.setColor(new Color(0, 128, 0));
            g.fillRect(0, 0, 800, 850);
            // first, draw the player names
            writePlayerNames(g);
            // next, draw the player avatars
            drawAvatars(g);
            // next, draw the player cards as well as the table contents
            drawCardsAndTableContent(g);

        }

        /**
         * This method is responsible for writing out the player names on the BigTwoPanel object.
         *
         * @param           g A Graphics object used for drawing on screen
         */
        private void writePlayerNames(Graphics g)   {

            int numberOfPlayers = game.getNumOfPlayers();
            for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++)    {
                String playerName = game.getPlayerList().get(playerIndex).getName();
                if (playerIndex == game.getCurrentIdx())    {
                    // player whose turn it is right now
                    g.setColor(Color.GREEN);
                }
                else if (playerIndex == activePlayer)    {
                    // player who is running the local client
                    g.setColor(Color.BLUE);
                }
                else    {
                    // all other players
                    g.setColor(Color.WHITE);
                }
                g.drawString(playerName, playerNameXCoordinate, playerNameYCoordinate + playerIndex * playerOffset);
            }

        }

        /**
         * This method is responsible for painting the avatars on the BigTwoPanel object.
         *
         * @param           g A Graphics object used for drawing on screen
         */
        private void drawAvatars(Graphics g)    {

            int numberOfPlayers = game.getNumOfPlayers();
            for (int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++)  {
                String playerName = game.getPlayerList().get(playerIndex).getName();
                if (playerName.isBlank() || playerName.isEmpty())   {
                    // skip rendering non-existent players
                    continue;
                }
                g.drawImage(avatars[playerIndex], playerAvatarXCoordinate, playerAvatarYCoordinate + playerIndex * playerOffset, this);
            }

        }

        /**
         * This method is responsible for drawing the players' cards on the BigTwoPanel object
         *
         * @param           g A Graphics object used for drawing on screen
         */
        private void drawCardsAndTableContent(Graphics g)   {

            int numberOfPlayers = game.getNumOfPlayers();
            for (int playerIndex = 0; playerIndex < numberOfPlayers + 1; playerIndex++) {
                if (playerIndex < numberOfPlayers) {
                    if ((!game.getPlayerList().get(playerIndex).getName().isEmpty()) && (!game.getPlayerList().get(playerIndex).getName().isBlank())) {
                        // render the contents for each player
                        CardGamePlayer currentPlayer = game.getPlayerList().get(playerIndex);
                        if (playerIndex == activePlayer) {
                            // draw cards face up
                            for (int cardIndex = 0; cardIndex < currentPlayer.getNumOfCards(); cardIndex++) {
                                int cardSuit = currentPlayer.getCardsInHand().getCard(cardIndex).getSuit();
                                int cardRank = currentPlayer.getCardsInHand().getCard(cardIndex).getRank();

                                // selected cards must pop out
                                if (selected[cardIndex]) {
                                    g.drawImage(cardImages[cardSuit][cardRank], playerCardXCoordinate + cardIndex * cardStackingOffset, playerCardYCoordinateSelected + playerOffset * playerIndex, this);
                                } else {
                                    g.drawImage(cardImages[cardSuit][cardRank], playerCardXCoordinate + cardIndex * cardStackingOffset, playerCardYCoordinateNormal + playerOffset * playerIndex, this);
                                }
                            }
                        } else {
                            // just draw the card backs
                            for (int cardIndex = 0; cardIndex < currentPlayer.getNumOfCards(); cardIndex++) {
                                g.drawImage(cardBackImage, playerCardXCoordinate + cardIndex * cardStackingOffset, playerCardYCoordinateNormal + playerOffset * playerIndex, this);
                            }
                        }
                    }
                }
                else    {
                    // render the table contents
                    if (game.getHandsOnTable().size() != 0) {
                        Hand lastHandOnTable = game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);
                        int lengthOfLastHandOnTable = lastHandOnTable.size();
                        String player = lastHandOnTable.getPlayer().getName();
                        g.setColor(Color.WHITE);
                        g.drawString("Played by " + player, playerNameXCoordinate, playerNameYCoordinate + playerIndex * playerOffset);

                        for (int cardIndex = 0; cardIndex < lengthOfLastHandOnTable; cardIndex++) {
                            int cardSuit = lastHandOnTable.getCard(cardIndex).getSuit();
                            int cardRank = lastHandOnTable.getCard(cardIndex).getRank();

                            // all cards are rendered as 'unselected', we use Avatar X Coordinate for displaying cards
                            // to prevent empty space on the left
                            g.drawImage(cardImages[cardSuit][cardRank], playerAvatarXCoordinate + cardIndex * cardStackingOffset, playerCardYCoordinateNormal + playerOffset * playerIndex, this);
                        }
                    }
                }
                // draw the lines
                g.setColor(Color.BLACK);
                g.drawLine(lineXCoordinate, lineYCoordinate + playerOffset * playerIndex, lineXCoordinate + 750, lineYCoordinate + playerOffset * playerIndex);
            }

        }

        /**
         * This method is an implementation of the mouseClicked() method from the MouseListener interface.
         * It works to handle all mouse clicks on the main panel of the application to allow selection of
         * cards and playing the game.
         *
         * @param           mouseEvent A MouseEvent object representing the event of a mouse click
         */
        public void mouseClicked(MouseEvent mouseEvent) {

            final int cardWidth = 73;
            final int cardHeight = 97;

            // store mouse click offsets to accommodate for human inaccuracies while clicking
            int lowerBoundX;
            int upperBoundX;
            int lowerBoundY;
            int upperBoundY;

            int numberOfCards = game.getPlayerList().get(game.getCurrentIdx()).getNumOfCards();

            // we check the rightmost card first, since stacking makes rightmost card the top card in stack
            for (int checkCardClicked = numberOfCards - 1; checkCardClicked >= 0; checkCardClicked--)  {
                lowerBoundX = playerCardXCoordinate + cardStackingOffset*checkCardClicked;
                upperBoundX = lowerBoundX + cardWidth;
                lowerBoundY = selected[checkCardClicked] ? playerCardYCoordinateSelected + playerOffset * activePlayer : playerCardYCoordinateNormal + playerOffset * activePlayer;
                upperBoundY = lowerBoundY + cardHeight;

                // perform hit-box click check
                if (checkInRange(mouseEvent.getX(), lowerBoundX, upperBoundX) && checkInRange(mouseEvent.getY(), lowerBoundY, upperBoundY))  {
                        if (selected[checkCardClicked]) {
                            selected[checkCardClicked] = false;
                        } else {
                            selected[checkCardClicked] = true;
                        }
                        break;
                }

            }

            repaint();

        }

        /**
         * This method is a helper for checking whether or not a given coordinate value lies between two
         * other specified values.
         *
         * @param           checkCoordinate An integer representing the coordinate value to check
         * @param           lowerBound      An integer representing the lower bound to check against
         * @param           upperBound      An integer representing the upper bound to check against
         *
         * @return          A boolean value indicating whether or not the given coordinate lies between the
         *                  the lower and upper bounds
         */
        private boolean checkInRange(int checkCoordinate, int lowerBound, int upperBound) {
            if (checkCoordinate >= lowerBound && checkCoordinate <= upperBound) {
                return true;
            }
            return false;
        }

        /**
         * This is just a dummy override for implementing the MouseListener interface. It doesn't do anything.
         *
         * @param           mouseEvent A MouseEvent object representing the event of a mouse press
         */
        public void mousePressed(MouseEvent mouseEvent) {}

        /**
         * This is just a dummy override for implementing the MouseListener interface. It doesn't do anything.
         *
         * @param           mouseEvent A MouseEvent object representing the event of a mouse release
         */
        public void mouseReleased(MouseEvent mouseEvent) {}

        /**
         * This is just a dummy override for implementing the MouseListener interface. It doesn't do anything.
         *
         * @param           mouseEvent A MouseEvent object representing the event of a mouse enter
         */
        public void mouseEntered(MouseEvent mouseEvent) {}

        /**
         * This is just a dummy override for implementing the MouseListener interface. It doesn't do anything.
         *
         * @param           mouseEvent A MouseEvent object representing the event of a mouse exit
         */
        public void mouseExited(MouseEvent mouseEvent) {}
    }

    /**
     * This class is an inner class for BigTwoTable that implements the ActionListener interface.
     * It implements the actionPerformed() method from ActionListener to handle button-click events for
     * the 'Play' button.
     *
     * @author Anchit Mishra
     */
    private class PlayButtonListener implements ActionListener  {

        /**
         * An implementation of the actionPerformed() method from the ActionListener interface. It handles
         * button-click events for the 'Play' button by calling the makeMove() method on the CardGame object.
         *
         * @param          event The ActionEvent that occurs when the method is invoked
         */
        public void actionPerformed(ActionEvent event) {
            if (getSelected() == null || getSelected().length == 0 || activePlayer != game.getCurrentIdx()) {
                return;
            }
            game.makeMove(activePlayer, getSelected());
            repaint();
        }

    }

    /**
     * This class is an inner class for BigTwoTable that implements the ActionListener interface.
     * It implements the actionPerformed() method from ActionListener to handle button-click events for
     * the 'Pass' button.
     *
     * @author Anchit Mishra
     */
    private class PassButtonListener implements ActionListener  {

        /**
         * An implementation of the actionPerformed() method from the ActionListener interface. It handles
         * button-click events for the 'Pass' button by calling the makeMove() method on the CardGame object.
         *
         * @param          event The ActionEvent that occurs when the method is invoked
         */
        public void actionPerformed(ActionEvent event)  {

            // if the player is trying to move out of turn, we just ignore the input
            if (activePlayer != game.getCurrentIdx())   {
                return;
            }

            // first, we clear all selections, if any were made
            for (int i = 0; i < selected.length; i++)   {
                selected[i] = false;
            }

            // next, we make the pass move
            game.makeMove(activePlayer, getSelected());
            repaint();
        }

    }

    /**
     * This class is an inner class for BigTwoTable that implements the ActionListener interface.
     * It implements the actionPerformed() method from ActionListener to handle menu-item-click events for the
     * 'Restart' menu item.
     *
     * @author Anchit Mishra
     */
    private class ConnectMenuItemListener implements ActionListener {

        /**
         * An implementation of the actionPerformed() method from the ActionListener interface. It handles
         * menu-item-click events for the 'Restart' menu item.
         *
         * @param          event The ActionEvent that occurs when the method is invoked
         */
        public void actionPerformed(ActionEvent event)  {
            // connect to the game server
            if (game.isConnected()) {
                return;
            }
            for (int i = 0; i < game.getPlayerList().size(); i++)   {
                game.getPlayerList().get(i).removeAllCards();
            }
            game.makeConnection();
            repaint();
        }

    }

    /**
     * This class is an inner class for BigTwoTable that implements the ActionListener interface.
     * It implements the actionPerformed() method from ActionListener to handle menu-item-click events for the
     * 'Quit' menu item.
     *
     * @author Anchit Mishra
     */
    private class QuitMenuItemListener implements ActionListener    {

        /**
         * An implementation of the actionPerformed() method from the ActionListener interface. It handles
         * menu-item-click events for the 'Quit' menu item.
         *
         * @param          event The ActionEvent that occurs when the method is invoked
         */
        public void actionPerformed(ActionEvent event)  {
            // simply terminate the application with status 0 to indicate successful exit
            System.exit(0);
        }

    }

    /**
     * This class is an inner class for BigTwoTable that implements the ActionListener interface.
     * It implements the actionPerformed() method from ActionListener to handle message sends using 'Enter' presses
     * from the user.
     */
    private class SendMessageListener implements ActionListener {

        /**
         * An implementation of the actionPerformed() method from the ActionListener interface. It handles
         * message sends using 'Enter' presses from the user.
         *
         * @param           event The ActionEvent that occurs when the method is invoked
         */
        public void actionPerformed(ActionEvent event)  {
            String chatMessage = chatMsgField.getText();
            if (chatMessage.isEmpty() || chatMessage.isBlank()) {
                return;
            }
            int playerID = game.getPlayerID();
            int messageType = CardGameMessage.MSG;
            game.sendMessage(new CardGameMessage(messageType, playerID, chatMessage));
            chatMsgField.setText("");
            repaint();
        }

    }




}