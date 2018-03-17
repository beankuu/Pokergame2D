package objects;
import java.util.*;

public class Bet {
	private int bet;

	public Bet() // default constructor sets bet to 0
	{
		bet = 0;
	}

	public Bet(int n) // one-argument constructor, sets bet to n
	{
		bet = n;
	}

	public void setBet(int n) // setter
	{
		bet = n;
	}

	public int getBet() // getter
	{
		return bet;
	}
}
