
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.io.File;
import java.lang.Comparable;

class Card //implements Comparable
{

   public enum Suit {clubs, diamonds, hearts, spades};
   boolean flag;
   private char val;
   private Suit suitVal;
   
   public static char[] values = {'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X'};
   public static char[] suits = {'C', 'D', 'H', 'S'};
  // public static char[] valueRanks = validCardValues;

   public int compare(Object obj)
   {
      Card test = (Card) obj;
      
      if (test.getClass() != this.getClass())
         return 1;

      String strRanks = new String(values);
      int index1 =strRanks.indexOf(test.getValue());
      int index2= strRanks.indexOf(this.getValue());
      if (index1 < 0)
         return 1;
      if (index1 < index2)
         return 1;
      if (index1 == index2)
         return 0;
      if (index1 > index2)
         return -1;
      return 1;
   } 
   
   
    

   
   public Card()
   {
      this.set('A', Suit.spades);
   } 
   
   public Card(Card card)
   {
      this.set(card.val, card.suitVal);
   } 
   
   public Card(char value, Suit suit)
   {
      this.set(value, suit);
   } 
   
   
   public boolean set(char value, Suit suit)
   {
      if (Card.isValid(value, suit))
      {
         this.flag = false;
         this.val = value;
         this.suitVal = suit;
         return true;
      }
      else
      {
         this.flag = true;
         return false;
      }
   } 
   
   
   private static boolean isValid(char value, Suit suit)
   {
      for (char validValue : Card.values)
         if (String.valueOf(validValue).toLowerCase().equals(String
            .valueOf(value).toLowerCase()))
            return true;
      
      return false;
   } 
   
   public char getValue() {
      return val;
   }


   public Suit getSuit()
   {
      return this.suitVal;
   } 
   
   public String toString()
   {
      if (this.flag == true)
         return "[INVALID CARD]";
      else
         return this.val + " of " + suitVal.toString();
   } 
   
   public boolean equals(Card c)
   {
      if (this.getValue() == c.getValue() && this.getSuit() == c.getSuit())
         return true;
      
      return false;
   } 
   
   static void sort(Card[] cards, int numCards)
   {
      boolean replace = false;      
      do
      {
         replace = false;

         for (int i = 1; i < numCards; i++)
         {
            if (cards[i - 1].compare(cards[i]) > 0)
            {
               Card tmpCard = new Card(cards[i - 1]);
               cards[i - 1] = new Card(cards[i]);
               cards[i] = new Card(tmpCard);
               replace = true;
            }
         } 
      }
      while (replace);
   } 
} 