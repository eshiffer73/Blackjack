import java.util.ArrayList;

public class Player {
    private int chips;
    private final String name;

    private final boolean isDealer;

    public ArrayList<Hand> hands;

    public Player (String name, boolean isDealer) {
        chips = 500;
        this.name = name;
        this.isDealer = isDealer;
        this.hands = new ArrayList<>();
    }

    public int getChips() {
        return chips;
    }

//    public void addCard(Card card) {
//        this.hand.addCard(card);
//    }

    public void viewHand(int index) {
        System.out.println(name + "'s hand " + (index + 1) + ": " + hands.get(index));
    }

    public String handDetails(int index) {
        return name + "'s hand: " + hands.get(index);
    }

    public void addChips(int add) {
        chips += add;
    }

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

    public boolean isDealer() {
        return isDealer;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    public String toString() {
        return name + ": $" + chips;
    }

    public String getName() {
        return name;
    }

    public boolean allFinished() {
        for (Hand hand : hands) {
            if (!hand.isFinished()) {
                return false;
            }
        }
        return true;
    }
}
