package game.CounterController;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import game.CounterView.cardTableFrame;
import game.CounterModel.*;
import game.CounterView.*;
import game.CounterController.*;

public class cardGameAppController
{
   static final int NUM_CARDS_PER_HAND = 7;
   static final int NUM_PLAYERS = 2;
   static final int numPacksPerDeck = 1;
   static final int numJokersPerPack = 0;
   static final String GAME_TITLE = "High Card Game";

   static public cardTableFrame cardTable;
   static Card[] player1Winnings = new Card[NUM_CARDS_PER_HAND * 2];
   static Card[] player2Winnings = new Card[NUM_CARDS_PER_HAND * 2];

   private CardListener listen;
   
   public void start()
   {
      cardTable = new cardTableFrame(this, GAME_TITLE, NUM_CARDS_PER_HAND, 
            NUM_PLAYERS);
      listen = new CardListener();
   }

    class CardListener implements MouseListener
    {
       public void mouseEntered(MouseEvent e)
       {
          cardGameAppController.cardTable.currentCard((JLabel)e.getSource());
       } 
       
       public void mouseExited(MouseEvent e)
       {
          cardGameAppController.cardTable.deselectedCard((JLabel)e.getSource());
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
