package controller;

import model.Hand_GUI;
import objects.Bankroll;
import objects.Bet;
import objects.Card;

public class PokerGame_GUI{
	private Bankroll bankroll;
	private Bet bet;
	private Hand_GUI hand;

	public PokerGame_GUI(Bet coinsBet, Bankroll br) {
		bankroll = br;
		bet = coinsBet;
		hand = new Hand_GUI();
	}

	public void updateBankroll(int payoff) {
		int winnings = payoff * (bet.getBet()); // negative for a loss
		bankroll.alterBankroll(winnings);
	}

	public Card[] viewInitialHand() {
		hand.newHand();
		return hand.getCard();
	}
	public Card[] discardOrHoldCards(boolean[] holdCards) {
		hand.updateHand(holdCards);
		updateBankroll(hand.evaluateHand());
		return hand.getCard();
	}
	public int evaluateHand(){
		return hand.evaluateHand();
	}
	public Bankroll getBankRoll(){
		return bankroll;
	}
}
