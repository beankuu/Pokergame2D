package model;

import objects.Card;
import objects.Deck;

///////////////////Hand.java///////////////////

public class Hand_GUI extends Hand{
	public Hand_GUI() {super();}
	public Card[] getCard() {
		return super.cards.clone();
	}
}
