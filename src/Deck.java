import java.util.ArrayList;
import java.util.List;


public class Deck {
    private final List<Card> cards = new ArrayList<>();

    public Deck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public List<Card> getCards() {
        return cards;
    }

    public Card dealCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }

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
