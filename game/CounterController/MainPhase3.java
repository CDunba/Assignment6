/****************************************************************
* Tejus Nandha, Courtney Dunbar, Talanda Williams
* CST 338-30 FA 17 Homework 5
* Phase 3 Project Assignment
*****************************************************************/
package game.CounterController;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

import game.CounterModel.Deck;
import game.CounterModel.Card;
import game.CounterModel.Card.Suit;
import game.CounterModel.CardGameFramework;

import java.awt.event.*;
import java.io.File;
import java.lang.Comparable;
import javax.swing.JOptionPane;


public class MainPhase3
{
   static int NUM_CARDS_PER_HAND = 7;
   static int NUM_PLAYERS = 2;
   static int numPacksPerDeck = 1;
   static int numJokersPerPack = 0;
   static int numUnusedCardsPerPack = 0;
   static Card[] unusedCardsPerPack = null;
   
   //labels
   static JLabel[] cpuSide = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] playedCardLabels = new JLabel[NUM_PLAYERS];
   static JLabel[] playLabelText = new JLabel[NUM_PLAYERS];
   
   
   static Card[] p1Winnings = new Card[NUM_CARDS_PER_HAND*2];
   static Card[] p2Winnings = new Card[NUM_CARDS_PER_HAND*2];
   static final String p1_TEXT = "Computer";
   static final String p2_TEXT = "You";
   static CardGameFramework highCardGame;
   static CardTable table;
   static JLabel statusText = new JLabel("");
   static CardListener listener = new CardListener();
   
   

   public static void main(String[] args)
   {
	   highCardGame = new CardGameFramework(numPacksPerDeck, numJokersPerPack,
		         numUnusedCardsPerPack, unusedCardsPerPack, NUM_PLAYERS,
		         NUM_CARDS_PER_HAND);
      table = new CardTable("High Card Game", NUM_CARDS_PER_HAND, NUM_PLAYERS);

      initGame();
      table.setVisible(true);
   }
   
   public static void initGame()
   {
      table.pnlComputerHand.removeAll();
      table.pnlPlayedCards.removeAll();
      table.pnlPlayerText.removeAll();
      table.pnlStatusText.removeAll();
      table.pnlHumanHand.removeAll();
      
      
      highCardGame.deal();
      highCardGame.getHand(1).sort();
      
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
      {
         cpuSide[i] = new JLabel();
         cpuSide[i].setIcon(GUICard.getBackCardIcon());
         
         humanLabels[i] = new JLabel();
         humanLabels[i].setIcon(GUICard.getIcon(highCardGame.getHand(1).getCard(i)));
         humanLabels[i].addMouseListener(listener);
         
         table.pnlComputerHand.add(cpuSide[i]);
         table.pnlHumanHand.add(humanLabels[i]);
      } 

      playLabelText[0] = new JLabel(p1_TEXT + ": 0");
      playLabelText[0].setHorizontalAlignment(JLabel.CENTER);
      playLabelText[0].setVerticalAlignment(JLabel.TOP);
      
      playLabelText[1] = new JLabel(p2_TEXT + ": 0");
      playLabelText[1].setHorizontalAlignment(JLabel.CENTER);
      playLabelText[1].setVerticalAlignment(JLabel.TOP);
      
      table.pnlPlayerText.add(playLabelText[0]);
      table.pnlPlayerText.add(playLabelText[1]);
      
      statusText.setHorizontalAlignment(JLabel.CENTER);
      table.pnlStatusText.add(statusText);
      statusText.removeMouseListener(listener);
      statusText.setText("");
      statusText.setBorder(null);
      
      table.pnlHumanHand.revalidate();
      table.pnlHumanHand.repaint();
      table.pnlComputerHand.revalidate();
      table.pnlComputerHand.repaint();
      table.pnlPlayArea.revalidate();
      table.pnlPlayArea.repaint();
   } 
   
   
   static int getcpuCard(Card playerCard)
   {
      Card choiceCard = null;
      int cardIndex = 0;
      boolean foundOne = false;
      
      for (int i = 0; i < highCardGame.getHand(0).getNumCards(); i++)
      {
         if (playerCard.compareTo(highCardGame.getHand(0).getCard(i)) < 0)
         {
            //The human card is lower and computer card is higher
            if (choiceCard != null)
            {
               if (choiceCard.compareTo(highCardGame.getHand(0).getCard(i)) > 0)
               {
                  choiceCard = new Card(highCardGame.getHand(0).getCard(i));
                  cardIndex = i;
               } 
            }
            else
            {
               choiceCard = new Card(highCardGame.getHand(0).getCard(i));
               foundOne = true;
               cardIndex = i;
            } 
         }
      }
      
      if (!foundOne)
      {
         for (int i = 0; i < highCardGame.getHand(0).getNumCards(); i++)
         {
            if(playerCard.compareTo(highCardGame.getHand(0).getCard(i)) >= 0)
            {
               if(choiceCard != null)
               {
                  if(choiceCard.compareTo(highCardGame.getHand(0).getCard(i)) > 0)
                  {
                     choiceCard = new Card(highCardGame.getHand(0).getCard(i));
                     cardIndex = i;
                  } 
               }
               else
               {
                  choiceCard = highCardGame.getHand(0).getCard(i);
                  cardIndex = i;
               } 
            }
         }
      } 
      return cardIndex;
   } 
   
   static int getScore(Card[] winnings)
   {
      int score = 0;
      for( Card card : winnings)
      {
    	  if(card!=null)
    	  {
    		  score++;
    	  }
    	  else
    		  break;
      }
      return score;
   }
   
   
   static void removeLabel(JLabel[] labels, JLabel label)
   {
	   int shiftStartPos=0;
      boolean shift = false;
      
      for (int i = 0; i < labels.length; i++)
      {
         if (labels[i] == label)
         {
            labels[i] = null;
            shift = true;
            shiftStartPos=i;
         }
         
      }
      for(int i=shiftStartPos+1; i<labels.length; i++)
      {
    	  labels[i-1]=labels[i];
      }
   }
   
   
   static void addToWinnings(Card[] winnings, Card... cards)
   {
      for (int i = 0; i < cards.length; i++)
         for(int j = 0; j < winnings.length; j++)
            if(winnings[j] == null)
            {
               winnings[j] = new Card(cards[i]);
               break;
            } 
   } 
   
   
   static void playCard(int handPosition)
   {
      table.pnlPlayedCards.removeAll();

      Card humanCard = highCardGame.getHand(1).getCard(handPosition);
      int cpuPosition = getcpuCard(humanCard);
      Card cpuCard = highCardGame.getHand(0).getCard(cpuPosition);
      
      JLabel cpuCardLabel = new JLabel();
      cpuCardLabel.setIcon(GUICard.getIcon(cpuCard));
      cpuCardLabel.setHorizontalAlignment(JLabel.CENTER);
      cpuCardLabel.setVerticalAlignment(JLabel.BOTTOM);

      
      cpuSide[0].setHorizontalAlignment(JLabel.CENTER);
      humanLabels[handPosition].setHorizontalAlignment(JLabel.CENTER);
      humanLabels[handPosition].setVerticalAlignment((JLabel.BOTTOM));

      table.pnlPlayedCards.add(cpuCardLabel);
      table.pnlPlayedCards.add(humanLabels[handPosition]);
      humanLabels[handPosition].removeMouseListener(listener);
      humanLabels[handPosition].setBorder(null);
      
      table.pnlHumanHand.remove(humanLabels[handPosition]);
      table.pnlComputerHand.remove(cpuSide[cpuPosition]);
      highCardGame.getHand(0).withdrawCard(cpuPosition);
      highCardGame.getHand(1).withdrawCard(handPosition);

      
      removeLabel(humanLabels, humanLabels[handPosition]);
      removeLabel(cpuSide, cpuSide[cpuPosition]);

      if (humanCard.compareTo(cpuCard) < 0)
      {
    	  addToWinnings(p2Winnings, cpuCard, humanCard);
          statusText.setText("Computer wins...!");
         
      }
      else if (humanCard.compareTo(cpuCard) > 0)
      {
    	  addToWinnings(p1Winnings, cpuCard, humanCard);
          statusText.setText("You win!");
      }
      else
         statusText.setText("Draw! The cards have been discarded.");
      
      playLabelText[0].setText(p1_TEXT + ": "+ getScore(p1Winnings));
      playLabelText[1].setText(p2_TEXT + ":"+ getScore(p2Winnings));

      if (highCardGame.getHand(0).getNumCards() == 0)
      {
         // All the cards are finished, game is over.
         if (getScore(p1Winnings) > getScore(p2Winnings))
         {
            statusText.setText("You Lost :(");
         }
         else if (getScore(p1Winnings) < getScore(p2Winnings))
         {
            statusText.setText("Winner!");
         }
         else
         {
            statusText.setText("Draw game!");
         }
         
         statusText.setText(statusText.getText()+ " Start over");
         statusText.addMouseListener(listener);
      }

      table.pnlHumanHand.revalidate();
      table.pnlHumanHand.repaint();
      table.pnlComputerHand.revalidate();
      table.pnlComputerHand.repaint();
      table.pnlPlayArea.revalidate();
      table.pnlPlayArea.repaint();
   }
} 