package objects;

/**
 * 
 * 
 * Result에 표시할 내용 저장하는 객체 "단위"
 * 
 * @author yimsb
 */
public class Record {
	/*
	 * 뭐가있드라? 1. 베팅전 카드내역 2. 베팅후 카드내역 3. 베팅한 금액 4. 베팅 후 잔액
	 * 
	 * 일단 MainFrame이나 손보러 가야겠슴
	 * 
	 * 2016/11/01 23:50 회의끝난후 만지작거리다...
	 */
	private Card[] before_bet_cards;
	private boolean[] which_card_discarded;
	private Card[] after_bet_cards;
	private int bet_amount;
	private String evaluated_Result;
	private int left_cash;
	private int eval;

	public void set_Before_bet_cards(Card[] before_bet_cards) {
		this.before_bet_cards = before_bet_cards;
	}

	public void set_Which_card_discarded(boolean[] which_card_discarded) {
		this.which_card_discarded = which_card_discarded;
	}

	public void set_After_bet_cards(Card[] after_bet_cards) {
		this.after_bet_cards = after_bet_cards;
	}

	public void set_Bet_amount(int bet_amount) {
		this.bet_amount = bet_amount;
	}

	public void set_Evaluated_Result(String evaluated_Result) {
		this.evaluated_Result = evaluated_Result;
	}

	public void set_Left_cash(int left_cash) {
		this.left_cash = left_cash;
	}

	public void set_Eval(int eval) {
		this.eval = eval;
	}

	public Card[] get_Before_bet_cards() {
		return before_bet_cards;
	}

	public boolean[] get_Which_Card_Discarded() {
		return which_card_discarded;
	}

	public Card[] get_After_bet_cards() {
		return after_bet_cards;
	}

	public int get_Bet_amount() {
		return bet_amount;
	}

	public String get_Evaluated_Result() {
		return evaluated_Result;
	}

	public int get_Left_cash() {
		return left_cash;
	}

	public int get_Eval() {
		return eval;
	}
}
