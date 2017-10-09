package game.CounterView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import game.CounterController.gameController;
import game.CounterView.cardTablePanel;
import game.CounterController.CardListener;
public class cardTableOutline extends JFrame {

   static final int MAX_CARDS_PER_HAND = 56;
   static final int MAX_PLAYERS = 2; 
   private int numCardsPerHand = 20;
   private int numPlayers = 2;
   private String title;
   private gameController baseController;
   private cardTablePanel pnlComputerHand, pnlHumanHand, pnlPlayArea, 
   pnlPlayedCards, pnlPlayerText, pnlStatusText;
 
   static JLabel[] computerLabels;
   static JLabel[] humanLabels;
   static JLabel[] playedCardLabels;
   static JLabel[] playLabelText;
   static JLabel statusText = new JLabel("");

   static CardListener playerCardListener;
   
   public int getNumCardsPerHand()
   {
      return numCardsPerHand;
   }

   public int getNumPlayers()
   {
      return numPlayers;
   }
   
   public String getStatusCommand()
   {
      return statusText.getName();
   }
   
   public boolean setComputerCard(int cardPosition, Icon cardIcon)
   {
      boolean returnValue = true;
      
      if ((cardPosition < 1) || (cardPosition > numCardsPerHand))
      {
         returnValue = false;
      }
      else
      {
         computerLabels[cardPosition - 1] = new JLabel();
         computerLabels[cardPosition - 1].setIcon(cardIcon);
      }
      
      return returnValue;
   }
   
   public boolean setHumanCard(int cardPosition, Icon cardIcon)
   {
      boolean returnValue = true;
      if ((cardPosition < 1) || (cardPosition > numCardsPerHand))
      {
         returnValue = false;
      }
      else
      {
         humanLabels[cardPosition - 1] = new JLabel();
         humanLabels[cardPosition - 1].setIcon(cardIcon);
         humanLabels[cardPosition - 1].setMaximumSize(new Dimension(0,0));      
      }
      return returnValue;
   }

   public cardTableOutline(gameController baseController,
         String title, int numCardsPerHand, int numPlayers)
   {
      super();
      if((numCardsPerHand < 0) || 
            (numCardsPerHand > cardTableOutline.MAX_CARDS_PER_HAND))
      {
         this.numCardsPerHand = 20;     
      }
      else
      {
         this.numPlayers = numPlayers;
      }
  
      if((numPlayers < 2) || (numPlayers > cardTableOutline.MAX_PLAYERS))
      {
         this.numPlayers = 2;
      }
      else
      {
         this.numPlayers = numPlayers;  
      }
      
      if(title == null)
      {
         this.title = "";
      }
      else
      {
         this.title = title;
      }
      
      computerLabels = new JLabel[this.numCardsPerHand];
      humanLabels = new JLabel[this.numCardsPerHand];
      playedCardLabels = new JLabel[this.numPlayers];
      playLabelText = new JLabel[this.numPlayers];
      
      setupFrame();
      setupComputerHand();
      setupPlayingArea();
      setupHumanHand();
      this.setVisible(true);
   } 
   
   private void setupFrame()
   {
      this.setTitle(this.title);
      this.setSize(800, 600);
      this.setMinimumSize(new Dimension(800, 600));
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   
      BorderLayout layout = new BorderLayout();
      this.setLayout(layout);    
   }

   private void setupComputerHand()
   {
      pnlComputerHand = new cardTablePanel(baseController);

      FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
      TitledBorder border = new TitledBorder("Computer Hand");

      pnlComputerHand.setLayout(flowLayout);
      pnlComputerHand.setPreferredSize(
            new Dimension((int)this.getMinimumSize().getWidth()-50, 105));

      JScrollPane scrollComputerHand = new JScrollPane(pnlComputerHand);
      scrollComputerHand.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_NEVER);
      scrollComputerHand.setBorder(border);
      this.add(scrollComputerHand, BorderLayout.NORTH);   
   }
   
   private void setupHumanHand()
   {
      TitledBorder border = new TitledBorder("Human Hand");
      FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);

      pnlHumanHand = new cardTablePanel(baseController);
      pnlHumanHand.setLayout(flowLayout);
      
      pnlHumanHand.setPreferredSize(
            new Dimension((int)this.getMinimumSize().getWidth()-50, 115));
      
      JScrollPane scrollHumanHand = new JScrollPane(pnlHumanHand);
      scrollHumanHand.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_NEVER);
      scrollHumanHand.setBorder(border);
      this.add(scrollHumanHand, BorderLayout.SOUTH);
   }
   
   private void setupPlayingArea()
   {
      TitledBorder border = new TitledBorder("Playing Area");
      BorderLayout layout = new BorderLayout();

      pnlPlayArea = new cardTablePanel(baseController);
      pnlPlayArea.setBorder(border);
      pnlPlayArea.setLayout(layout);

      GridLayout gridLayoutCardsArea = new GridLayout(1, 2);
      GridLayout gridLayoutStatusArea = new GridLayout(1, 1);
      
      pnlPlayedCards = new cardTablePanel(baseController);
      pnlPlayedCards.setLayout(gridLayoutCardsArea);
      pnlPlayedCards.setPreferredSize(new Dimension((int)this.getMinimumSize().
      getWidth()-50, 150));
      
      pnlPlayerText = new cardTablePanel(baseController);
      pnlPlayerText.setLayout(gridLayoutCardsArea);
      pnlPlayerText.setPreferredSize(new Dimension(100, 30));
      
      pnlStatusText = new cardTablePanel(baseController);
      pnlStatusText.setLayout(gridLayoutStatusArea);
      pnlStatusText.setPreferredSize(new Dimension(100, 30));
      
      pnlPlayArea.add(pnlPlayedCards, BorderLayout.NORTH);
      pnlPlayArea.add(pnlPlayerText, BorderLayout.CENTER);
      pnlPlayArea.add(pnlStatusText, BorderLayout.SOUTH);
      this.add(pnlPlayArea, BorderLayout.CENTER);     
   }

   public void initializeGameBoard()
   {
      resetCardTable();    
   }

   public void resetCardTable()
   {
      pnlComputerHand.removeAll();
      pnlHumanHand.removeAll();
      pnlPlayedCards.removeAll();
      pnlPlayerText.removeAll();
      pnlStatusText.removeAll();
   }

   public void addCardListener(MouseListener listenForMouse)
   {
      
   }
   
   public void currentCard(JLabel source)
   {
      //JLabel source = (JLabel)e.getSource();
      LineBorder border = new LineBorder(new Color(235, 212, 040), 3);
      source.setBorder(border);

   }
   
   public void deselectedCard(JLabel source)
   {

      source.setBorder(null);

   }
  
   public int locatePlayerCard(JLabel source)
   {
      int returnLocation = -1;
      
      for (int i = 0; i < humanLabels.length; i++)
         if (humanLabels[i] == source)
         {
            returnLocation = i;
            break;
         }     
      return returnLocation;
   }
}
