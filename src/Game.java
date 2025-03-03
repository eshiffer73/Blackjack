import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/*
 * This class is the main logic behind the game of blackjack. It contains the main game loop and scoring for rounds.
 */
public class Game {
    private final Scanner scan = new Scanner(System.in);

    // Starts the game and loops while the player wants to play again
    public void startGame() {
        System.out.println("Welcome to Blackjack!\nYou will begin with $500.");
        pause(1);
        // Creates the player and dealer
        Player player = new Player(getName());
        pause(1);
        Player dealer = new Player("Dealer");

        System.out.println("Good luck " + player.getName() + "!");

        pause(1);

        // Starts the first round
        startRound(player, dealer);

        // Ends the game if the player runs out of chips
        if (player.getChips() == 0) {
            System.out.println("You ran out of chips!");
            pause(1);
            System.out.println("Goodbye " + player.getName());
            return;
        }

        System.out.println("You now have $" + player.getChips());

        pause(1);

        System.out.println("Would you like to play again? (Y or N)");
        String ans = scan.next();

        // Loops while the player wants to play again
        while (ans.equalsIgnoreCase("Y")) {
            startRound(player, dealer);

            pause(1);

            System.out.println("You now have $" + player.getChips());

            pause(1);

            if (player.getChips() == 0) {
                System.out.println("You ran out of chips!");
                pause(1);
                break;
            }

            System.out.println("Would you like to play again? (Y or N)");
            ans = scan.next();
        }

        System.out.println("Goodbye " + player.getName());
    }

    // Starts a round of blackjack
    public void startRound(Player player, Player dealer) {
        System.out.println("How much would you like to bet?");
        int bet = scan.nextInt();

        // Makes sure the player bets a valid amount
        while (!player.bet(bet)) {
            System.out.println("Not enough chips!");
            System.out.println("How much would you like to bet?");
            bet = scan.nextInt();
        }

        // Shuffles the deck
        Deck deck = new Deck();
        deck.shuffleDeck();

        player.hands = new ArrayList<>();
        player.hands.add(new Hand());

        dealer.hands = new ArrayList<>();
        dealer.hands.add(new Hand());

        // Deals the initial cards
        player.hands.get(0).addCard(deck.dealCard());
        dealer.hands.get(0).addCard(deck.dealCard());
        player.hands.get(0).addCard(deck.dealCard());
        dealer.hands.get(0).addCard(deck.dealCard());

        // Shows the dealers one card
        System.out.println("Dealer's hand: " + dealer.hands.get(0).getFirstCard());
        pause(1);

        // Determines if the player and dealer have blackjack
        if (dealer.hands.get(0).getValueOfHand() == 21 && player.hands.get(0).getValueOfHand() == 21) {
            System.out.println("Both have blackjack! It is a push!");
            player.addChips(bet);
        }
        // Determines if the dealer has blackjack
        if (dealer.hands.get(0).getValueOfHand() == 21) {
            dealer.viewHand(0);
            System.out.println("Dealer has blackjack. You lose!");
        }
        // Determines if the player has blackjack
        else if (player.hands.get(0).getValueOfHand() == 21) {
            System.out.println("Blackjack! You win!!!");
            player.addChips(bet * 2);
        }
        // If neither has blackjack, the game starts
        else {
            roundAction(player, dealer, deck, bet);
        }

    }

    // Adds a new hand to the player if they want to split
    public void split(Player player, Deck deck, int index) {
        player.hands.add(new Hand());
        Card card = player.hands.get(index).removeCard();
        player.hands.get(player.hands.size() - 1).addCard(card);

        player.hands.get(index).addCard(deck.dealCard());
        player.hands.get(player.hands.size() - 1).addCard(deck.dealCard());
    }

    // Determines the winner of each hand
    public void determineWinningHands(Player player, Player dealer, int bet) {
        // Loops through each of the players hands to determine the winner of each
        for (int i = 0; i < player.hands.size(); i++) {
            // Player busted
            if (player.hands.get(i).getValueOfHand() > 21) {
                System.out.println("Hand " + (i + 1) + " busted.");
            }
            // Dealer busted
            else if (dealer.hands.get(0).getValueOfHand() > 21) {
                System.out.println("Hand " + (i + 1) + " won!");
                // Determines if the player decided to double down
                if (player.hands.get(i).doubled) {
                    player.addChips(bet * 4);
                }
                else {
                    player.addChips(bet * 2);
                }
            }
            // Player wins
            else if (player.hands.get(i).getValueOfHand() > dealer.hands.get(0).getValueOfHand()) {
                System.out.println("Hand " + (i + 1) + " won!");
                // Determines if the player decided to double down
                if (player.hands.get(i).doubled) {
                    player.addChips(bet * 4);
                }
                else {
                    player.addChips(bet * 2);
                }
            }
            // Push
            else if (player.hands.get(i).getValueOfHand() == dealer.hands.get(0).getValueOfHand()) {
                if (player.hands.get(i).doubled) {
                    player.addChips(bet * 2);
                } else {
                    player.addChips(bet);
                }
                System.out.println("Hand " + (i + 1) + " pushed!");
            }
            else {
                System.out.println("Hand " + (i + 1) + " lost.");
            }
        }
    }

    // Plays through a round of blackjack
    public void roundAction(Player player, Player dealer, Deck deck, int bet) {
        // Loops through each of the players hands
        for (int i = 0; i < player.hands.size(); i++) {
            playHand(player, deck, bet, i);
        }
        // Dealer plays
        dealerAction(dealer, deck);
        // Determines the winner of each hand
        determineWinningHands(player, dealer, bet);
    }

    // Plays a single hand
    public void playHand(Player player, Deck deck, int bet, int index) {
        // Loops until the player decides to stay or busts
        while (true) {
            player.viewHand(index);
            // Allows the player to split if they'd like and have enough chips
            if (player.hands.get(index).isSplittable() && player.getChips() >= bet) {
                System.out.println("Would you like to split? (Y or N)");
                String ans = scan.next();

                // Splits the players hand
                if (ans.equalsIgnoreCase("Y")) {
                    player.bet(bet);
                    split(player, deck, index);
                    player.viewHand(index);
                }
            }

            System.out.println("Would you like to hit or stay or double down? (H or S or D)");
            String ans = scan.next();

            // Player hits
            if (ans.equalsIgnoreCase("H")) {
                player.hands.get(index).addCard(deck.dealCard());
                pause(1);

                if (player.hands.get(index).getValueOfHand() > 21) {
                    player.viewHand(index);
                    System.out.println("You busted!");
                    break;
                } else if (player.hands.get(index).getValueOfHand() == 21) {
                    player.viewHand(index);
                    System.out.println("21!");
                    pause(1);
                    player.hands.get(index).setFinished(true);
                    break;
                }
                // Player doubles down
            } else if (ans.equalsIgnoreCase("D")){
                if (player.getChips() < bet) {
                    System.out.println("You don't have enough chips to double!");
                    continue;
                }
                player.bet(bet);
                player.hands.get(index).addCard(deck.dealCard());
                player.viewHand(index);
                player.hands.get(index).doubledHand();
                break;
            }
            // Player stays
            else {
                player.hands.get(index).setFinished(true);
                break;
            }
        }
    }

    // Dealer plays through their hand
    public void dealerAction(Player dealer, Deck deck) {
        dealer.viewHand(0);
        pause(1);

        // Dealer plays until they reach 17 or higher. They hit on a soft 17
        while (dealer.hands.get(0).getValueOfHand() < 17) {
            dealer.hands.get(0).addCard(deck.dealCard());
            dealer.viewHand(0);
            pause(1);
            // Soft 17 so the dealer has to hit
            if (dealer.hands.get(0).getValueOfHand() == 17 && dealer.hands.get(0).hasAce()) {
                dealer.hands.get(0).addCard(deck.dealCard());
                dealer.viewHand(0);
                pause(1);
            }
            // Dealer busts
            if (dealer.hands.get(0).getValueOfHand() > 21) {
                System.out.println("Dealer busted!");
                pause(1);
                return;
            }
        }
    }

    // Asks the player for their name
    public String getName() {
        System.out.println("What is your name?");
        return scan.nextLine();
    }

    // Allows for a delay between text outputs in the console
    public void pause(int secs) {
        try {
            TimeUnit.SECONDS.sleep(secs);
        }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}