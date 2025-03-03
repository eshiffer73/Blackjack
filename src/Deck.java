import java.util.ArrayList;
import java.util.List;

// This class represents a deck of cards
public class Deck {
    private final List<Card> cards = new ArrayList<>();

    // Creates a deck of 52 cards
    public Deck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    // Deals a card from the deck
    public Card dealCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }

    // Shuffles the deck
    public void shuffleDeck() {
        for (int i = 0; i < 1000; i++) {
            int first = (int) (Math.random() * 52);
            int second = (int) (Math.random() * 52);

            Card temp = cards.get(first);
            cards.set(first, cards.get(second));
            cards.set(second, temp);
        }
    }
}
