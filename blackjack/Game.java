package blackjack;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JFrame {

    private static final long serialVersionUID = 1L;
    public static final int CARD_WIDTH = 100;
    public static final int CARD_HEIGHT = 145;
    public static final String IMAGE_DIR = "C:\\Users\\vueri\\eclipse-workspace\\MyGuiComp155\\src\\imagesCard\\";

    private Deck deck, discarded;
    private Dealer dealer;
    private Player player;
    private int wins, losses, draw;
    private int playerTurns;
    private int dealerTurns;

    private JButton btnHit, btnStand, btnNext;
    private JLabel[] lblDealerCards, lblPlayerCards;
    private JLabel lblScore, lblPlayerHandVal, lblDealerHandVal, lblGameMessage;

    public Game() {
        getContentPane().setBackground(new Color(0, 100, 0));
        setTitle("BlackJack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        getContentPane().setLayout(null); // Absolute positioning

        deck = new Deck(true);
        discarded = new Deck();
        dealer = new Dealer();
        player = new Player();
        deck.shuffle();

        gameGUI();
        startRound();
    }

    private void gameGUI() {
        // Make Buttons for "Hit" "Stand" and "Next Round" actions.
        // setBounds is used to define their locations and sizes
        btnHit = new JButton("Hit");
        btnHit.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnHit.setBackground(Color.GREEN);
        btnHit.setBounds(569, 250, 75, 50);
        btnStand = new JButton("Stand");
        btnStand.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnStand.setBackground(Color.RED);
        btnStand.setBounds(650, 250, 100, 50);
        btnNext = new JButton("Next Round");
        btnNext.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnNext.setBounds(587, 310, 140, 35);
        btnNext.setVisible(false);

        getContentPane().add(btnHit);
        getContentPane().add(btnStand);
        getContentPane().add(btnNext);

        // arrays to hold the dealer and player card images
        lblDealerCards = new JLabel[11];
        lblPlayerCards = new JLabel[11];

        //starting position of the cards
        int initialCardX = 10, initialCardY = 150;

        // next position of the cards
        for (int i = 0; i < lblDealerCards.length; i++) {
            lblDealerCards[i] = new JLabel();
            lblPlayerCards[i] = new JLabel();

            lblDealerCards[i].setBounds(initialCardX, initialCardY, CARD_WIDTH, CARD_HEIGHT);
            lblPlayerCards[i].setBounds(initialCardX, initialCardY + 250, CARD_WIDTH, CARD_HEIGHT);

            getContentPane().add(lblDealerCards[i]);
            getContentPane().add(lblPlayerCards[i]);

            initialCardX += 50;
            initialCardY -= 18;
        }

        lblScore = new JLabel("[Wins: 0]   [Losses: 0]   [Pushes: 0]");
        lblScore.setBounds(450, 10, 300, 50);
        getContentPane().add(lblScore);

        lblGameMessage = new JLabel("Round Start! Hit or Stand?");
        lblGameMessage.setBounds(450, 200, 300, 40);
        lblGameMessage.setFont(new Font("Arial", 1, 20));
        getContentPane().add(lblGameMessage);

        lblDealerHandVal = new JLabel("Dealer's Hand Value:");
        lblDealerHandVal.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblPlayerHandVal = new JLabel("Player's Hand Value:");
        lblPlayerHandVal.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblDealerHandVal.setBounds(20, 280, 300, 50);
        lblPlayerHandVal.setBounds(20, 530, 300, 50);
        getContentPane().add(lblDealerHandVal);
        getContentPane().add(lblPlayerHandVal);

        lblGameMessage.setForeground(Color.WHITE);
        lblDealerHandVal.setForeground(Color.WHITE);
        lblPlayerHandVal.setForeground(Color.WHITE);
        lblScore.setForeground(Color.WHITE);

        btnHit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
                player.hit(deck, discarded);
                updateScreen();
                checkBusts();
                checkPlayer33();
                playerTurns++;
                System.out.println(playerTurns);
                if (playerTurns == 4) {
                	dealerTurns=1;
                	dealersTurn();
                }
            }
        });


        btnStand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
                dealersTurn();
                //start dealer's turns
            }
        });

        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnNext.setVisible(false);
                btnHit.setVisible(true);
                btnStand.setVisible(true);
                playerTurns = 0;
                dealerTurns = 0;
                startRound();
            }
        });
    }

    private void checkBusts() {
        if (player.getHand().calculatedValue() > 33) {
            lblGameMessage.setText("You BUST - Over 33");
            losses++;
            btnHit.setVisible(false);
            btnStand.setVisible(false);
            btnNext.setVisible(true);
        }
    }

    private void checkWins() {
        lblDealerHandVal.setText("Dealer's hand value: " + dealer.getHand().calculatedValue());

        String message;
        int dealerValue = dealer.getHand().calculatedValue();
        int playerValue = player.getHand().calculatedValue();

        if (dealerValue > 33) {
            message = "Dealer Busts! You win!";
            wins++;
        } else if (dealerValue > playerValue) {
            message = "Dealer wins - Higher hand";
            losses++;
        } else if (playerValue > dealerValue) {
            message = "You win - Higher hand";
            wins++;
        } else {
            message = "Equal Value - Draw";
            draw++;
        }

        lblGameMessage.setText(message);
        lblScore.setText("[Wins: " + wins + "]   [Losses: " + losses + "]   [Draws: " + draw + "]");
    }

    private void checkPlayer33() {
        if (player.getHand().calculatedValue() == 33) {
            lblGameMessage.setText("You have 33!");
            wins++;
            btnHit.setVisible(false);
            btnStand.setVisible(false);
            btnNext.setVisible(true);
        }
    }

    private void dealersTurn() {
        while (dealer.getHand().calculatedValue() < 29 && dealerTurns < 4) {
            dealer.hit(deck, discarded);
   
            dealerTurns++;
        }
     // update screen after dealer's turn
        updateScreen();
        // check win after dealer's turns are over
        checkWins();
        //print dealer's final hand
        dealer.printHand(lblDealerCards);
        //disable hit and stand buttons, and show next button
        btnHit.setVisible(false);
        btnStand.setVisible(false);
        btnNext.setVisible(true);
    }



    private void updateScreen() {
        lblPlayerHandVal.setText("Player's Hand Value: " + player.getHand().calculatedValue());
        player.printHand(lblPlayerCards);
        lblScore.setText("[Wins: " + wins + "]   [Losses: " + losses + "]   [Draws: " + draw + "]");
    }

    private void startRound() {
        if (wins > 0 || losses > 0 || draw > 0) {
            System.out.println();
            System.out.println("Starting Next Round... Wins: " + wins + " Losses: " + losses + " Draw: " + draw);
            dealer.getHand().discardHandToDeck(discarded);
            player.getHand().discardHandToDeck(discarded);
        }
        dealer.getHand().discardHandToDeck(discarded);
        for (int i = 0; i < lblDealerCards.length; i++) {
            lblDealerCards[i].setIcon(null);
        }

        if (deck.cardsLeft() < 4) {
            deck.reset(discarded);
        }

        dealer.getHand().takeCardFromDeck(deck);
        dealer.printHand(lblDealerCards);
        player.getHand().takeCardFromDeck(deck);

        updateScreen();
        lblDealerHandVal.setText("Dealer's hand value: " + dealer.getHand().getCard(0).getPts());
        lblGameMessage.setText("Starting round! Hit or Stand?");
        

        playerTurns = 0;
        dealerTurns = 0;
    }

    public static void main(String[] args) {
        Game blackjack = new Game();
        blackjack.setVisible(true);
    }
}
