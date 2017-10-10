
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

import java.awt.event.*;
import java.io.File;
import java.lang.Comparable;
import javax.swing.JOptionPane;

class Deck
{

    public static final short MAX_CARDS_IN_PACK = 56;
    public static final short MAX_PACKS = 6;
    public static final short MAX_CARDS = MAX_PACKS * MAX_CARDS_IN_PACK;
    
    private static Card[] masterPack = new Card[MAX_CARDS_IN_PACK];
    private Card[] cards;  
    private int topCard; 
    private int numPacks;

    public Deck() 
    {
       this.allocateMasterPack();
       this.init(1);
    } 

    public Deck(int numPacks) 
    {
        this.allocateMasterPack();
        if (numPacks > Deck.MAX_PACKS)
        {
           this.init(Deck.MAX_PACKS);
        }
        else if (numPacks < 1)
        {
           this.init(1); 
        }    
        else
        {
           this.init(numPacks);     
        } 
    } 

    public int getNumCards()
    {
        return this.topCard;
    }

    public int getTopCard() 
    {
        return this.topCard;
    }

    public void init(int numPacks) 
    {
        this.cards = new Card[numPacks * Deck.MAX_CARDS_IN_PACK];
        int count=0;
        for(int j=0; j<numPacks; j++)
        {
        	for(int i=0; i<Deck.MAX_CARDS_IN_PACK; i++)
        	{
        		this.cards[count]=this.masterPack[i];
        		count++;
        	}
        }
        this.topCard = numPacks * Deck.MAX_CARDS_IN_PACK;
    } 


    public void shuffle() 
    {
       for (int i = this.topCard - 1; i >= 0; i--) 
       {
          Card tmpCard = this.cards[i];
          int randomPosition = (int) (Math.random() * (this.topCard - 1));
          this.cards[i] = this.cards[randomPosition];
          this.cards[randomPosition] = tmpCard;
        } 
    } 
   
    
    public Card dealCard() 
    {
    	if (this.topCard <= 0)
            return new Card('0', Card.Suit.spades);
         else
         {
            Card card = new Card(this.cards[this.topCard - 1]);
            this.cards[this.topCard - 1] = null;
            this.topCard--;
            return card;
         } 
    } 
    
   
    public Card inspectCard(int k) 
    {
        if (k >= this.topCard || k < 0)
        {
           return new Card('0', Card.Suit.spades);
        }      
        else
        {
           return new Card(this.cards[k]);
          
        } 
    } 
    
    
    private static void allocateMasterPack() 
    {
    	int count=0;
        if (Deck.masterPack != null) 
        {
           for (int i = 0; i < Card.Suit.values().length; i++) 
           {
              for (int j = 0; j < Card.values.length; j++) 
              {
                 Deck.masterPack[count] = new Card(Card.values[j], Card.Suit.values()[i]);
                 count++;
                }
            }
        } 
    }

   
    
    public boolean addCard(Card card)
    {
        int cardCount = 0;
        for(Card cardInDeck : this.cards)
        {
           if(cardInDeck.equals(card))
           {
              cardCount++;
           } 
        }   
        if((cardCount >= this.numPacks) || 
              (this.topCard >= this.MAX_CARDS))
        {
           return false;
        }
        this.cards[topCard]=new Card(card);
        return true;
    }

    
    
   public boolean removeCard(Card card)
    {
        for(int i = 0; i < this.cards.length; i++)
        {
           if(this.cards[i].equals(card))
           {
              this.cards[i] = new Card(this.cards[topCard - 1]);
              this.topCard--;
              return true;   
           }        
        }    
        return false;
    }
   

   public void sort()
    {
        Card.sort(this.cards, this.topCard);
    } 
} 