import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> hand;
    private boolean finished;
    public boolean doubled;

    public Hand() {
        this.hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void viewHand() {
        System.out.println(hand.toString());
    }

    public String toString() {
        return hand.toString();
    }

    public Card getFirstCard() {
        if (hand.get(0) == null) {
            return null;
        }
        else {
            return hand.get(0);
        }
    }

    public Card getLastCard() {
        return hand.get(hand.size() - 1);
    }

    public Card removeCard() {
        return hand.remove(0);
    }

    public int size() {
        return hand.size();
    }

    public boolean isFinished() {
        return finished;
    }

    public void doubledHand() {
        this.doubled = true;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isSplittable() {
        if (hand.size() == 2) {
            Card first = hand.get(0);
            Card second = hand.get(1);

            return first.getRank() == second.getRank();
        }
        return false;
    }

    public int getValueOfHand() {
        int value = 0;
        for (Card card : hand) {
            value += card.getRank().getValue();
        }

        if (value > 21) {
            for (Card card : hand) {
                if (card.getRank() == Rank.ACE) {
                    value -= 10;
                    if (value <= 21) {
                        break;
                    }
                }
            }
        }
        return value;
    }

    public boolean hasAce() {
        for (Card card : hand) {
            if (card.getRank() == Rank.ACE) {
                return true;
            }
        }
        return false;
    }

    public List<Card> getHand() {
        return hand;
    }
}
