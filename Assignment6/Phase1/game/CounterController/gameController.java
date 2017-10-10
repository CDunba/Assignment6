package game.CounterController;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import game.CounterView.cardTableOutline;
import game.CounterModel.*;
import game.CounterView.*;
import game.CounterController.*;

public class gameController {
	
   static final int amtCardsInHand = 7;
   static final int numPlayers = 2;
   
   static final String GAME_TITLE = "High Card";
   static final int numPacks = 1;

   static public cardTableOutline cardTable;
   
   static Card[] player1Winnings = new Card[amtCardsInHand * 2];
   static Card[] player2Winnings = new Card[amtCardsInHand * 2];

   private CardListener listen;
   
   public void start()
   {
      cardTable = new cardTableOutline(this, GAME_TITLE, amtCardsInHand,numPlayers);
      listen = new CardListener();
   }

   class CardListener implements MouseListener
   {
      public void mouseEntered(MouseEvent e)
      {       
    	  gameController.cardTable.currentCard((JLabel)e.getSource());
      } 
       
      public void mouseExited(MouseEvent e)
       {
          gameController.cardTable.deselectedCard((JLabel)e.getSource());
       } 
       
      public void mouseClicked(MouseEvent e)
      {
         int cardPos = 0;
         JLabel source = (JLabel)e.getSource();

         if (source.getName() != cardTable.getStatusCommand())
          {           
        	 cardPos = cardTable.locatePlayerCard((JLabel)e.getSource());
             if (cardPos > -1)
             {
                MainPhase3.playCard(cardPos);
             }
             else
             {
                MainPhase3.initGame();
             }         
          }
          
          for (int i = 0; i < MainPhase3.humanLabels.length; i++)
             if (MainPhase3.humanLabels[i] == source)
             {
                MainPhase3.playCard(i);
                break;
             }
          
          if (MainPhase3.statusText == source)
          {
             MainPhase3.initGame();
          }
       } 
      
       public void mousePressed(MouseEvent e)
       {
   
       } 
       
       public void mouseReleased(MouseEvent e)
       {
   
       }   
    } 
}
