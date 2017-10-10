package game.CounterController;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

import game.CounterModel.Deck;
import game.CounterModel.Card;
import game.CounterModel.Card.Suit;

import java.awt.event.*;
import java.io.File;
import java.lang.Comparable;
import javax.swing.JOptionPane;

public class CardListener implements MouseListener
{
   public void mouseEntered(MouseEvent e)
   {
      JLabel card = (JLabel)e.getSource();
      LineBorder border = new LineBorder(new Color(255, 0, 255), 4);
      card.setBorder(border);
   }
   
   public void mouseExited(MouseEvent e)
   {
      JLabel card = (JLabel)e.getSource();
      card.setBorder(null);
   } 
   
   public void mouseClicked(MouseEvent e)
   {
      JLabel card = (JLabel)e.getSource();
      
      for (int i = 0; i < MainPhase3.humanLabels.length; i++)
      {
          if (MainPhase3.humanLabels[i] == card)
          {
             MainPhase3.playCard(i);
             break;
          }  
      }

      // will lead to restart of the game
      if (MainPhase3.statusText == card)
      {
         MainPhase3.initGame();
      }
   } 
   
   public void mouseReleased(MouseEvent e)
   {

   } 
   
   public void mousePressed(MouseEvent e)
   {

   } 
}