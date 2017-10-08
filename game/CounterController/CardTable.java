package game.CounterController;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;


class CardTable extends JFrame 
{
   static final int MAX_CARDS_PER_HAND = 56;
   static final int MAX_PLAYERS = 2;

   private int numCardsPerHand;
   private int numPlayers;

   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea, pnlPlayedCards,
      pnlPlayerText, pnlStatusText;
    

   public CardTable(String title, int numCardsPerHand, int numPlayers)
   {
      if((numCardsPerHand < 0) || 
            (numCardsPerHand > CardTable.MAX_CARDS_PER_HAND))
      {
         this.numCardsPerHand = 20;
      }
            
      this.numCardsPerHand = numCardsPerHand;
        
      if((numPlayers < 2) || 
            (numPlayers > CardTable.MAX_PLAYERS))
      {
         this.numPlayers = numPlayers; 
      }
           
      if(title == null)
      {
         title = ""; 
      }
      //
      // build layouts and panels
      //
      this.setTitle(title);
      this.setSize(800, 600);
      this.setMinimumSize(new Dimension(800, 600));
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setLocationRelativeTo(null);

      BorderLayout layout = new BorderLayout();
      this.setLayout(layout);

      //set layout for computer hand
      
      FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
      pnlComputerHand = new JPanel();
      pnlComputerHand.setLayout(flowLayout);
      TitledBorder pnlTitle1 = new TitledBorder("Computer Hand");
      pnlComputerHand.setPreferredSize(
            new Dimension((int)this.getMinimumSize().getWidth()-60, 120));
      JScrollPane scroll = new JScrollPane(pnlComputerHand);
      scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
      scroll.setBorder(pnlTitle1);
      this.add(scroll, BorderLayout.NORTH);   

      
      // setting layout for pnlPlayedCards
      pnlPlayedCards = new JPanel();
      GridLayout gridLayoutCardsArea = new GridLayout(1, 2);
      pnlPlayedCards.setLayout(gridLayoutCardsArea);
      pnlPlayedCards.setPreferredSize(new Dimension((int)this.getMinimumSize().getWidth()-50, 150));
      
      // setting layout for pnlPlayerText
      pnlPlayerText = new JPanel();
      pnlPlayerText.setLayout(gridLayoutCardsArea);
      pnlPlayerText.setPreferredSize(new Dimension(135, 34));
      
      // setting layout for pnlStatusText
      pnlStatusText = new JPanel();
      GridLayout gridLayoutStatusArea = new GridLayout(1, 1);
      pnlStatusText.setLayout(gridLayoutStatusArea);
      pnlStatusText.setPreferredSize(new Dimension(135, 34));
          
      //set layout for playing area
      TitledBorder pnlTitle2 = new TitledBorder("Playing Area");
      pnlPlayArea = new JPanel();
      pnlPlayArea.setBorder(pnlTitle2);
      layout = new BorderLayout();
      pnlPlayArea.setLayout(layout);
      pnlPlayArea.add(pnlPlayedCards, BorderLayout.NORTH);
      pnlPlayArea.add(pnlPlayerText, BorderLayout.CENTER);
      pnlPlayArea.add(pnlStatusText, BorderLayout.SOUTH);
      this.add(pnlPlayArea, BorderLayout.CENTER);


      //set layout for human hand
      TitledBorder pnlTitle3 = new TitledBorder("Your Hand");
      pnlHumanHand = new JPanel();
      pnlHumanHand.setLayout(flowLayout);
      pnlHumanHand.setPreferredSize(new Dimension((int)this.getMinimumSize().getWidth()-60, 120));
      JScrollPane scroll2 = new JScrollPane(pnlHumanHand);
      scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
      scroll2.setBorder(pnlTitle3);
      this.add(scroll2, BorderLayout.SOUTH);
   } 
}  