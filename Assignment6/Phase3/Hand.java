import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.io.File;
import java.lang.Comparable;


class Hand
{
   public static final int MAX_CARDS = 50;
   private Card[] myCards = new Card[MAX_CARDS];
   int cardsCount = 0;

   //public Hand()
   //{
   
  // } 
   
  
   
   public void resetHand()
   {
      this.myCards = new Card[MAX_CARDS];
      this.cardsCount = 0;
   } 
   
   public boolean addCard(Card card)
   {
      if (this.cardsCount >= MAX_CARDS)
         return false;
      else
      {
         this.myCards[cardsCount] = new Card(card);
         this.cardsCount++;
         return true;
      }
   }
   
   /*public Card playCard()
   {
      Card card = this.myCards[this.numCards - 1];
      this.myCards[this.numCards - 1] = null;
      this.numCards--;
      return card;
   }
   
   public String toString()
   {
      String handString = "( ";
      
      for (int i = 0; i < this.numCards; i++)
      {
         handString += this.myCards[i].toString();
         
         if (i != this.numCards - 1)
            handString += ", ";
      }
      
      handString += " )";
      
      return handString;
   } 
   */
   public int getNumCards()
   {
      return this.cardsCount;
   }
   
   public Card inspectCard(int k)
   {
      if (k >= this.cardsCount || k < 0)
         return new Card('0', Card.Suit.spades);
      else
         return new Card(this.myCards[k]);
   } 
   
   public Card playCard(int k)
   {
      
      if (k >= this.cardsCount || k < 0)
         return new Card('0', Card.Suit.spades);
      else
      {

         Card card = new Card(this.myCards[k]);
         
         for (int i = k + 1; i < this.cardsCount; i++)
         {
            this.myCards[i - 1] = this.myCards[i];
            this.myCards[i] = null;
         } 
         
         this.cardsCount--;
         return card;
      }
   }
   void sort()
   {
      Card.sort(this.myCards, cardsCount);
   } 
} 
