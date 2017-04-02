/* Courtney Hunt JAVA 1B Assignment 5 Phase 3 Foothill College
 * 
 * This program is the third phase of a playing card game the class is
 * developing through assignments. This is a display-only program and no
 * actual game is played. In this phase, the CardFrameWork class is added and
 * utilized to create a game. A computer player and a human player are dealt
 * 7 random cards and are displayed in a GUI interface showing the Game Room
 * and three framed panels: the computer player at the top,
 * the playing area of 2 cards only in the middle and the human player at
 * the bottom.
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class Foothill
{
   static final int NUM_CARDS_PER_HAND = 7;
   static final int  NUM_PLAYERS = 2;
   static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];  
   static JLabel[] playedCardLabels  = new JLabel[NUM_PLAYERS]; 
   static JLabel[] playLabelText  = new JLabel[NUM_PLAYERS]; 
   static int numPacksPerDeck = 1;
   static int numJokersPerPack = 0;
   static int numUnusedCardsPerPack = 0;
   static Card[] unusedCardsPerPack = null;

   public static void main(String[] args)
   {
      // create a game
      CardGameFramework toyGame = new CardGameFramework( 
            numPacksPerDeck, numJokersPerPack,  
            numUnusedCardsPerPack, unusedCardsPerPack, 
            NUM_PLAYERS, NUM_CARDS_PER_HAND);

      // deal cards to each hand
      toyGame.deal();

      // establish main frame in which program will run
      CardTable table1 = new CardTable("CS 1B CardTable",
            NUM_CARDS_PER_HAND, NUM_PLAYERS);
      table1.setSize(650, 680);
      table1.setLocationRelativeTo(null);
      table1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // place cards into the Human Hand panel and the Computer Hand Panel
      for (int k = 0; k < NUM_CARDS_PER_HAND; k++)
      {
         humanLabels[k] =
               new JLabel(GUICard.getIcon(toyGame.getHand(1).inspectCard(k)));
         table1.pnlHumanHand.add(humanLabels[k]);
         computerLabels[k] = new JLabel(GUICard.getBackCardIcon());
         table1.pnlComputerHand.add(computerLabels[k]);
      }

      // place cards and text into the Playing Area panel
      Card playAreaJoker = new Card('X', Card.Suit.clubs);
      for (int k = 0; k < NUM_PLAYERS; k++)
      {
         playedCardLabels[k] = new JLabel(GUICard.getIcon(playAreaJoker));
         table1.pnlPlayArea.add(playedCardLabels[k]);
      }
      playLabelText[0] = new JLabel("Computer", JLabel.CENTER);
      playLabelText[1] = new JLabel("You", JLabel.CENTER);
      for (int k = 0; k < NUM_PLAYERS; k++)
         table1.pnlPlayArea.add(playLabelText[k]);

      // show everything to the user
      table1.setVisible(true);
   }

   static Card generateRandomCard()
   {
      Card.Suit suit;
      char val;

      int suitSelector, valSelector;

      // get random suit and value
      suitSelector = (int) (Math.random() * 4);
      valSelector = (int) (Math.random() * 14);

      // pick suit
      suit = Card.Suit.values()[suitSelector];

      // pick value
      valSelector++;   // put in range 1-14
      switch(valSelector)
      {
      case 1:
         val = 'A';
         break;
      case 10:
         val = 'T';
         break;
      case 11:
         val = 'J';
         break;
      case 12:
         val = 'Q';
         break;
      case 13:
         val = 'K';
         break;
      case 14:
         val = 'X';
         break;
      default:
         val = (char)('0' + valSelector);   // simple way to turn n into 'n'   
      }

      return new Card(val, suit);
   }
}

class CardTable extends JFrame
{
   static final int MIN_CARDS_PER_HAND = 2, MAX_CARDS_PER_HAND = 56;
   static final int MIN_PLAYERS = 2, MAX_PLAYERS = 2;

   private int numCardsPerHand;
   private int numPlayers;

   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;

   // 3-parameter constructor
   public CardTable(String title, int numCardsPerHand, int numPlayers)
   {
      // pass the title up to the JFrame constructor
      super(title);

      // validate input
      if (numCardsPerHand >= MIN_PLAYERS && numCardsPerHand <= MAX_PLAYERS)
         this.numCardsPerHand = numCardsPerHand;

      if (numCardsPerHand >= MIN_CARDS_PER_HAND
            && numCardsPerHand <= MAX_CARDS_PER_HAND)
         this.numCardsPerHand = numCardsPerHand;

      // set up main components for the JFrame
      pnlComputerHand = new JPanel();
      pnlHumanHand = new JPanel();
      pnlPlayArea = new JPanel();


      // use grid layout and put three above components in frame
      setLayout(new GridLayout(3, 2));
      pnlPlayArea.setLayout(new GridLayout(2, 2));
      add(pnlComputerHand);
      add(pnlPlayArea);
      add(pnlHumanHand);

      // place borders around three sub-panels
      pnlComputerHand.setBorder(new TitledBorder("Computer Hand"));
      pnlPlayArea.setBorder(new TitledBorder("Playing Area"));
      pnlHumanHand.setBorder(new TitledBorder("Your Hand"));
   }

   // accessors
   public int getNumCardsPerHand()
   {
      return numCardsPerHand;
   }

   public int getNumPlayers()
   {
      return numPlayers;
   }
}

class GUICard
{
   static Icon[][] iconCards = new ImageIcon[14][4]; // 14 = A thru K + joker
   private static Icon iconBack;
   static boolean iconsLoaded = false;
   private static final char[] CARD_SUITS = {'C', 'D', 'H', 'S'};

   static void loadCardIcons()
   {
      // execute this method only once check
      if(iconsLoaded)
         return;

      for (int j = 0; j < CARD_SUITS.length; j++)
      {
         for (int k = 0; k < Card.valueRanks.length; k++)
         {
            iconCards[k][j] = new ImageIcon("images/"
                  + Card.valueRanks[k] + CARD_SUITS[j] + ".gif");
         }
      }
      iconBack = new ImageIcon("images/BK.gif");
      iconsLoaded = true;
   }

   // accessors
   static public Icon getIcon(Card card)
   {
      loadCardIcons(); // will not load twice, so no worries.
      return iconCards[valueAsInt(card)][suitAsInt(card)];
   }

   static public Icon getBackCardIcon()
   {
      return iconBack;
   }

   // value conversion helpers
   static private int valueAsInt(Card card)
   {
      int i;
      for (i = 0; i < Card.valueRanks.length; i++)
      {
         if (Card.valueRanks[i] == card.getVal())
            break; 
      }
      return i;
   }

   static private int suitAsInt(Card card)
   {
      int i = 0;
      for (Card.Suit suit : Card.Suit.values())
      {
         if (suit == card.getSuit())
            break;
         i++;
      }
      return i;
   }
}        

//class Card  ----------------------------------------------------------------
class Card
{   
   // type and constants
   public enum State {deleted, active} // not bool because later we may expand
   public enum Suit { clubs, diamonds, hearts, spades }

   // for sort.  
   public static char[] valueRanks = { '2', '3', '4', '5', '6', '7', '8', '9',
         'T', 'J', 'Q', 'K', 'A', 'X'};
   static Suit[] suitRanks = {Suit.clubs, Suit.diamonds, Suit.hearts,
         Suit.spades};
   static int numValsInOrderingArray = 14;  // 'X' = Joker

   // private data
   private char value;
   private Suit suit;
   State state;
   boolean errorFlag;

   // 4 overloaded constructors
   public Card(char value, Suit suit)
   {
      set(value, suit);
   }

   public Card(char value)
   {
      this(value, Suit.spades);
   }

   public Card()
   {
      this('A', Suit.spades);
   }
   // copy constructor
   public Card(Card card)
   {
      this(card.value, card.suit);
   }

   // mutators
   public boolean set(char value, Suit suit)
   {
      char upVal; // for upcasing char

      // can't really have an error here
      this.suit = suit;  

      // convert to uppercase to simplify
      upVal = Character.toUpperCase(value);

      // check for validity
      if (
            upVal == 'A' || upVal == 'K'
            || upVal == 'Q' || upVal == 'J'
            || upVal == 'T' || upVal == 'X'
            || (upVal >= '2' && upVal <= '9')
            )
      {
         errorFlag = false;
         state = State.active;
         this.value = upVal;
      }
      else
      {
         errorFlag = true;
         return false;
      }

      return !errorFlag;
   }

   public void setState( State state)
   {
      this.state = state;
   }

   // accessors
   public char getVal()
   {
      return value;
   }

   public Suit getSuit()
   {
      return suit;
   }

   public State getState()
   {
      return state;
   }

   public boolean getErrorFlag()
   {
      return errorFlag;
   }

   // stringizer
   public String toString()
   {
      String retVal;

      if (errorFlag)
         return "** illegal **";
      if (state == State.deleted)
         return "( deleted )";

      // else implied

      if (value != 'X')
      {
         // not a joker
         retVal =  String.valueOf(value);
         retVal += " of ";
         retVal += String.valueOf(suit);
      }
      else
      {
         // joker
         retVal = "joker";

         if (suit == Suit.clubs)
            retVal += " 1";
         else if (suit == Suit.diamonds)
            retVal += " 2";
         else if (suit == Suit.hearts)
            retVal += " 3";
         else if (suit == Suit.spades)
            retVal += " 4";
      }

      return retVal;
   }

   public boolean equals(Card card)
   {
      if (this.value != card.value)
         return false;
      if (this.suit != card.suit)
         return false;
      if (this.errorFlag != card.errorFlag)
         return false;
      if (this.state != card.state)
         return false;
      return true;
   }

   // sort member methods
   public int compareTo(Card other)
   {
      if (this.value == other.value)
         return ( getSuitRank(this.suit) - getSuitRank(other.suit) );

      return (
            getValueRank(this.value)
            - getValueRank(other.value)
            );
   }

   public static void setRankingOrder(
         char[] valueOrderArr, Suit[] suitOrdeArr,
         int numValsInOrderingArray )
   {
      int k;

      // expects valueOrderArr[] to contain only cards used per pack,
      // including jokers, needed to define order for the game environment

      if (numValsInOrderingArray < 0 || numValsInOrderingArray > 14)
         return;

      Card.numValsInOrderingArray = numValsInOrderingArray;

      for (k = 0; k < numValsInOrderingArray; k++)
         Card.valueRanks[k] = valueOrderArr[k];

      for (k = 0; k < 4; k++)
         Card.suitRanks[k] = suitOrdeArr[k];
   }

   public static int getSuitRank(Suit st)
   {
      int k;

      for (k = 0; k < 4; k++)
         if (suitRanks[k] == st)
            return k;

      // should not happen
      return 0;
   }

   public  static int getValueRank(char val)
   {
      int k;

      for (k = 0; k < numValsInOrderingArray; k++)
         if (valueRanks[k] == val)
            return k;

      // should not happen
      return 0;
   }

   public static void arraySort(Card[] array, int arraySize)
   {
      for (int k = 0; k < arraySize; k++)
         if (!floatLargestToTop(array, arraySize - 1 - k))
            return;
   }

   private static boolean floatLargestToTop(Card[] array, int top)
   {
      boolean changed = false;
      Card temp;

      for (int k = 0; k < top; k++)
         if (array[k].compareTo(array[k+1]) > 0)
         {
            temp = array[k];
            array[k] = array[k+1];
            array[k+1] = temp;
            changed = true;
         }
         return changed;
   }
}

//class Hand  ----------------------------------------------------------------
class Hand
{
   public static final int MAX_CARDS_PER_HAND = 100;  // should cover any game

   private Card[] myCards;
   private int numCards;

   //constructor
   public Hand()
   {
      // careful - we are only allocating the references
      myCards = new Card[MAX_CARDS_PER_HAND];
      resetHand();
   }

   // mutators
   public void resetHand() { numCards = 0; }

   public boolean takeCard(Card card)
   {
      if (numCards >= MAX_CARDS_PER_HAND)
         return false;

      // be frugal - only allocate when needed
      if (myCards[numCards] == null)
         myCards[numCards] = new Card();

      // don't just assign:  mutator assures active/undeleted      
      myCards[numCards++].set( card.getVal(), card.getSuit() );
      return true;
   }

   public Card playCard()
   {
      // always play  highest card in array.  client will prepare this position.
      // in rare case that client tries to play from a spent hand, return error

      Card errorReturn = new Card('E', Card.Suit.spades); // in rare cases

      if (numCards == 0)
         return errorReturn;
      else
         return myCards[--numCards];
   }

   public Card playCard(int cardIndex)
   {
      if ( numCards == 0 ) //error
      {
         //Creates a card that does not work
         return new Card('M', Card.Suit.spades);
      }
      //Decreases numCards.
      Card card = myCards[cardIndex];

      numCards--;
      for(int i = cardIndex; i < numCards; i++)
      {
         myCards[i] = myCards[i+1];
      }

      myCards[numCards] = null;

      return card;
   }

   // accessors
   public String toString()
   {
      int k;
      String retVal = "Hand =  ( ";

      for (k = 0; k < numCards; k++)
      {
         retVal += myCards[k].toString();
         if (k < numCards - 1)
            retVal += ", ";
      }
      retVal += " )";
      return retVal;
   }

   int getNumCards()
   {
      return numCards;
   }

   Card inspectCard(int k)
   {
      // return copy of card at position k.
      // if client tries to access out-of-bounds card, return error

      Card errorReturn = new Card('E', Card.Suit.spades); // in rare cases

      if (k < 0 || k >= numCards)
         return errorReturn;
      else
         return myCards[k];
   }

   void sort()
   {
      // assumes that Card class has been sent ordering (if default not correct)
      Card.arraySort(myCards, numCards);
   }
}

//class Deck  ----------------------------------------------------------------
class Deck
{
   // six full decks (with jokers) is enough for about any game
   private static final int MAX_CARDS_PER_DECK = 6 * 54;
   private static Card[] masterPack;   // one 52-Card master to use for
   // initializing decks
   private Card[] cards;
   private int topCard;
   private int numPacks;

   private static boolean firstTime = true;  // avoid calling allcMstrPck > once

   public Deck()
   {
      this(1);
   }

   public Deck(int numPacks)
   {
      allocateMasterPack();  // do not call from init()
      cards = new Card[MAX_CARDS_PER_DECK];
      init(numPacks);
   }

   static private void allocateMasterPack()
   {
      int j, k;
      Card.Suit st;
      char val;

      // we're in static method; only need once / program: good for whole class
      if ( !firstTime )
         return;
      firstTime = false;

      // allocate
      masterPack = new Card[52];
      for (k = 0; k < 52; k++)
         masterPack[k] = new Card();

      // next set data
      for (k = 0; k < 4; k++)
      {
         // set the suit for this loop pass
         st = Card.Suit.values()[k];

         // now set all the values for this suit
         masterPack[13*k].set('A', st);
         for (val='2', j = 1; val<='9'; val++, j++)
            masterPack[13*k + j].set(val, st);
         masterPack[13*k+9].set('T', st);
         masterPack[13*k+10].set('J', st);
         masterPack[13*k+11].set('Q', st);
         masterPack[13*k+12].set('K', st);
      }
   }

   // set deck from 1 to 6 packs, perfecly ordered
   public void init(int numPacks)
   {
      int k, pack;

      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;

      // hand over the masterPack cards to our deck
      for (pack = 0; pack < numPacks; pack++)
         for (k = 0; k < 52; k++)
            cards[pack*52 + k] = masterPack[k];

      // this was slightly sloppy:  multiple packs point to same master cards
      // if something modified a card, we would be in trouble.  fortunately,
      // we don't expect a card to ever be modified after instantiated
      // in the context of a deck.

      this.numPacks = numPacks;
      topCard = numPacks * 52;
   }

   public void init()
   {
      init(1);
   }

   public int getNumCards()
   {
      return topCard;
   }

   public void shuffle()
   {
      Card tempCard;
      int k, randInt;

      // topCard is size of deck
      for (k = 0; k < topCard; k++)
      {
         randInt = (int)(Math.random() * topCard);

         // swap cards k and randInt (sometimes k == randInt:  okay)
         tempCard = cards[k];
         cards[k] = cards[randInt];
         cards[randInt] = tempCard;
      }
   }

   public Card takeACard()
   {
      return new Card();
   }

   public Card dealCard()
   {
      // always deal the topCard.  
      Card errorReturn = new Card('E', Card.Suit.spades); //  in rare cases

      if (topCard == 0)
         return errorReturn;
      else
         return cards[--topCard];
   }

   public boolean removeCard(Card card)
   {
      int k;
      boolean foundAtLeastOne;

      foundAtLeastOne = false;
      for (k = 0; k < topCard; k++)
      {
         // care: use while, not if, in case we copy to-be-removed from top to k
         while ( cards[k].equals(card) )
         {
            // overwrite card[k] with top of deck, then decrement topCard
            cards[k] = cards[topCard - 1];
            topCard--;
            foundAtLeastOne = true;
            // test because "while" causes topCard to decrease, possibly below k
            if ( k >= topCard )
               break;
         }
      }
      // did above work if k == topCard-1?  think about it
      return foundAtLeastOne;
   }

   public boolean addCard(Card card)
   {
      // don't allow too many copies of this card in the deck
      if (numOccurrences(card) >= numPacks)
         return false;

      cards[topCard++] = card;
      return true;
   }

   public Card inspectCard(int k)
   {
      // return copy of card at position k.
      // if client tries to access out-of-bounds card, return error

      Card errorReturn = new Card('E', Card.Suit.spades); //  in rare cases

      if (k < 0 || k >= topCard)
         return errorReturn;
      else
         return cards[k];
   }

   public int numOccurrences(Card card)
   {
      int retVal, k;

      retVal = 0;

      // assumption:  card is a default item:  not deleted and state=active)
      for (k = 0; k < topCard; k++)
      {
         if (inspectCard(k).equals(card))
            retVal++;
      }
      return retVal;
   }

   public String toString()
   {
      int k;
      String retString = "\n";

      for (k = 0; k < topCard; k++)
      {
         retString += cards[k].toString();
         if (k < topCard - 1)
            retString += " / ";
      }
      retString += "\n";

      return retString;
   }

   void sort()
   {
      // assumes that Card class has been sent ordering (if default not correct)
      Card.arraySort(cards, topCard);
   }
}

//class CardGameFramework  ----------------------------------------------------
class CardGameFramework
{
   private static final int MAX_PLAYERS = 50;

   private int numPlayers;
   private int numPacks;            // # standard 52-card packs per deck
   // ignoring jokers or unused cards
   private int numJokersPerPack;    // if 2 per pack & 3 packs per deck, get 6
   private int numUnusedCardsPerPack;  // # cards removed from each pack
   private int numCardsPerHand;        // # cards to deal each player
   private Deck deck;               // holds the initial full deck and gets
   // smaller (usually) during play
   private Hand[] hand;             // one Hand for each player
   private Card[] unusedCardsPerPack;   // an array holding the cards not used
   // in the game.  e.g. pinochle does not
   // use cards 2-8 of any suit

   public CardGameFramework( int numPacks, int numJokersPerPack,
         int numUnusedCardsPerPack,  Card[] unusedCardsPerPack,
         int numPlayers, int numCardsPerHand)
   {
      int k;

      // filter bad values
      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;
      if (numJokersPerPack < 0 || numJokersPerPack > 4)
         numJokersPerPack = 0;
      if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) //  > 1 card
         numUnusedCardsPerPack = 0;
      if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
         numPlayers = 4;
      // one of many ways to assure at least one full deal to all players
      if  (numCardsPerHand < 1 ||
            numCardsPerHand >  numPacks * (52 - numUnusedCardsPerPack)
            / numPlayers )
         numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;

      // allocate
      this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
      this.hand = new Hand[numPlayers];
      for (k = 0; k < numPlayers; k++)
         this.hand[k] = new Hand();
      deck = new Deck(numPacks);

      // assign to members
      this.numPacks = numPacks;
      this.numJokersPerPack = numJokersPerPack;
      this.numUnusedCardsPerPack = numUnusedCardsPerPack;
      this.numPlayers = numPlayers;
      this.numCardsPerHand = numCardsPerHand;
      for (k = 0; k < numUnusedCardsPerPack; k++)
         this.unusedCardsPerPack[k] = unusedCardsPerPack[k];

      // prepare deck and shuffle
      newGame();
   }

   // constructor overload/default for game like bridge
   public CardGameFramework()
   {
      this(1, 0, 0, null, 4, 13);
   }

   public Hand getHand(int k)
   {
      // hands start from 0 like arrays

      // on error return automatic empty hand
      if (k < 0 || k >= numPlayers)
         return new Hand();

      return hand[k];
   }

   public Card getCardFromDeck() { return deck.dealCard(); }

   public int getNumCardsRemainingInDeck() { return deck.getNumCards(); }

   public void newGame()
   {
      int k, j;

      // clear the hands
      for (k = 0; k < numPlayers; k++)
         hand[k].resetHand();

      // restock the deck
      deck.init(numPacks);

      // remove unused cards
      for (k = 0; k < numUnusedCardsPerPack; k++)
         deck.removeCard( unusedCardsPerPack[k] );

      // add jokers
      for (k = 0; k < numPacks; k++)
         for ( j = 0; j < numJokersPerPack; j++)
            deck.addCard( new Card('X', Card.Suit.values()[j]) );

      // shuffle the cards
      deck.shuffle();
   }

   public boolean deal()
   {
      // returns false if not enough cards, but deals what it can
      int k, j;
      boolean enoughCards;

      // clear all hands
      for (j = 0; j < numPlayers; j++)
         hand[j].resetHand();

      enoughCards = true;
      for (k = 0; k < numCardsPerHand && enoughCards ; k++)
      {
         for (j = 0; j < numPlayers; j++)
            if (deck.getNumCards() > 0)
               hand[j].takeCard( deck.dealCard() );
            else
            {
               enoughCards = false;
               break;
            }
      }

      return enoughCards;
   }

   void sortHands()
   {
      int k;

      for (k = 0; k < numPlayers; k++)
         hand[k].sort();
   }

   Card playCard(int playerIndex, int cardIndex)
   {
      // returns bad card if either argument is bad
      if (playerIndex < 0 ||  playerIndex > numPlayers - 1 ||
            cardIndex < 0 || cardIndex > numCardsPerHand - 1)
      {
         //Creates a card that does not work
         return new Card('M', Card.Suit.spades);      
      }

      // return the card played
      return hand[playerIndex].playCard(cardIndex);

   }


   boolean takeCard(int playerIndex)
   {
      // returns false if either argument is bad
      if (playerIndex < 0 || playerIndex > numPlayers - 1)
         return false;

      // Are there enough Cards?
      if (deck.getNumCards() <= 0)
         return false;

      return hand[playerIndex].takeCard(deck.dealCard());
   }

}