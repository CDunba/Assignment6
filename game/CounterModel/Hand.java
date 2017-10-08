package game.CounterModel;
public class Hand 
{
   public static final int MAX_CARDS = 50;      

   private Card[] myCards = new Card[MAX_CARDS];
   int numCards = 0;

    
     public int getNumCards()
     {  
        return this.numCards;  
     } 
     
     
    public void resetHand()
    {
        this.myCards = new Card[MAX_CARDS];
        this.numCards = 0;
    } 
    

    public void sort()
    {
        Card.sort(this.myCards, numCards);
    }
   
    
    public boolean addCard(Card card)
    {
        if(this.numCards >= MAX_CARDS)
        {
           return false;           
        }

        else 
        {
            this.myCards[numCards] = new Card(card);
            this.numCards++;
            return true;
        }
    }
       
    public Card getCard(int index)
    {
        if(index >= this.numCards || index < 0)
            return new Card('0', Card.Suit.spades);
        else
            return new Card(this.myCards[index]);
    } 
    
    public Card withdrawCard()
    {
        Card card = this.myCards[this.numCards-1];
        this.myCards[this.numCards-1] = null;
        this.numCards--;
        return card;
    }
   
    public Card withdrawCard(int index)
    {
       if (index >= this.numCards || index < 0)
       {
          return new Card('0', Card.Suit.spades);
       }   
       else
       {
          Card card = new Card(this.myCards[index]);
          
          //now shift the cards to fill vacant place
          for(int i = index+1; i < this.numCards; i++)
          {
             this.myCards[i - 1] = this.myCards[i];
             this.myCards[i] = null;
          } 
          this.numCards--;
          
          return card;
       }
    } 
}
