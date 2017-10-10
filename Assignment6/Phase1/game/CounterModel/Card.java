package game.CounterModel;

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

public class Card implements Comparable
{

   public enum Suit{clubs, diamonds, hearts, spades};

   private char value;
   private Suit suit;
   boolean flag;
    
    public static char[] validCardValues = {'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X'};
    public static char[] valueRanks = validCardValues;

    public Card()
    {
        this.set('A', Suit.spades);
    }
     
    public Card(char value, Suit suit)
    {
       this.set(value, suit);
    }
    
    public Card(Card card)
    {
       this.set(card.value, card.suit);
    }


    public int compareTo(Object testObj)
    {
       int result = 1;

       if(testObj.getClass() != this.getClass())
       {
          result = 1;
       }
       else
       {
          Card cardObj = (Card)testObj;
          String strRanks = new String(valueRanks);
          int testVal=strRanks.indexOf(cardObj.getValue());
          int thisVal=strRanks.indexOf(this.getValue());
          
          if(testVal < 0)
          {
             result = 1;
          }  
          else if(testVal < thisVal)
          {
             result = 1;
          } 
          else if(testVal == thisVal)
          {
             result = 0;
          } 
          else if(testVal > thisVal)
          {
             result = -1;  
          } 
       } 
        return result;  
    } 


    public boolean equals(Card cardObj)
    {
       if((this.getValue() == cardObj.getValue()) && 
             (this.getSuit() == cardObj.getSuit()))
       {
          return true;
       } 
      return false;   
    } 
    //
    public char getValue()
    {
        return value;
    }

    public Suit getSuit()
    {
        return this.suit;
    }

    
    public boolean set(char value, Suit suit)
    {
       boolean result = false;
       
       if(Card.checkValue(value, suit)) 
        {
            this.flag = false;
            this.value = value;
            this.suit = suit;
            result = true;
        } 
        else
        {
            this.flag = true;
            result = false;
        }
       
       return result;
    }

    private static boolean checkValue(char value, Suit suit)
    {
       boolean returnValue = false;
       
       for(char validValue : Card.validCardValues)
       {
          if(String.valueOf(validValue).toLowerCase().equals(
                String.valueOf(value).toLowerCase()))
            {
               returnValue = true;
            }
       }
        return returnValue;
    }
    

    public static void sort(Card[] cards, int numCards)
    {
        boolean exchange = false;

        do {
            exchange = false;
            for(int i = 1; i < numCards; i++)
            {
                if(cards[i-1].compareTo(cards[i]) > 0)
                {
                    Card tmpCard = new Card(cards[i-1]);
                    cards[i-1] = new Card(cards[i]);
                    cards[i] = new Card(tmpCard);
                    exchange = true;
                } 
            }
        } while(exchange);
    }   
} 