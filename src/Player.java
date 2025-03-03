import java.util.ArrayList;

// This class represents a player in a game of blackjack
public class Player {
    private int chips;
    private final String name;

    public ArrayList<Hand> hands;

    // Starts the player with 500 chips and sets their name
    public Player (String name) {
        chips = 500;
        this.name = name;
        this.hands = new ArrayList<>();
    }

    // Returns the number of chips the player has
    public int getChips() {
        return chips;
    }

    // Prints the player's hand
    public void viewHand(int index) {
        System.out.println(name + "'s hand " + (index + 1) + ": " + hands.get(index));
    }

    // Returns the details of the player's hand
    public String handDetails(int index) {
        return name + "'s hand: " + hands.get(index);
    }

    // Adds chips to the player
    public void addChips(int add) {
        chips += add;
    }

    // Places a bet
    public boolean bet(int bet) {
        if (bet <= 0) {
            System.out.println("Must place a bet");
            return false;
        }
        else if (chips - bet >= 0) {
            chips = chips - bet;
            return true;
        }

        return false;
    }

    public String toString() {
        return name + ": $" + chips;
    }

    // Returns the name of the player
    public String getName() {
        return name;
    }
}
