
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.io.File;
import java.lang.Comparable;

class CardStack
{
   Card[] cards;
   int numCards;
   
   //constructor to initialize cards array
   CardStack(int maxCards)
   {
      cards = new Card[maxCards];
      numCards = 0;
   }
   
   public boolean addCard(Card card)
   {
      if (numCards == cards.length)
         return false;
      
      cards[numCards] = card;
      numCards++;
      
      return true;
   } 
   
   public Card returnTopCard()
   {
      if (numCards == 0) 
      {
    	  return new Card('M', Card.Suit.spades);
      }
      else
      {
    	  return new Card(cards[numCards - 1]);
      }
   } 
} 