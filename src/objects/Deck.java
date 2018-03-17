package objects;

///////////////////Deck.java///////////////////

import java.util.*; // for Random

public class Deck {
	private Card deck[];
	private int next; // holds position of next card to be dealt

	public Deck() {
		deck = new Card[53]; // does not use position 0, uses 1..52

		for (int rank = 1; rank <= 13; rank++) {
			// place cards in order in deck
			deck[rank] = new Card(1, rank); // "rank of first suit" e.g. 3 of
											// hearts
			deck[rank + 13] = new Card(2, rank); // rank of second suit e.g. 3
													// of diamonds
			deck[rank + 26] = new Card(3, rank); // "rank of third suit" e.g 3
													// of clubs
			deck[rank + 39] = new Card(4, rank); // "rank of fourth suit" e.g. 3
													// of spades
		}
		next = 1;
	}

	public void shuffle() {
		Random randomNumber = new Random();
		for (int card = 1; card <= 52; card++) {
			// find a random place in the deck
			int rand = randomNumber.nextInt(52) + 1;
			// swap deck[i] with deck[m]
			Card temp = deck[card];
			deck[card] = deck[rand];
			deck[rand] = temp;
		}
		next = 1; // top card of the deck
	}

	public Card deal() {
		if (next > 52) // if deck is depleted
			shuffle();
		Card c = deck[next];
		next++;
		return c;
	}
}
