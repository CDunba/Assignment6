import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.io.File;

public class TimedGame
{
   static Controller controller;
   static Model model;
   static View view;
   public static final int CARDS_PER_HAND = 7;
   public static final int PLAYERS_COUNT = 2;
   static final String  RESET_TIME = "Reset";
   static final String SKIP_TURN = "I cannot play.";
   static final String START_TIME = "Start";
   static final String   STOP_TIME = "Stop";
   
   
   public static void main(String[] args)
   {
      controller = new Controller();
      view = new View("High Card Game", PLAYERS_COUNT, CARDS_PER_HAND);
      Model model = new Model(controller, view);
      model.startGame();
   } 
   
   private static class Model
   {
      static CardGameFramework highCardGame;
      
      static Thread timerThread;
      static boolean timeClicking = false;
      static View view;
      static Controller controller;
      
      static int packsInDeck = 1;
      static int jokersInPack = 0;
      static int unusedCardsInPack = 0;
      
      static Card[] unusedCardsPerPack = null;
      static Card[][] playerWinnings = new Card[PLAYERS_COUNT][CARDS_PER_HAND * 2];
      static int[] cardPointValues = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

      private static final int HUMAN_PLAYER = 1;   
      private static int mint = 0, sec = 0;
      public static final int NUM_STACKS = 2;
      private static CardStack[] cardStacks = new CardStack[NUM_STACKS];
      private static int selectedCard = -1;
      private static int[] numNoPlay = new int[PLAYERS_COUNT];
      private static int skips = 0;
      private static boolean check = false;
      public static String timerString;
      
      static Runnable timer = new Runnable()
      {
         public void run()
         {
            while (timeClicking)
            {
               timerString = "";
               sec++;
               if (sec == 60)
               {
                  sec = 0;
                  mint++;
               }
               
               timerString = mint + ":";
               
               if (sec < 10)
                  timerString += "0";
               
               timerString += sec;
               view.updateTimer(timerString);
               
               try
               {
                  Thread.sleep(1000);
               }
               catch (Exception e)
               {
                  
               }
            }
         } 
      }; 
      
      public static void resetTimer()
      {
         sec = 0;
         mint = 0;
      }
      
      public Model(Controller controller, View view)
      {
         this.controller = controller;
         this.view = view;
         this.highCardGame = new CardGameFramework(this.packsInDeck, this.jokersInPack, this.unusedCardsInPack,
            this.unusedCardsPerPack, TimedGame.PLAYERS_COUNT, TimedGame.CARDS_PER_HAND);
      } 
      
      private static char[] getValidValues(Card card)
      {
         String cardValues = new String(Card.values);
         char[] values = new char[2];
         int index=cardValues.length() - 1;
         if (card.getValue() == cardValues.charAt(0))
         {
            values[0] = cardValues.charAt(index);
            values[1] = cardValues.charAt(1);
         }
         else if (card.getValue() == cardValues.charAt(index))
         {
            values[0] = cardValues.charAt(index-1);
            values[1] = cardValues.charAt(0);
         }
         else
         {
            values[0] = cardValues.charAt(cardValues.indexOf(card.getValue()) - 1);
            values[1] = cardValues.charAt(cardValues.indexOf(card.getValue()) + 1);
         }
         
         return values;
      } 
      
      
      
      public static boolean placeCard(int stack)
      {
         Card humanCard = highCardGame.getHand(HUMAN_PLAYER).inspectCard(selectedCard);
         char[] validValues = getValidValues(cardStacks[stack].returnTopCard());

         for (char value : validValues)
         {
            if (value == humanCard.getValue())
            {
               skips = 0;
               cardStacks[stack].addCard(humanCard);
               highCardGame.playCard(HUMAN_PLAYER, selectedCard);
               selectedCard = -1;
               highCardGame.takeCard(HUMAN_PLAYER, CARDS_PER_HAND - 1);
               highCardGame.getHand(HUMAN_PLAYER).sort();
               view.drawHand(HUMAN_PLAYER, highCardGame.getHand(HUMAN_PLAYER), true);
               view.setPlayedCard(stack, humanCard);
               
               if (highCardGame.getHand(HUMAN_PLAYER).getNumCards() == 0)
               {
                  gameOver();
                  return true;
               }
               
               playComputerCard();
               return true;
            }
         }        
         view.setStatusText("Invalid Card");
         return false;
      } 
      
      private static int findWinner()
      {
         if (numNoPlay[0] == numNoPlay[1])
            return -1;
         else if (numNoPlay[0] > numNoPlay[1])
            return 1;
         else
            return 0;
      } 
      
      private static void skipPlayerTurn()
      {
         int play[] = getPossiblePlay(HUMAN_PLAYER);
         
         if (play[0] != -1)
         {
            view.setStatusText("One of your card can be placed on stack"+ (play[0] + 1) + ".");
            return;
         }
         
         boolean playComputerCard = true;
         skips++;
         numNoPlay[HUMAN_PLAYER]++;
         view.setStatusText("");
         
         if (skips == 2)
         {
            if (!setStackTops())
            {
               playComputerCard = false;
            }
            
            skips = 0;
         }
         
         if(playComputerCard)
         {
            playComputerCard();
         }
         
         view.updateNoPlayLabel(numNoPlay[0], numNoPlay[1]);
      } 
      
      private static void initGame()
      {
         highCardGame.startNewGame();
         highCardGame.deal();
         highCardGame.getHand(HUMAN_PLAYER).sort();
         
         for (int i = 0; i < playerWinnings[0].length; i++)
         {
            playerWinnings[0][i] = null;
            playerWinnings[1][i] = null;
         }
         
         view.drawHand(0, highCardGame.getHand(0), false);
         view.drawHand(1, highCardGame.getHand(1), true);
         view.setPlayLabelText(0, "Stack 1");
         view.setPlayLabelText(1, "Stack 2");
         view.updateNoPlayLabel(0, 0);
         view.setStatusText("Select a card and then select a stack");
         cardStacks[0] = new CardStack(Deck.MAX_CARDS);
         cardStacks[1] = new CardStack(Deck.MAX_CARDS);
         cardStacks[0].addCard(highCardGame.getCardFromDeck());
         cardStacks[1].addCard(highCardGame.getCardFromDeck());
         view.setPlayedCard(0, cardStacks[0].returnTopCard());
         view.setPlayedCard(1, cardStacks[1].returnTopCard());
         view.removeStatusListener();
         mint = 0;
         sec = 0;
         view.updateTimer("0:00");
         numNoPlay[0] = 0;
         numNoPlay[1] = 0;
         
         timerThread = new Thread(timer);
         view.addButtonListener();
         skips = 0;
      } 
      
      private static void startGame()
      {
         initGame();
         view.showCardTable();

      } 
      
      static int[] getPossiblePlay(int player)
      {
         for (int i = 0; i < cardStacks.length; i++)
         {
            String cardValues = new String(Card.values);
            char[] validValues = getValidValues(cardStacks[i].returnTopCard());
            
            for (int x = 0; x < highCardGame.getHand(player).getNumCards(); x++)
            {
               Card card = highCardGame.getHand(player).inspectCard(x);
               
               for (char value : validValues)
                  if (card.getValue() == value)
                     return new int[] {i, x};
            } 
         } 
         
         return new int[] {-1, -1};
      }
      
      private static void playComputerCard()
      {
         int[] game = getPossiblePlay(0);
         
         if (game[0] != -1)
         {
            int stack = game[0];
            int location = game[1];
            Card compCard = highCardGame.getHand(0).inspectCard(location);
            
            highCardGame.playCard(0, location);
            highCardGame.takeCard(0, CARDS_PER_HAND - 1);
            view.drawHand(0, highCardGame.getHand(0), false);
            view.setPlayedCard(stack, compCard);
            cardStacks[stack].addCard(compCard);
            view.setStatusText(compCard + "played by computer on Stack "+ (stack+1) + ".");
            skips = 0;
         }
         else
         {
            view.setStatusText("Computer did not play.");
            numNoPlay[0]++;
            view.updateNoPlayLabel(numNoPlay[0], numNoPlay[1]);
            skips++;
            if (skips >= 2)
            {
               if (!setStackTops())
               {
                  return;
               }      
               skips = 0;
            }
         }  
         if (check)
         {
            view.setStatusText(view.getStatusText() + " New cards are placed on each stack.");
            check = false;
         }       
         if (highCardGame.getHand(0).getNumCards() == 0)
            gameOver();  
         view.setStatusText(view.getStatusText() + " Your turn..");
      }
      
      private static boolean setStackTops()
      {
         int errorFlagCount = 0;
         
         for (int i = 0; i < NUM_STACKS; i++)
         {
            Card card = highCardGame.getCardFromDeck();
            
            if (!card.flag)
            {
               cardStacks[i].addCard(card);
               view.setPlayedCard(i, card);
            }
            else
            	errorFlagCount++;
         } 
         
         if (errorFlagCount == NUM_STACKS)
         {
            gameOver();
            return false;
         }
         else
         {
        	 check = true;
         }
         
         return true;
      } 
      
      private static void gameOver()
      {
    	  //System.out.println("game over called");
    	  timeClicking = false;
         int winner = findWinner();
         
         if (winner == HUMAN_PLAYER)
            view.setStatusText(view.getStatusText() + "You won..Click here to play again!");
         else if (winner == 0)
            view.setStatusText(view.getStatusText() + "Computer won..Click here to play again!");
         else
            view.setStatusText(view.getStatusText() + "Its a draw..Click here to play again!");    
         view.removeButtonListener();
         view.addStatusListener();
      } 
      
      static int getCardPointValue(Card card)
      {
         if (card.flag)
         {
        	 return -1;
         }    
         String values = new String(Card.values);
         return cardPointValues[values.indexOf(card.getValue())];
      } 
   } 

   private static class View
   {
      static JLabel[][] hands;
      static JLabel[] playedCardLabels;
      static JLabel[] playLabelText;
      static JButton skipBtn = new JButton(SKIP_TURN);
      static JButton startBtn = new JButton(START_TIME);
      static JButton stopBtn = new JButton(STOP_TIME);
      static JLabel timerLabel = new JLabel("0:00");
      static JLabel noPlayLabel = new JLabel();
      static JPanel[] playedCardPanels = new JPanel[Model.NUM_STACKS]; 
      private static final Color COLOR = new Color(0, 255, 255);
      static JLabel status = new JLabel("");
      static CardTable cardTable;
      
      public View(String title, int playersCount, int cardsInHand)
      {
         this.cardTable = new CardTable(title, cardsInHand, playersCount);
         this.hands = new JLabel[playersCount][cardsInHand];
         this.playLabelText = new JLabel[playersCount];
         
         for (int i = 0; i < playersCount; i++)
         {
            playLabelText[i] = new JLabel();
            playLabelText[i].setHorizontalAlignment(JLabel.CENTER);
            playLabelText[i].setVerticalAlignment(JLabel.TOP);
            cardTable.pnlPlayerText.add(playLabelText[i]);
         }

         playedCardPanels[0] = new JPanel();
         playedCardPanels[1] = new JPanel();
         FlowLayout flow = new FlowLayout(FlowLayout.CENTER);
         playedCardPanels[0].setLayout(flow);
         playedCardPanels[1].setLayout(flow);
         cardTable.pnlPlayedCards.add(playedCardPanels[0]);
         cardTable.pnlPlayedCards.add(playedCardPanels[1]);
         playedCardLabels = new JLabel[PLAYERS_COUNT];
         
         for (int i = 0; i < playedCardLabels.length; i++)
         {
            playedCardLabels[i] = new JLabel();
            playedCardLabels[i].setHorizontalAlignment(JLabel.CENTER);
            playedCardLabels[i].addMouseListener(TimedGame.controller);
            playedCardPanels[i].add(playedCardLabels[i]);
         }

         noPlayLabel.setHorizontalAlignment(JLabel.LEFT);
         noPlayLabel.setVerticalAlignment(JLabel.BOTTOM);
         cardTable.pnlNoPlays.add(noPlayLabel);
         status.setHorizontalAlignment(JLabel.CENTER);
         skipBtn.setHorizontalAlignment(JButton.CENTER);
         skipBtn.setFocusPainted(false);
         cardTable.pnlStatusText.add(status);
         cardTable.pnlStatusText.add(skipBtn);

         timerLabel.setVerticalAlignment(JLabel.CENTER);
         timerLabel.setHorizontalAlignment(JLabel.CENTER);
         timerLabel.setText(Model.timerString);
         cardTable.pnlTimerLabel.add(timerLabel);
         startBtn.setBackground(Color.YELLOW);
         startBtn.setBorderPainted(true);
         startBtn.setOpaque(true);
         cardTable.pnlTimerSubBttn.add(startBtn);
         stopBtn.setBackground(Color.GRAY);
         stopBtn.setBorderPainted(true);
         stopBtn.setOpaque(true);
         cardTable.pnlTimerSubBttn.add(stopBtn);
         cardTable.pnlTimer.add(timerLabel);
      }
      
      private static void addStatusListener()
      {
    	  status.addMouseListener(model.controller);
      }
      
      private static void removeStatusListener()
      {
    	  status.setBorder(null);
         status.removeMouseListener(model.controller);
      }
      
      private static void updateNoPlayLabel(int player1Score, int player2Score)
      {
         noPlayLabel.setText("<html><u>No Play Counts</u><br>Computer: "
            + player1Score + "<br>You: " + player2Score + "</html>");
      }
      
      private static void updateTimer(String timerString)
      {
         timerLabel.setText(timerString);
         cardTable.pnlTimer.revalidate();
         cardTable.pnlTimer.repaint();
      } 
      
      private static void addButtonListener()
      {
    	  skipBtn.addActionListener(model.controller);
    	  startBtn.addActionListener(model.controller);
    	  stopBtn.addActionListener(model.controller);
      } 
      
      private static void removeButtonListener()
      {
    	 skipBtn.removeActionListener(model.controller);
    	 startBtn.removeActionListener(model.controller);
    	 stopBtn.removeActionListener(model.controller);
      }
      
      
      
      private void setPlayedCard(int stack, Card card)
      {
         if (card == null)
            playedCardLabels[stack].setIcon(null);
         else
            playedCardLabels[stack].setIcon(GUICard.getIcon(card));
      }
      
      private void setPlayLabelText(int player, String text)
      {
         playLabelText[player].setText(text);
         cardTable.pnlPlayerText.revalidate();
         cardTable.pnlPlayerText.repaint();
      } 
      
      private String getStatusText()
      {
         return status.getText();
      } 
      
      private void setStatusText(String text)
      {
    	  status.setText(text);
         cardTable.pnlStatusText.revalidate();
         cardTable.pnlStatusText.repaint();
      }
      
      
      
      public void drawHand(int player, Hand hand, boolean showCards)
      {
         this.cardTable.handPanels[player].removeAll();
         this.hands[player] = new JLabel[CARDS_PER_HAND];
         
         for (int i = 0; i < hand.getNumCards(); i++)
         {
            this.hands[player][i] = new JLabel();
            
            if(showCards)
            {
               this.hands[player][i].setIcon(GUICard.getIcon(hand
                  .inspectCard(i)));
               this.hands[player][i].addMouseListener(Model.controller);
            }
            else
               this.hands[player][i].setIcon(GUICard.getBackCardIcon());
            
            this.cardTable.handPanels[player].add(this.hands[player][i]);
         } 
         
         this.cardTable.handPanels[player].validate();
         this.cardTable.handPanels[player].repaint();
      }
      
      private void highlightLabel(JLabel label)
      {
         label.setBorder(new LineBorder(COLOR));
      }
      
      private void deHighlightLabel(JLabel label)
      {
         label.setBorder(null);
      }
      
      private void showCardTable()
      {
         this.cardTable.setVisible(true);
      } 
      
      
      // GUICard class
      public static class GUICard
      {
         private static Icon[][] iconCards = new ImageIcon[14][4];
         private static Icon iconBack;
         static boolean iconsLoaded = false;
         static final char[] VALID_SUITS = {'C', 'D', 'H', 'S'};
         private static String iconFolderPath = "./images";
         
         public static Icon getIcon(Card card)
         {
            if (!GUICard.iconsLoaded)
               GUICard.loadCardIcons();
            return iconCards[valueAsInt(card)][suitAsInt(card)];
         } 
         private static int valueAsInt(Card card)
         {
            String values = new String(Card.values);
            
            return values.indexOf(card.getValue());
         } 
         
         private static int suitAsInt(Card card)
         {
            return card.getSuit().ordinal();
         } 
         
         public static Icon getBackCardIcon()
         {
            if (!GUICard.iconsLoaded)
               GUICard.loadCardIcons();
            
            return GUICard.iconBack;
         }
         
         private static void loadCardIcons()
         {
            if (!(new File(GUICard.iconFolderPath).exists()))
            {
               JOptionPane.showMessageDialog(null, "Press OK to select folder to fetch Images");
               JFileChooser chooser = new JFileChooser(".");
               chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
               chooser.setMultiSelectionEnabled(false);
               chooser.showDialog(null, "Select");
               File selectedFile = chooser.getSelectedFile();
               
               if (selectedFile == null)
                  System.exit(0);
               
               GUICard.iconFolderPath = selectedFile.getPath();
               System.out.println(iconFolderPath);
            }

            for (int i = 0; i < Card.values.length; i++)
            {
               for (int j = 0; j < VALID_SUITS.length; j++)
               {
                  if (!new File(iconFolderPath + "/" + Card.values[i] + VALID_SUITS[j] + ".gif").exists())
                  {
                     JOptionPane.showMessageDialog(null, Card.values[i]
                        + VALID_SUITS[j] + ".gif could not be found in the icon folder. Program execution will now stop.");
                     System.exit(0);
                  }
                  
                  iconCards[i][j] = new ImageIcon(iconFolderPath + "/" + Card.values[i] + VALID_SUITS[j] + ".gif");
               }
            }
            
            iconBack = new ImageIcon(iconFolderPath + "/BK.gif");
            GUICard.iconsLoaded = true; 
         } 
         
          
      } 
      
      class CardTable extends JFrame
      {
         static final int MAX_CARDS_PER_HAND = 56;
         static final int MAX_PLAYERS = 2;
         
         public JPanel[] handPanels;
         public JPanel pnlPlayArea, pnlPlayedCards, pnlPlayerText,pnlStatusText, pnlNoPlays, pnlPlayedCardArea, pnlTimer, 
            pnlTimerLabel, pnlTimerButton, pnlTimerSubBttn;

         public CardTable(String title, int numCard, int playersCount)
         {
            super(); 
            this.handPanels = new JPanel[playersCount];

            if (numCard < 0 || numCard > CardTable.MAX_CARDS_PER_HAND)
            {
               
            }
            
            if (playersCount < 2 || playersCount > CardTable.MAX_PLAYERS)
            {
               
            }
            
            if (title == null)
               title = "";

            this.setTitle(title);
            this.setSize(800, 600);
            this.setMinimumSize(new Dimension(800, 600));
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            BorderLayout layout = new BorderLayout();
            this.setLayout(layout);
            this.setLocationRelativeTo(null);
            FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
            
            //set computer panel
            TitledBorder border = new TitledBorder("Computer Hand");
            this.handPanels[0] = new JPanel();
            this.handPanels[0].setLayout(flowLayout);
            this.handPanels[0].setPreferredSize(new Dimension((int) this.getMinimumSize().getWidth() - 50, 105));
            JScrollPane computerHandScroll = new JScrollPane(this.handPanels[0]);
            computerHandScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            computerHandScroll.setBorder(border);
            this.add(computerHandScroll, BorderLayout.NORTH);

            //set playing area panel
            border = new TitledBorder("Playing Area");
            GridLayout gridLayoutCardsArea = new GridLayout(1, 2);
            GridLayout gridLayoutStatusArea = new GridLayout(2, 1);
            pnlPlayArea = new JPanel();
            pnlPlayArea.setBorder(border);
            
            layout = new BorderLayout();
            pnlPlayArea.setLayout(layout);
            
            pnlPlayedCardArea = new JPanel();
            pnlPlayedCardArea.setLayout(new GridLayout(2, 1));
            
            pnlTimer = new JPanel();
            pnlTimer.setLayout(gridLayoutStatusArea);
            
            pnlNoPlays = new JPanel();
            pnlNoPlays.setLayout(new GridLayout(3, 1));
            
            pnlPlayedCards = new JPanel();
            pnlPlayedCards.setLayout(gridLayoutCardsArea);
            
            pnlPlayerText = new JPanel();
            pnlPlayerText.setLayout(gridLayoutCardsArea);
            
            pnlStatusText = new JPanel();
            pnlStatusText.setLayout(gridLayoutStatusArea);
            
            // add components 
            pnlPlayedCardArea.add(pnlPlayedCards);
            pnlPlayedCardArea.add(pnlPlayerText);
            pnlPlayArea.add(pnlTimer, BorderLayout.EAST);
            pnlPlayArea.add(pnlNoPlays, BorderLayout.WEST);
            pnlPlayArea.add(pnlPlayedCardArea, BorderLayout.CENTER);
            pnlPlayArea.add(pnlStatusText, BorderLayout.SOUTH);
            this.add(pnlPlayArea, BorderLayout.CENTER);

            // set timer panel
            border = new TitledBorder("Game Timer");
            GridLayout gridLayoutTimer = new GridLayout(1, 2);
            GridLayout subGridLayoutTimer = new GridLayout(2, 1);
            pnlTimer = new JPanel();
            pnlTimer.setBorder(border);
            layout = new BorderLayout();
            pnlTimer.setLayout(layout);
            pnlTimerLabel = new JPanel();
            pnlTimerLabel.setLayout(gridLayoutTimer);
            pnlTimerButton = new JPanel();
            pnlTimerButton.setLayout(gridLayoutTimer);
            pnlTimerSubBttn = new JPanel();
            pnlTimerSubBttn.setLayout(subGridLayoutTimer);
            pnlTimerLabel.setPreferredSize(new Dimension(150, 150));
            pnlTimerButton.setPreferredSize(new Dimension(150, 50));
            pnlTimerSubBttn.setPreferredSize(new Dimension(150, 50));
            pnlTimer.add(pnlTimerLabel, BorderLayout.NORTH);
            pnlTimer.add(pnlTimerButton, BorderLayout.SOUTH);
            pnlTimerButton.add(pnlTimerSubBttn);
            this.add(pnlTimer, BorderLayout.EAST);
            
            //set human panel
            border = new TitledBorder("Human Hand");
            this.handPanels[1] = new JPanel();
            this.handPanels[1].setLayout(flowLayout);
            this.handPanels[1].setPreferredSize(new Dimension((int) this.getMinimumSize().getWidth() - 50, 105));
            JScrollPane scrollHumanHand = new JScrollPane(this.handPanels[1]);
            scrollHumanHand.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollHumanHand.setBorder(border);
            this.add(scrollHumanHand, BorderLayout.SOUTH);
         }
      } 
   } 
   
   private static class Controller implements MouseListener, ActionListener
   {
      private static JLabel selectedCard = null;
      private static boolean stackClickable = false;
      
      public void mouseEntered(MouseEvent e)
      {
         JLabel origin = (JLabel)e.getSource();
         
         if (origin == view.playedCardLabels[0] || origin == view.playedCardLabels[1])
         {
            if (stackClickable)
               view.highlightLabel(origin);
         }
         else
            view.highlightLabel(origin);
      } 
      
      public void mouseExited(MouseEvent e)
      {
         JLabel origin = (JLabel)e.getSource();
         
         if (origin != selectedCard)
            view.deHighlightLabel(origin);
      } 
      
      public void mouseClicked(MouseEvent e)
      {
         JLabel origin = (JLabel)e.getSource();
         
         if (origin == view.status)
         {
            model.initGame();
            return;
         }
         
         for (int playerHand = 0; playerHand < view.hands.length; playerHand++)
         {
            for (int card = 0; card < model.highCardGame.getHand(playerHand).getNumCards(); card++)
            {
               if (view.hands[playerHand][card].getIcon() == View.GUICard.getBackCardIcon())
                  continue;
               
               if (view.hands[playerHand][card] == origin)
               {
                  if (origin != selectedCard && selectedCard != null)
                  {
                	  view.deHighlightLabel(selectedCard);
                  }
                  
                  selectedCard = origin;
                  model.selectedCard = card;
                  stackClickable = true;
                  view.setStatusText("Select the stack.");
                  view.highlightLabel(origin);
                  return;
               }
            } 
         } 
         
         if (stackClickable)
         {
            boolean cardPlaced = false;
            
            if(origin == view.playedCardLabels[0])
               cardPlaced = model.placeCard(0); 
            
            else if (origin == view.playedCardLabels[1])
               cardPlaced = model.placeCard(1);
            
            if(cardPlaced)
            {
               stackClickable = false;
               view.deHighlightLabel(origin);
               selectedCard = null;
            }
         }
      }
      
      public void actionPerformed(ActionEvent e)
      {
         JButton origin = (JButton)e.getSource();
         
         if (origin.getActionCommand() == TimedGame.SKIP_TURN)
         {
            model.skipPlayerTurn();
         }

         else if (origin.getActionCommand() == TimedGame.START_TIME)
         {
            model.timeClicking = true;
            model.timerThread = new Thread(model.timer);
            model.timerThread.start();
            origin.setText(TimedGame.RESET_TIME);
         }
         else
         {
            model.timerThread.stop();

            try
            {
               model.timerThread.join();
            }
            catch (InterruptedException exp)
            {
               exp.printStackTrace();
            }
            finally
            {
               if (origin.getActionCommand() == TimedGame.RESET_TIME)
               {
                  model.timeClicking = true;
                  model.timerThread = new Thread(model.timer);
                  model.resetTimer();
                  model.timerThread.start();
                  origin.setText(TimedGame.RESET_TIME);
               }
               else
               {
                  view.startBtn.setText(TimedGame.START_TIME);
               }
            } 
         }
      } 
      public void mouseReleased(MouseEvent e)
      {

      }

      public void mousePressed(MouseEvent e)
      {

      }
   } 
}