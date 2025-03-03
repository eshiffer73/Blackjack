import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class BlackjackGUI extends Application {
    private GameJavaFX game;
    private Player player;
    private Player dealer;
    private Deck deck;
    private HBox playerCardArea;
    private HBox dealerCardArea;
    private HBox controls;
    private ArrayList<ImageView> playerCardViews;
    private ArrayList<ImageView> dealerCardViews;

    @Override
    public void start(Stage primaryStage) {
        game = new GameJavaFX(); // Use existing game logic
        deck = new Deck();
        deck.shuffleDeck();
        player = new Player("Player");
        dealer = new Player("Dealer");
        game.startRound(player, dealer, deck);

        playerCardViews = new ArrayList<>();
        dealerCardViews = new ArrayList<>();

        for (Card card : player.hands.get(0).getHand()) {
            addCardToPlayer(card);
        }

        if (dealer.hands.get(0).getValueOfHand() == 21) {
            for (Card card : dealer.hands.get(0).getHand()) {
                addCardToDealer(card);
            }
        } else {

            addCardToDealer(dealer.hands.get(0).getFirstCard());
            String cardFile = "cards/card_back.png";
            ImageView cardView = new ImageView(new Image(cardFile));
            cardView.setFitWidth(100);
            cardView.setFitHeight(150);
            dealerCardViews.add(cardView);
        }

        BorderPane root = new BorderPane();
        playerCardArea = new HBox(10); // Area to display player's cards
        dealerCardArea = new HBox(10); // Area to display dealer's cards
        controls = new HBox(10); // Buttons for player actions

        updateCardDisplay();

        // Buttons for actions
        Button hitButton = new Button("Hit");
        Button stayButton = new Button("Stay");
        Button doubleDownButton = new Button("Double Down");
        Button splitButton = new Button("Split");

        controls.getChildren().addAll(hitButton, stayButton, doubleDownButton, splitButton);

        // Add to layout
        root.setCenter(playerCardArea);
        root.setTop(dealerCardArea);
        root.setBottom(controls);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Blackjack");
        primaryStage.setScene(scene);
        primaryStage.show();

        // TODO: Connect buttons to game logic
          hitButton.setOnAction(e -> handleHit());
          stayButton.setOnAction(e -> handleStay());
          doubleDownButton.setOnAction(e -> handleDoubleDown());
//        splitButton.setOnAction(e -> handleSplit());
    }

    private void updateCardDisplay() {
        // Update the player and dealer's cards in the GUI
        playerCardArea.getChildren().clear();
        dealerCardArea.getChildren().clear();
        for (ImageView cardView : playerCardViews) {
            playerCardArea.getChildren().add(cardView);
        }
        for (ImageView cardView : dealerCardViews) {
            dealerCardArea.getChildren().add(cardView);
        }
    }

    private void handleHit() {
        game.hit(player, deck, 0); // Call the game logic to hit
        addCardToPlayer(player.hands.get(0).getLastCard());
        updateCardDisplay();

        if (player.hands.get(0).getValueOfHand() > 21) {
            dealerCardViews.clear();
            for (Card card : dealer.hands.get(0).getHand()) {
                addCardToDealer(card);
            }
            updateCardDisplay();
        }
    }

    private void handleStay() {
        game.stay(player, dealer, deck); // Call the game logic for stay
        dealerCardViews.clear();
        Timeline timeline = new Timeline();
        for (Card card : dealer.hands.get(0).getHand()) {
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), e -> {
                addCardToDealer(card);
                updateCardDisplay();
            });
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.play();
    }

    private void handleDoubleDown() {
        game.doubleDown(player, deck, 0, 50); // Call the game logic for double down
        addCardToPlayer(player.hands.get(0).getLastCard());
        updateCardDisplay();
        handleStay();
    }
//
//    private void handleSplit() {
//        game.split(player); // Call the game logic for split
//        updateCardDisplay();
//    }

    // Additional methods to update the visual display of cards
    private void addCardToPlayer(Card card) {
        String cardFile = getCardFile(card);
        ImageView cardView = new ImageView(new Image(cardFile));
        cardView.setFitWidth(100);
        cardView.setFitHeight(150);
        playerCardViews.add(cardView);
    }

    private String getCardFile(Card card) {
        String cardFile;
        if (card.getRank() == Rank.ACE) {
            cardFile = "cards/ace_of_" + card.getSuit().toString().toLowerCase() + ".png";
        }
        else if (card.getRank() == Rank.JACK) {
            cardFile = "cards/jack_of_" + card.getSuit().toString().toLowerCase() + ".png";
        }
        else if (card.getRank() == Rank.QUEEN) {
            cardFile = "cards/queen_of_" + card.getSuit().toString().toLowerCase() + ".png";
        }
        else if (card.getRank() == Rank.KING) {
            cardFile = "cards/king_of_" + card.getSuit().toString().toLowerCase() + ".png";
        }
        else {
            cardFile = "cards/" + card.getRank().getValue() + "_of_" + card.getSuit().toString().toLowerCase() + ".png";
        }
        return cardFile;
    }

    private void addCardToDealer(Card card) {
        String cardFile = getCardFile(card);
        ImageView cardView = new ImageView(new Image(cardFile));
        cardView.setFitWidth(100);
        cardView.setFitHeight(150);
        dealerCardViews.add(cardView);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

