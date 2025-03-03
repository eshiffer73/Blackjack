import java.util.ArrayList;
import java.util.List;

// This class represents a player's hand during a game of blackjack
public class Hand {
    private final List<Card> hand;
    private boolean finished;
    public boolean doubled;

    public Hand() {
        this.hand = new ArrayList<>();
    }

    // Adds a card to the hand
    public void addCard(Card card) {
        hand.add(card);
    }

    // Returns a string representation of the hand
    public String toString() {
        return hand.toString();
    }

    // Returns the first card in the hand (Used for the dealer)
    public Card getFirstCard() {
        if (hand.get(0) == null) {
            return null;
        }
        else {
            return hand.get(0);
        }
    }

    // Returns the last card in the hand
    public Card getLastCard() {
        return hand.get(hand.size() - 1);
    }

    // Removes the first card in the hand
    public Card removeCard() {
        return hand.remove(0);
    }

    // Returns the number of cards in the hand
    public int size() {
        return hand.size();
    }

    // Returns true if the hand is finished
    public boolean isFinished() {
        return finished;
    }

    // Returns true if the hand has been doubled
    public void doubledHand() {
        this.doubled = true;
    }

    // Sets the finished variable
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    // Returns true if the hand can be split
    public boolean isSplittable() {
        if (hand.size() == 2) {
            Card first = hand.get(0);
            Card second = hand.get(1);

            return first.getRank() == second.getRank();
        }
        return false;
    }

    // Returns the value of the hand
    public int getValueOfHand() {
        int value = 0;
        for (Card card : hand) {
            value += card.getRank().getValue();
        }

        // Determines if an ace is worth 1 or 11
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

    // Returns true if the hand contains an ace
    public boolean hasAce() {
        for (Card card : hand) {
            if (card.getRank() == Rank.ACE) {
                return true;
            }
        }
        return false;
    }

    // Returns the hand
    public List<Card> getHand() {
        return hand;
    }
}
