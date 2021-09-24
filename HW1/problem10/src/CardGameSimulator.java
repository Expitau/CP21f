public class CardGameSimulator{
  private static final Player[] players = new Player[2];

  public static void simulateCardGame(String inputA, String inputB){
    // DO NOT change the skeleton code.
    // You can add codes anywhere you want.
    players[0] = new Player("A", inputA);
		players[1] = new Player("B", inputB);

		int now = 0;
    Card prev = players[now].getMax();
		players[now].playCard(prev);
    players[now].removeCard(prev);

    while(prev.isValid()){
      now = 1 - now;
			prev = players[now].getNext(prev);
			if(prev.isValid()){
				players[now].playCard(prev);
				players[now].removeCard(prev);
			}else{
				printWinMessage(players[1-now]);
			}
    }
  }

	private static void printWinMessage(Player player) {
		System.out.printf("Player %s wins the game!\n", player);
	}
}


class Player{
  private String name;
  private Card[] deck;

	Player(){
		name = "";
		deck = new Card[0];
	}

	Player(String name, Card[] deck){
		setDeck(deck);
		setName(name);
	}

	Player(String name, String deckInput){
		Card[] deck = new Card[10];
		String[] deck_string = deckInput.split(" ");

		for(int i = 0; i < deck_string.length; i++) deck[i] = Card.stringToCard(deck_string[i]);

		setDeck(deck);
		setName(name);
	}

  public void playCard(Card card){
    System.out.printf("Player %s: %s\n", name, card);
  }

  public void setName(String name){
    this.name = name;
  }

  public void setDeck(Card[] cards){
    this.deck = cards;
  }

  public void removeCard(Card card){
    Card[] newDeck = new Card[deck.length - 1];
    int flag = 0;
    for(int i = 0; i < deck.length; i++){
      if(deck[i].equals(card)) flag = 1;
      else newDeck[i - flag] = deck[i];
    }
    setDeck(newDeck);
  }

  public Card getMax(){
    Card temp = deck[0];
    for(Card card : deck){
      if(card.isBigger(temp)) temp = card;
    }
    return temp;
  }

  public Card getNext(Card card){
    if(deck.length == 0) return new Card();

    for(Card value : deck){
      if(value.isSameNumber(card)) return value;
    }
    Card temp = new Card();
    for(Card value : deck){
      if(value.isSameShape(card) && value.isBigger(temp)){
        temp = value;
      }
    }
    return temp;
  }

  @Override
  public String toString(){
    return name;
  }
}


class Card{
  private int number;
  private char shape;

  Card(){
    number = -1;
    shape = 'O';
  }

  Card(int num, char shp){
    number = num;
    shape = shp;
  }

  public static Card stringToCard(String str){
    return new Card(str.charAt(0) - '0', str.charAt(1));
  }

  public boolean equals(Card card){
    return (number == card.number) && (shape == card.shape);
  }

  public boolean isBigger(Card card){
    if(card.number != number) return number > card.number;
    if(shape == 'X') return true;
    return false;
  }

  public boolean isSameNumber(Card card){
    return number == card.number;
  }

  public boolean isSameShape(Card card){
    return shape == card.shape;
  }

  public boolean isValid(){
    return number != -1;
  }

  @Override
  public String toString(){
    return "" + number + shape;
  }
}
