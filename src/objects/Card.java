package objects;
///////////////////Card.java///////////////////

public class Card {
	private int suit; // 1= Hearts, 2= Diamonds, 3=Clubs, 4 = Spades
	private int value; // 1 = Ace占?11 = Jack, 12 = Queen, 13 = King

	public Card() // Ace of Hearts, by default
	{
		suit = 1;
		value = 1;
	}

	public Card(int s, int v) {
		suit = s;
		value = v;
	}

	public int getSuit() {
		return suit;
	}

	public int getValue() {
		return value;
	}

	public void setSuit(int s) {
		suit = s;
	}

	public void setValue(int v) {
		value = v;
	}

	public String getName() // returns string, e.g., "Ace of Hearts"
	{
		String name = "";
		if (value == 1)
			name = "Ace of ";
		else if (value == 11)
			name = "Jack of ";
		else if (value == 12)
			name = "Queen of ";
		else if (value == 13)
			name = "King of ";
		else // use the numerical value
			name = value + " of ";

		// Add on the suit onto the name

		if (suit == 1)
			name += "Hearts";
		else if (suit == 2)
			name += "Diamonds";
		else if (suit == 3)
			name += "Clubs";
		else
			name += "Spades";
		return name;
	}
}
