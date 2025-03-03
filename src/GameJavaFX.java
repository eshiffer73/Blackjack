import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GameJavaFX {
   public void hit(Player player, Deck deck, int index) {
      player.hands.get(index).addCard(deck.dealCard());
   }

   public void stay(Player player, Player dealer, Deck deck) {
      while (dealer.hands.get(0).getValueOfHand() < 17) {
          dealer.hands.get(0).addCard(deck.dealCard());
          if (dealer.hands.get(0).getValueOfHand() == 17 && dealer.hands.get(0).hasAce()) {
              dealer.hands.get(0).addCard(deck.dealCard());
          }
      }
   }

   public void startRound(Player player, Player dealer, Deck deck) {
      deck = new Deck();
      deck.shuffleDeck();
      player.hands = new ArrayList<>();
      player.hands.add(new Hand());
      dealer.hands = new ArrayList<>();
      dealer.hands.add(new Hand());

      player.hands.get(0).addCard(deck.dealCard());
      dealer.hands.get(0).addCard(deck.dealCard());
      player.hands.get(0).addCard(deck.dealCard());
      dealer.hands.get(0).addCard(deck.dealCard());
   }

   public void doubleDown(Player player, Deck deck, int index, int bet) {
      player.hands.get(index).addCard(deck.dealCard());
      player.hands.get(index).doubledHand();
   }

   public void split(Player player, Deck deck, int bet, int index) {
      player.hands.add(new Hand());
      Card card = player.hands.get(index).removeCard();
      player.hands.get(player.hands.size() - 1).addCard(card);

      player.hands.get(index).addCard(deck.dealCard());
      player.hands.get(player.hands.size() - 1).addCard(deck.dealCard());
   }
}