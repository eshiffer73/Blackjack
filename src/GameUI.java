import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class GameUI {
    private JFrame frame;
    private JPanel dealerPanel, playerPanel, controlPanel, betPanel;
    private JLabel dealerLabel, playerLabel, messageLabel, chipLabel;
    private JButton hitButton, stayButton, splitButton, doubleDownButton, betButton, newRoundButton;
    private JTextField betInput;

    private Player player = new Player("Player", false);
    private Player dealer = new Player("Dealer", true);
    private Deck deck;
    private int bet;
    private int currentHandIndex = 0;

    public GameUI() {
        setupGUI();
    }

    public void setupGUI() {
        frame = new JFrame("Blackjack");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Panels
        dealerPanel = new JPanel();
        playerPanel = new JPanel();
        controlPanel = new JPanel();
        betPanel = new JPanel();

        // Labels
        dealerLabel = new JLabel("Dealer's Hand:");
        playerLabel = new JLabel("Your Hand:");
        messageLabel = new JLabel("Welcome to Blackjack!");
        chipLabel = new JLabel("Chips: $" + player.getChips());

        // Betting Input
        betInput = new JTextField(5);
        betButton = new JButton("Place Bet");

        // Buttons
        hitButton = new JButton("Hit");
        stayButton = new JButton("Stay");
        splitButton = new JButton("Split");
        doubleDownButton = new JButton("Double Down");
        newRoundButton = new JButton("New Round");

        // Action Listeners
        betButton.addActionListener(e -> placeBet());
        hitButton.addActionListener(e -> playerHit());
        stayButton.addActionListener(e -> playerStay());
        splitButton.addActionListener(e -> playerSplit());
        doubleDownButton.addActionListener(e -> playerDoubleDown());
        newRoundButton.addActionListener(e -> newGame());

        // Adding Components
        betPanel.add(new JLabel("Bet Amount:"));
        betPanel.add(betInput);
        betPanel.add(betButton);
        betPanel.add(chipLabel);

        dealerPanel.add(dealerLabel);
        playerPanel.add(playerLabel);
        controlPanel.add(hitButton);
        controlPanel.add(stayButton);
        controlPanel.add(splitButton);
        controlPanel.add(doubleDownButton);
        controlPanel.add(messageLabel);

        frame.add(dealerPanel, BorderLayout.NORTH);
        frame.add(playerPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.add(betPanel, BorderLayout.WEST);

        // Disable action buttons until a bet is placed
        toggleGameControls(false);

        frame.setVisible(true);

        startGame();
    }

    public void newGame() {
        betButton.setEnabled(true);
        betInput.setEnabled(true);
        startRound();
    }
    public void startGame() {
        messageLabel.setText("You start with $500.");
    }

    public void placeBet() {
        try {
            bet = Integer.parseInt(betInput.getText());
            if (!player.bet(bet)) {
                messageLabel.setText("Invalid bet. Enter a valid amount.");
                return;
            }

            messageLabel.setText("Bet placed: $" + bet);
            chipLabel.setText("Chips: $" + player.getChips());

            // Enable game controls and start the round
            toggleGameControls(true);
            betButton.setEnabled(false);
            betInput.setEnabled(false);
            startRound();

        } catch (NumberFormatException e) {
            messageLabel.setText("Enter a valid number.");
        }
    }

    public void startRound() {
        toggleGameControls(true);
        deck = new Deck();
        deck.shuffleDeck();

        player.hands = new ArrayList<>();
        player.hands.add(new Hand());

        dealer.hands = new ArrayList<>();
        dealer.hands.add(new Hand());

        //player.hands.get(0).addCard(deck.dealCard());
        player.hands.get(0).addCard(new Card(Suit.CLUBS, Rank.FOUR));
        dealer.hands.get(0).addCard(deck.dealCard());
        //player.hands.get(0).addCard(deck.dealCard());
        player.hands.get(0).addCard(new Card(Suit.DIAMONDS, Rank.FOUR));
        dealer.hands.get(0).addCard(deck.dealCard());

        updateUI();
    }

    private void updateUI() {
        dealerLabel.setText("Dealer's Hand: " + dealer.hands.get(0).getFirstCard());
        playerLabel.setText("Your Hand: " + player.hands.get(0));
        if (player.hands.get(0).getValueOfHand() == 21) {
            messageLabel.setText("Blackjack! You win!");
            player.addChips(bet * 2);
            endRound();
        }
    }

    private void dealerUpdateUI() {
        dealerLabel.setText(dealer.handDetails(0));
        playerLabel.setText("Your Hand: " + player.hands.get(currentHandIndex));

        if (player.hands.get(0).getValueOfHand() == 21) {
            messageLabel.setText("Blackjack! You win!");
            endRound();
        }
    }

    private void playerHit() {
        player.hands.get(currentHandIndex).addCard(deck.dealCard());
        updateUI();
        if (player.hands.get(currentHandIndex).getValueOfHand() > 21) {
            messageLabel.setText("Hand " + (currentHandIndex + 1) + " busted!");
            if (currentHandIndex < player.hands.size() - 1) {
                currentHandIndex++;
                messageLabel.setText("Playing next hand...");
                playerLabel.setText("Your Hand: " + player.hands.get(currentHandIndex));
            }
            else {
                endRound();
            }
        }
    }

    private void playerStay() {
        if (currentHandIndex < player.hands.size() - 1) {
            currentHandIndex++; // Move to the next hand
            playerLabel.setText("Your Hand: " + player.hands.get(currentHandIndex));
            messageLabel.setText("Playing next hand...");
        } else {
            dealerAction(); // Dealer plays after all hands are finished
        }
    }

    private void playerSplit() {
        if (player.hands.get(currentHandIndex).isSplittable() && player.getChips() >= bet) {
            player.bet(bet);

            // Create the new hand from the split
            Hand newHand = new Hand();
            newHand.addCard(player.hands.get(currentHandIndex).removeCard());
            newHand.addCard(deck.dealCard());
            player.hands.add(newHand);

            // Add a new card to the original hand
            player.hands.get(currentHandIndex).addCard(deck.dealCard());

            messageLabel.setText("Hand split! Play each hand separately.");
            updateUI();
        } else {
            messageLabel.setText("You cannot split this hand.");
        }
    }

    private void playerDoubleDown() {
        if (player.getChips() >= bet) {
            player.bet(bet);
            player.hands.get(0).addCard(deck.dealCard());
            player.hands.get(0).doubledHand();
            messageLabel.setText("Double down! One more card.");
            playerStay();
        } else {
            messageLabel.setText("Not enough chips to double down.");
        }
    }

    private void dealerAction() {
        messageLabel.setText("Dealer's turn...");
        dealerUpdateUI();
        while (dealer.hands.get(0).getValueOfHand() < 17) {
            dealer.hands.get(0).addCard(deck.dealCard());
            dealerUpdateUI();
            if (dealer.hands.get(0).getValueOfHand() == 17 && dealer.hands.get(0).hasAce()) {
                dealer.hands.get(0).addCard(deck.dealCard());
                dealerUpdateUI();
            }
            if (dealer.hands.get(0).getValueOfHand() > 21) {
                break;
            }
        }
        dealerUpdateUI();
        determineWinningHands();
    }

    private void determineWinningHands() {
        int dealerValue = dealer.hands.get(0).getValueOfHand();
        int playerValue = player.hands.get(0).getValueOfHand();

        if (playerValue > 21) {
            messageLabel.setText("You busted! Dealer wins.");
        } else if (dealerValue > 21 || playerValue > dealerValue) {
            messageLabel.setText("You win!");
            if (player.hands.get(0).doubled) {
                player.addChips(bet * 4);
            }
            else {
                player.addChips(bet * 2);
            }
        } else if (playerValue == dealerValue) {
            messageLabel.setText("It's a push.");
            player.addChips(bet);
        } else {
            messageLabel.setText("Dealer wins.");
        }

        endRound();
    }

    private void winningHands() {
        int dealerValue = dealer.hands.get(0).getValueOfHand();

        for (int i = 0; i < player.hands.size(); i++) {
            int playerValue = player.hands.get(i).getValueOfHand();
            if (playerValue > 21) {
                messageLabel.setText("You busted! Dealer wins.");
                pause(1);
            } else if (dealerValue > 21 || playerValue > dealerValue) {
                messageLabel.setText("Hand " + (i + 1) + " wins!");
                pause(1);
                if (player.hands.get(0).doubled) {
                    player.addChips(bet * 4);
                }
                else {
                    player.addChips(bet * 2);
                }
            } else if (playerValue == dealerValue) {
                messageLabel.setText("It's a push.");
                pause(1);
                player.addChips(bet);
            } else {
                messageLabel.setText("Dealer wins.");
                pause(1);
            }
        }
        endRound();
    }

    private void endRound() {
        toggleGameControls(false);
        chipLabel.setText("Chips: $" + player.getChips());
        currentHandIndex = 0;

        betButton.setEnabled(true);
        betInput.setEnabled(true);
        frame.revalidate();
        frame.repaint();
    }

    private void toggleGameControls(boolean enable) {
        hitButton.setEnabled(enable);
        stayButton.setEnabled(enable);
        splitButton.setEnabled(enable);
        doubleDownButton.setEnabled(enable);
    }

    private String getCardImage(Card card) {
        return "cards/" + card.getRank().getValue() + "_of_" + card.getSuit().toString().toLowerCase() + ".png";
    }

    public void pause(int secs) {
        try {
            TimeUnit.SECONDS.sleep(secs);
        }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
