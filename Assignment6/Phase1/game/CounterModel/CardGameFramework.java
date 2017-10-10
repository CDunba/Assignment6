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


public class CardGameFramework
{
   private static final int MAX_PLAYERS = 50;

   private int numPlayers;
   private int numPacks;                                               
   private int numJokersPerPack;   
   private int numUnusedCardsPerPack; 
   private int numCardsPerHand;   
   private Deck deck;                                                   
   private Hand[] hand;              
   private Card[] unusedCardsPerPack;
                                     
                                 
   public CardGameFramework( int numPacks, int numJokersPerPack,int numUnusedCardsPerPack,  Card[] unusedCardsPerPack,
      int numPlayers, int numCardsPerHand)
   {
      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;
      if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
         numPlayers = 4;
      if (numJokersPerPack < 0 || numJokersPerPack > 4)
          numJokersPerPack = 0;
      if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) 
      {
       		numUnusedCardsPerPack = 0;
      }
      if (numCardsPerHand < 1 ||numCardsPerHand >  numPacks * (52 - numUnusedCardsPerPack)/ numPlayers )
      {
    	  numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;
      }

      // assign the values 
      this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
      this.hand = new Hand[numPlayers];
      for (int i = 0; i < numPlayers; i++)
      {
    	  this.hand[i] = new Hand(); 
      }     
      deck = new Deck(numPacks);

      this.numPacks = numPacks;      
      this.numPlayers = numPlayers;
      this.numCardsPerHand = numCardsPerHand;
      this.numJokersPerPack = numJokersPerPack;
      this.numUnusedCardsPerPack = numUnusedCardsPerPack;
      for (int i = 0; i < numUnusedCardsPerPack; i++)
      {
    	  this.unusedCardsPerPack[i] = unusedCardsPerPack[i]; 
      }
      startNewGame();
   }


   public CardGameFramework()
   {
      this(1, 0, 0, null, 2, 26);
   }
   

   public Hand getHand(int index)
   {
      if (index < 0 || index >= numPlayers)
      {
    	  return new Hand();
      }
      return hand[index];
    } 

   
   public Card getCardFromDeck()
   {
      return deck.dealCard();
   } 
   

   public int getNumCardsRemainingInDeck()
   {
      return deck.getNumCards();
   } 
   

   public void startNewGame()
   {
      int i, j;
      for (i = 0; i < numPlayers; i++)
      {
    	  hand[i].resetHand();
      }
      deck.init(numPacks);

      for (i = 0; i < numPacks; i++)
      {
    	  for ( j = 0; j < numJokersPerPack; j++)
    	  {
    		  Card joker= new Card('X', Card.Suit.values()[j]);
    		  deck.addCard(joker);
    	  }  
      }
      deck.shuffle();
   }

   
   public boolean deal()
   {
      int numPlayerCards, player;
      boolean deckHasCards=true;;

      for (player = 0; player < numPlayers; player++)
         hand[player].resetHand();
      
      for (numPlayerCards = 0; numPlayerCards < numCardsPerHand && deckHasCards ; numPlayerCards++)
      {
         for (player = 0; player < numPlayers; player++)
         {
        	 if (deck.getNumCards() > 0)
        	 {
        		 hand[player].addCard(deck.dealCard());
        	 }    
             else
             {
                deckHasCards = false;
                break;
             }
         }
      } 
      return deckHasCards;
   }

   void sortHands()
   {
      int i;

      for (i = 0; i < numPlayers; i++)
         hand[i].sort();
   } 
}