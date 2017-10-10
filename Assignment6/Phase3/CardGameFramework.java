import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

import java.awt.event.*;
import java.io.File;
import java.lang.Comparable;


class CardGameFramework
{
   private static final int MAX_PLAYERS = 50;

   private int numPlayers;
   private int numPacks;              
                                      
   private int JokersPerPack;     
   private int UnusedCardsPerPack; 
   private int CardsPerHand;       
   private Deck deck;                
                                      
   private Hand[] hand;               
   private Card[] unusedCardsPerPack; 
                                      

   Card playCard(int index1, int index2)
   {
	   if (index1 < 0 ||  index1 > numPlayers - 1 ||
			   index2 < 0 || index2 > CardsPerHand - 1)
		      {
		         return new Card('M', Card.Suit.spades);
		      }
		      return hand[index1].playCard(index2);
   } 
   
   boolean takeCard(int index1, int index2)
   {
	   if (index1 < 0 ||  index1 > numPlayers - 1 ||
			   index2 < 0 || index2 > CardsPerHand - 1)
		      {
		         return false;
		      }
		      if (deck.getNumCards() <= 0)
		         return false;

		      return hand[index1].addCard(deck.dealCard());
   }
   
   public CardGameFramework(int numPacks, int JokersPerPack, int UnusedCardsPerPack, 
		   Card[] unusedCardsPerPack, int numPlayers, int CardsPerHand)
   {
	   if (numPacks < 1 || numPacks > 6)
	         numPacks = 1;
	      if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
	         numPlayers = 4;
	      if (JokersPerPack < 0 || JokersPerPack > 4)
	          JokersPerPack = 0;
	      if (UnusedCardsPerPack < 0 || UnusedCardsPerPack > 50) 
	      {
	       		UnusedCardsPerPack = 0;
	      }
	      if (CardsPerHand < 1 ||CardsPerHand >  numPacks * (52 - UnusedCardsPerPack)/ numPlayers )
	      {
	    	  CardsPerHand = numPacks * (52 - UnusedCardsPerPack) / numPlayers;
	      }

	      // assign the values 
	      this.unusedCardsPerPack = new Card[UnusedCardsPerPack];
	      this.hand = new Hand[numPlayers];
	      for (int i = 0; i < numPlayers; i++)
	      {
	    	  this.hand[i] = new Hand(); 
	      }     
	      deck = new Deck(numPacks);

	      this.numPacks = numPacks;      
	      this.numPlayers = numPlayers;
	      this.CardsPerHand = CardsPerHand;
	      this.JokersPerPack = JokersPerPack;
	      this.UnusedCardsPerPack = UnusedCardsPerPack;
	      for (int i = 0; i < UnusedCardsPerPack; i++)
	      {
	    	  this.unusedCardsPerPack[i] = unusedCardsPerPack[i]; 
	      }
	      startNewGame();   
   }
   
   public CardGameFramework()
   {
	   this(1, 0, 0, null, 2, 26);
   }

   public Hand getHand(int k)
   {
      if (k < 0 || k >= numPlayers)
         return new Hand();

      return hand[k];
   }
   public Card getCardFromDeck()
   {
      return deck.dealCard();
   } 

   public int getNumCardsRemainingInDeck()
   {
      return deck.getNumCards();
   }

   

   public boolean deal()
   {

	   int numPlayerCards, player;
	      boolean deckHasCards=true;;

	      for (player = 0; player < numPlayers; player++)
	         hand[player].resetHand();
	      
	      for (numPlayerCards = 0; numPlayerCards < CardsPerHand && deckHasCards ; numPlayerCards++)
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
      int k;

      for (k = 0; k < numPlayers; k++)
         hand[k].sort();
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
	    	  for ( j = 0; j < JokersPerPack; j++)
	    	  {
	    		  Card joker= new Card('X', Card.Suit.values()[j]);
	    		  deck.addCard(joker);
	    	  }  
	      }
	      deck.shuffle();
   } 
}