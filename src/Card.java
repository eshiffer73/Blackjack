// This class represents a single card in the game of blackjack
public class Card {
    private final Suit suit;
    private final Rank rank;
    public Card (Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
