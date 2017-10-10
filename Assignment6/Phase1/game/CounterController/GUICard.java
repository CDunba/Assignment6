
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

class GUICard
{
   private static Icon[][] iconCards = new ImageIcon[14][4];
   private static Icon iconBack;
   static boolean iconsLoaded = false;
   static final char[] VALID_SUITS = {'C', 'D', 'H', 'S'};
   private static String iconFolderPath = "./images";
   
   public static Icon getIcon(Card card)
   {
      if(!GUICard.iconsLoaded)
         GUICard.loadCardIcons();
      
      return iconCards[valueAsInt(card)][suitAsInt(card)];
   } 
   
   
   private static void loadCardIcons()
   {
      for (int i = 0; i < Card.validCardValues.length; i++)
         for (int j = 0; j < VALID_SUITS.length; j++)
         {
            iconCards[i][j] = new ImageIcon(iconFolderPath + "/"+ Card.validCardValues[i] + VALID_SUITS[j] + ".gif");
         } 
      iconBack = new ImageIcon(iconFolderPath + "/BK.gif");
      GUICard.iconsLoaded = true;
   } 
   
   
   private static int valueAsInt(Card card)
   {
      String values = new String(Card.validCardValues);
      
      return values.indexOf(card.getValue());
   } 
   
   private static int suitAsInt(Card card)
   {
      return card.getSuit().ordinal();
   }
   
   public static Icon getBackCardIcon()
   {
      if(!GUICard.iconsLoaded)
         GUICard.loadCardIcons();
      
      return GUICard.iconBack;
   } 
}