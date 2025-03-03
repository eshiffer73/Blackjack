import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Game {
    private final Scanner scan = new Scanner(System.in);
    public void startGame() {
        System.out.println("Welcome to Blackjack!\nYou will begin with $500.");
        pause(1);
        Player player = new Player(getName(), false);
        pause(1);
        Player dealer = new Player("Dealer", true);

        System.out.println("Good luck " + player.getName() + "!");

        pause(1);

        startRound(player, dealer);

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

        while (ans.equalsIgnoreCase("Y")) {
            if (player.getChips() == 0) {
                System.out.println("You ran out of chips!");
                pause(1);
                break;
            }
            startRound(player, dealer);

            pause(1);

            System.out.println("You now have $" + player.getChips());

            pause(1);

            System.out.println("Would you like to play again? (Y or N)");
            ans = scan.next();
        }

        System.out.println("Goodbye " + player.getName());
    }

    public void startRound(Player player, Player dealer) {
        System.out.println("How much would you like to bet?");
        int bet = scan.nextInt();

        while (!player.bet(bet)) {
            System.out.println("Not enough chips!");
            System.out.println("How much would you like to bet?");
            bet = scan.nextInt();
        }

        Deck deck = new Deck();
        deck.shuffleDeck();

        player.hands = new ArrayList<>();
        player.hands.add(new Hand());

        dealer.hands = new ArrayList<>();
        dealer.hands.add(new Hand());

        player.hands.get(0).addCard(deck.dealCard());
        dealer.hands.get(0).addCard(deck.dealCard());
        player.hands.get(0).addCard(deck.dealCard());
        dealer.hands.get(0).addCard(deck.dealCard());

        System.out.println("Dealer's hand: " + dealer.hands.get(0).getFirstCard());
        pause(1);

        if (dealer.hands.get(0).getValueOfHand() == 21 && player.hands.get(0).getValueOfHand() == 21) {
            System.out.println("Both have blackjack! It is a push!");
            player.addChips(bet);
        }
        if (dealer.hands.get(0).getValueOfHand() == 21) {
            dealer.viewHand(0);
            System.out.println("Dealer has blackjack. You lose!");
        }
        else if (player.hands.get(0).getValueOfHand() == 21) {
            System.out.println("Blackjack! You win!!!");
            player.addChips(bet * 2);
        }
        else {
            roundAction(player, dealer, deck, bet);
        }

    }

    public void split(Player player, Deck deck, int bet, int index) {
        player.hands.add(new Hand());
        Card card = player.hands.get(index).removeCard();
        player.hands.get(player.hands.size() - 1).addCard(card);

        player.hands.get(index).addCard(deck.dealCard());
        player.hands.get(player.hands.size() - 1).addCard(deck.dealCard());
    }

    public void determineWinningHands(Player player, Player dealer, int bet) {
        for (int i = 0; i < player.hands.size(); i++) {
            if (player.hands.get(i).getValueOfHand() > 21) {
                System.out.println("Hand " + (i + 1) + " lost.");
                continue;
            }
            else if (dealer.hands.get(0).getValueOfHand() > 21) {
                System.out.println("Hand " + (i + 1) + " won!");
                if (player.hands.get(i).doubled) {
                    player.addChips(bet * 4);
                }
                else {
                    player.addChips(bet * 2);
                }
            }
            else if (player.hands.get(i).getValueOfHand() > dealer.hands.get(0).getValueOfHand()) {
                System.out.println("Hand " + (i + 1) + " won!");
                if (player.hands.get(i).doubled) {
                    player.addChips(bet * 4);
                }
                else {
                    player.addChips(bet * 2);
                }
            }
            else if (player.hands.get(i).getValueOfHand() == dealer.hands.get(0).getValueOfHand()) {
                player.addChips(bet);
                System.out.println("Hand " + (i + 1) + " pushed!");
            }
            else {
                System.out.println("Hand " + (i + 1) + " lost.");
            }
        }
    }

    public void roundAction(Player player, Player dealer, Deck deck, int bet) {
        for (int i = 0; i < player.hands.size(); i++) {
            playHand(player, deck, bet, i);
        }
        dealerAction(dealer, deck);
        determineWinningHands(player, dealer, bet);
    }

    public void playHand(Player player, Deck deck, int bet, int index) {
        while (true) {
            player.viewHand(index);
            if (player.hands.get(index).isSplittable() && player.getChips() >= bet) {
                System.out.println("Would you like to split? (Y or N)");
                String ans = scan.next();

                if (ans.equalsIgnoreCase("Y")) {
                    player.bet(bet);
                    split(player, deck, bet, index);
                    player.viewHand(index);
                }
            }

            System.out.println("Would you like to hit or stay or double down? (H or S or D)");
            String ans = scan.next();

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
            } else {
                player.hands.get(index).setFinished(true);
                break;
            }
        }
    }

    public void dealerAction(Player dealer, Deck deck) {
        dealer.viewHand(0);
        pause(1);

        while (dealer.hands.get(0).getValueOfHand() < 17) {
            dealer.hands.get(0).addCard(deck.dealCard());
            dealer.viewHand(0);
            pause(1);
            if (dealer.hands.get(0).getValueOfHand() == 17 && dealer.hands.get(0).hasAce()) {
                dealer.hands.get(0).addCard(deck.dealCard());
                dealer.viewHand(0);
                pause(1);
            }
            if (dealer.hands.get(0).getValueOfHand() > 21) {
                System.out.println("Dealer busted!");
                pause(1);
                return;
            }
        }
    }

    public String getName() {
        System.out.println("What is your name?");
        return scan.nextLine();
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