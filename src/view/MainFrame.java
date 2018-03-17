package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import animation.CardSizeChanger;
import animation.PanelMover;
import animation.RotateAndMoveImage;
import controller.PokerGame_GUI;
import objects.Bankroll;
import objects.Bet;
import objects.Card;
import objects.GUI_Card;
import objects.GUI_Coin;
import objects.GUI_Hand_Bottom;
import objects.GUI_Hand_Top;
import objects.Record;
/**
 * 메인 게임 프레임
 * 
 * @author yimsb
 */
public class MainFrame extends AnimatedFrame{
	//원 게임에서 가져온 파트
	private Bankroll bankroll;
	private PokerGame_GUI pokerGame;
	private Bet bet;
	
	private Card[] cardInfos;
	private Card[] prev_cardInfos;
	private boolean[] which_card_to_hold;
	private List<Record> list = new ArrayList<Record>();
	private Record record;
	
	private final int coin_limit = 10000;
	//좌표 모음들
	
	//카드가 펼쳐지는 진영의 중앙 좌표
	private final int CARD_DECK_CENTER_X = 50;
	private final int CARD_DECK_CENTER_Y = 200;
	//카드가 펼쳐지는 진영의 중앙 좌표
	private final int CARD_CENTER_X = 170;
	private final int CARD_CENTER_Y = 330;
	//프레임 패딩수치 (최소화/최대화/닫기버튼 있는 그 틀도 Screen에는 포함되나, getX()로 할 경우에는 포함되지 않음)
	private final int FRAME_PADDING_X = 9;
	private final int FRAME_PADDING_Y = 35;
	//코인 위치의 중앙값 [초기 생성시]
	private final int COIN_INIT_CENTER_X = 500;
	private final int COIN_INIT_CENTER_Y = 500;
	//코인 위치의 중앙값 [베팅 하였을때]
	private final int COIN_BET_CENTER_X = 460;
	private final int COIN_BET_CENTER_Y = 320;
	
	public static int count = 5;
	
	public static int count_2 =0;
	
	
	private int 
		//카드위치 관련
		//각 카드의 x, y위치 편차
		card_x_diff[] = { 70, 37, 0, -37, -70 },
		card_y_diff[] = { 33, 11, 0, 11, 33 },
		//베팅 후 각 카드 퍼트릴때의 x, y위치 편차
		card_bet_x_diff[] = { 100 + 100, 100, 0, -100, -100-100 },
		card_bet_y_diff[] = { 0, 0, 0, 0, 0 },
		//각 카드의 x, y 현재위치
		card_x_loc[] = new int[5],
		card_y_loc[] = new int[5],
		
		//코인위치 관련
		//각 코인의 x, y 위치 편차
		coin_x_diff[] = { 0, -3*1, -3*2, -3*3, -3*4 },
		coin_y_diff[] = { -3*4, -3*3, -3*2, -3*1, 0 },
		//각 코인의 x, y 현재위치
		coin_x_loc[] = new int[5],
		coin_y_loc[] = new int[5];
	//카드가 회천하는 최종 각도

	private final int card_rotation_degree[] = {
			30 + 30,
			15 + 30,
			0 + 30,
			-15 + 30,
			-30 + 30
	};
	
	//카드 객체 생성
	private GUI_Card base_deck = new GUI_Card(0, true);
	//게임에 사용할 객체들
	private GUI_Card[] cards = {
			new GUI_Card(card_rotation_degree[0], true),
			new GUI_Card(card_rotation_degree[1], true),
			new GUI_Card(card_rotation_degree[2], true),
			new GUI_Card(card_rotation_degree[3], true),
			new GUI_Card(card_rotation_degree[4], true)
	};
	
	//코인 객체 생성
	private GUI_Coin[] coins = {
			new GUI_Coin(),
			new GUI_Coin(),
			new GUI_Coin(),
			new GUI_Coin(),
			new GUI_Coin()
	};
	//손 모양 생성
	//이 객체안에서 손 앞면 뒷면 둘다 지정되어있다.
	private GUI_Hand_Top Hand_Top = new GUI_Hand_Top();
	private GUI_Hand_Bottom Hand_Bottom = new GUI_Hand_Bottom();
	
	
	////////////////////////////////
	////////   버튼 및 레이블들      ///////
	////////////////////////////////
	
	//계속 표시해야 할것들
	//베팅 테이블
	private JLabel betting_Table;
	private JLabel current_Cash;
		//결과 표시할곳
		
	private	JLabel result_Label;
	private	JLabel result_Payoff;
	private JButton BtnAddCoin;	//종모양
	private JButton BtnBet;	//Wow모양
	private JButton BtnExit;
	private JButton BtnRules;
	
	//각종 프레임들
	private MainFrame mf = this;
	private Add_Coins_Frame acf = new Add_Coins_Frame(mf);
	private RulesFrame rulesf = new RulesFrame();
	
	
	//게임 시작동안 다른버튼 숨겨야지
	boolean isGameStarted = false;
	boolean card_click_enabled = false;
	boolean is_Bet_Next_Enabled = false;
	boolean is_All_Card_Shown = false;
	//LoginFrame으로부터 이름(name)과 초기 자본(init_cash)를 받는다
	public MainFrame(String playerName, int init_cash){
		//기본 세팅
		bankroll = new Bankroll(init_cash);
		
		this.setTitle("한조) 카드가 진다..  played by " + playerName);
		this.setSize(800, 600);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setContentPane(new MainBackground());
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((dim.width/2)-(this.getWidth()/2), (dim.height/2)-(this.getHeight()/2));

		//손 세팅 - 1 (Top)
		Hand_Top.setSize(200, 184);
		Hand_Top.setVisible(false);
		Hand_Top.setLocation(this.getWidth(),this.getHeight()); //숨겨
		this.add(Hand_Top);
		//카드 세팅
		
		cards[0].setSuit(3);
		cards[1].setSuit(3);
		cards[1].setValue(3);

		for(GUI_Card card : cards){
			card.setSize(180, 180);
			card.setLocation(this.getWidth(),this.getHeight()); //숨겨
			card.setVisible(false);
			this.add(card);
			card.addMouseListener(new cardAdapter());
		}
		//카드 덱[Base] 세팅
		base_deck.setSize(180, 180);
		base_deck.setLocation(CARD_DECK_CENTER_X, CARD_DECK_CENTER_Y);
		base_deck.setVisible(false);
		this.add(base_deck);
		//손 세팅 - 2 (Bottom)
		Hand_Bottom.setSize(400, 450);
		Hand_Bottom.setVisible(false);
		Hand_Bottom.setLocation(this.getWidth(),this.getHeight()); //숨겨
		this.add(Hand_Bottom);
		//코인 세팅
		for(GUI_Coin coin : coins){
			coin.setSize(137, 45);
			coin.setVisible(true);
			this.add(coin);
			coin.addMouseListener(new coinAdapter());
			coin.addMouseMotionListener(new coinAdapter());
		}
		reset_All_Coins();

		//코인 수납 위치
		betting_Table = new JLabel(new ImageIcon(this.getClass().getClassLoader().getResource("images/Theme/Betting_Table.png")));
		betting_Table.setBounds(COIN_BET_CENTER_X-(int)(250*0.75), COIN_BET_CENTER_Y-(int)(120*0.75), 250, 120);
		betting_Table.setBackground(new Color(0,0,0,0));
		betting_Table.setVisible(true);
		this.add(betting_Table);
		
		//계속 디스플레이 할 레이블
		current_Cash = new JLabel("Current Cash = " + String.valueOf(bankroll.getBankroll()));
		current_Cash.setVisible(true);
		current_Cash.setForeground(Color.decode("#F16A43"));
		current_Cash.setFont(new Font("Jokerman",Font.BOLD, 26));
		current_Cash.setBounds(10,10,600,30);
		this.add(current_Cash);
		//매번 게임 끝날때마다 표시되는것들
		result_Label = new JLabel();
		result_Label.setVisible(false);
		result_Label.setForeground(Color.decode("#F16A43"));
		result_Label.setFont(new Font("Jokerman",Font.BOLD, 22));
		result_Label.setBounds(10,100,300,24);
		this.add(result_Label);
		
		result_Payoff = new JLabel();
		result_Payoff.setVisible(false);
		result_Payoff.setForeground(Color.decode("#F16A43"));
		result_Payoff.setFont(new Font("Jokerman",Font.BOLD, 22));
		result_Payoff.setBounds(10,150,200,24);
		this.add(result_Payoff);
		
		//////////////////////////////////////////////
		//				각종 버튼 세팅					//
		//////////////////////////////////////////////

		
		BtnBet = new JButton (new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/bet_button.gif")));
		BtnBet.setBounds(680, 450, 100, 100);
		BtnBet.setRolloverEnabled(false);
		BtnBet.setBackground(new Color(0,0,0,0));
		BtnBet.setVisible(false);
		BtnBet.setBorderPainted(false);
		BtnBet.setContentAreaFilled(false);
		BtnBet.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(!isGameStarted){
					isGameStarted = true;
					start_Game();
					BtnBet.setVisible(false);
				}else if(!is_All_Card_Shown){
					bet_And_Get();
					BtnBet.setVisible(false);
				}else if(is_All_Card_Shown){
					is_All_Card_Shown = false;
					is_Bet_Next_Enabled = true;
					count_2+=1;
					start_Next_Thread(9999);
				}
			}
		});
		add(BtnBet);	
		
		
		BtnAddCoin = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/bell1.png")));
		BtnAddCoin.setBounds(655, 300, 119 + 5, 99 + 5);
		BtnAddCoin.setBackground(new Color(0,0,0,0));
		BtnAddCoin.setBorderPainted(false);
		BtnAddCoin.setContentAreaFilled(false);
		BtnAddCoin.setDefaultCapable(false);
		BtnAddCoin.setFocusPainted(false);
		BtnAddCoin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				acf.set_Default_Coin(bankroll.getBankroll());
				acf.reset_Coin_Slider();
				acf.setVisible(true);
			}
		});
		BtnAddCoin.setRolloverIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/bell1_1.png")));
		BtnAddCoin.setPressedIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/bell1_2.png")));	
		add(BtnAddCoin);		
		
		
		
		BtnExit = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/btn1.png")));		
		BtnExit.setBounds(620, 10, 153 + 5, 50 + 5);
		BtnExit.setBackground(new Color(0,0,0,0));
		BtnExit.setBorderPainted(false);
		BtnExit.setContentAreaFilled(false);
		BtnExit.setDefaultCapable(false);
		BtnExit.setFocusPainted(false);
		BtnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new ResultFrame(list);
				rulesf.dispose();
				acf.dispose();
				mf.dispose();
			}
		});
		BtnExit.setRolloverIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/btn1_1.png")));
		BtnExit.setPressedIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/btn1_2.png")));		
		add(BtnExit);
		
		
		BtnRules = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/btn2.png")));
		BtnRules.setBounds(733, 65, 35 +10 , 57 + 5);
		BtnRules.setBackground(new Color(0,0,0,0));
		BtnRules.setBorderPainted(false);
		BtnRules.setContentAreaFilled(false);
		BtnRules.setDefaultCapable(false);
		BtnRules.setFocusPainted(false);
		BtnRules.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				rulesf.setVisible(true);
			}
		});
		BtnRules.setRolloverIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/btn2_1.png")));
		BtnRules.setPressedIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/btn2_2.png")));
		add(BtnRules);		
	}	
	/* 코인 움직임 관련 메소드들
	 * - 크게 종류로는 2가지인데
	 * - 최초 위치로 보내는것 >> xxx_Init 계열
	 * - 최종 위치로 보내는 것 >> xxx_Betting 계열
	 * 
	 * 기본적으로 메소드들에서는 움직일 "최종 위치"를 coin_x_loc[key] / coin_y_loc[key]에 저장하게 된다.
	 */
	/**
	 * 코인 움직이는 메소드</br>
	 * 
	 * 최초 위치로 지정된 좌표 + 편차를 coin_x_loc[key]와 coin_y_loc[key]에 저장한 후 </br> 
	 * 해당 카드의 위치를  setLocation을 통하여 위치를 지정한다.</br>
	 * 
	 * 비슷한 메소드로는 {@link MainFrame move_Coins_To_Betting}가 있다.</br>
	 * 
	 * 
	 * @author yimsb
	 * 
	 * @param key key번째 코인을 최초 위치로 움직인다, 각 key마다 기본 x,y편차가 정의되어있다</br>
	 * {@link MainFrame coin_x_loc}, {@link MainFrame coin_y_loc},
	 * {@link MainFrame COIN_INIT_CENTER_X}, {@link MainFrame COIN_INIT_CENTER_Y},
	 * {@link MainFrame coin_x_diff}, {@link MainFrame coin_y_diff}
	 */
	public void move_Coins_To_Init(int key){
		coin_x_loc[key] = COIN_INIT_CENTER_X + coin_x_diff[key];
		coin_y_loc[key] = COIN_INIT_CENTER_Y + coin_y_diff[key];
		coins[key].setLocation(coin_x_loc[key], coin_y_loc[key]);
		coins[key].set_As_Not_Bet();
	}
	/**
	 * 코인 움직이는 메소드</br>
	 * 
	 * @author yimsb
	 * {@link MainFrame move_Coins_To_Init}
	 */
	public void move_Coins_ALL_To_Init(){
		for(int i = 0; i < coins.length; ++i){
			move_Coins_To_Init(i);
			coins[i].setVisible(true);
		}
	}
	/**
	 * 코인 움직이는 메소드</br>
	 * 
	 * @author yimsb
	 * @param key key번째 코인을 x, y로 움직인다
	 */
	public void move_Coins_To_Betting(){
		for(int i = coins.length - 1; i >= 0; --i){
			if(!coins[i].get_Bet_State()){
				coin_x_loc[i] = COIN_BET_CENTER_X + coin_x_diff[i] - betting_Table.getWidth()/2;
				coin_y_loc[i] = COIN_BET_CENTER_Y + coin_y_diff[i] - betting_Table.getHeight()/2;
				coins[i].setLocation(coin_x_loc[i], coin_y_loc[i]);
				coins[i].set_As_Bet();
				return;
			}
		}
	}
	public int get_How_Many_Bet_Coins(){
		int coin_count = 0;
		for(GUI_Coin coin : coins)
			if(coin.get_Bet_State())
				coin_count++;
		return coin_count;
	}
	public void reset_All_Coins(){
		move_Coins_ALL_To_Init();
		for(GUI_Coin coin: coins)
			coin.setVisible(true);
		card_click_enabled = false;
	}	
	/* 카드 움직임 관련 메소드들
	 * 카드 펼처지는것
	 */
	public void set_Card_Degree(int key, int degree){
		cards[key].set_Degree(degree);
	}
	public void set_Card_Location(int key, int x, int y){
		card_x_loc[key] = x;
		card_y_loc[key] = y;
		cards[key].setLocation(card_x_loc[key], card_y_loc[key]);
	}
	
	public void set_All_Card_Same_Location(int x, int y){
		for(int i = 0; i < cards.length; ++i){
			set_Card_Location(i, x, y);
		}
	}
	public void flip_Card(int key){
		cards[key].flip_Card();
	}
	
	//Thread_Ing 관련
	//Thread에서 호출하는 Method들
	public void move_cards(double tick){
		for(int i = 0; i < cards.length; ++i)
			set_Card_Location(i, CARD_CENTER_X + (int)(card_x_diff[i]*tick), CARD_CENTER_Y + (int)(card_y_diff[i]*tick));
	}
	public void spread_out_cards(double tick){
		for(int i = 0; i < cards.length; ++i)
			set_Card_Location(i, CARD_CENTER_X + (int)(card_bet_x_diff[i]*tick), CARD_CENTER_Y + (int)(card_bet_y_diff[i]*tick));
	}
	
	
	//Console측과 연동하기 위해서 만든거
	public String getDisplayResults(int payoff){
		if (payoff == 250)
			return "Royal Flush";
		else if (payoff == 50)
			return "Straight Flush";
		else if (payoff == 25)
			return "Four of a Kind";
		else if (payoff == 9)
			return "Full House";
		else if (payoff == 6)
			return " Flush";
		else if (payoff == 4)
			return "Straight ";
		else if (payoff == 3)
			return "Three of a Kind";
		else if (payoff == 2)
			return "Two Pair";
		else if (payoff == 1)
			return " Pair of Jacks or Better";
		else
			return "Lose";
	}
	public void addCoin(int coin){
		if(bankroll.getBankroll() + coin < coin_limit)
			bankroll.alterBankroll(coin);
		current_Cash.setText("Current Cash = " + String.valueOf(bankroll.getBankroll()));
	}
	
	
	//게임 시작시 불르면 초기 GUI 세팅	
	//5개의 스레드 동시시작 후 해당 번호 아니면 
	//Boolean값에 따라서 다음 쓰레드 실행
	
	private Thread[] movingSteps;
	public void start_Game(){
		///////////////////////////////
		// 기존 게임으로부터 정보를 미리 받아옴 ///
		//////////////////////////////
		bet = new Bet(get_How_Many_Bet_Coins());
	    pokerGame = new PokerGame_GUI(bet, bankroll);
	    cardInfos = pokerGame.viewInitialHand();
	    prev_cardInfos = cardInfos.clone();
	    for(int i = 0;i < cards.length; ++i){
	    	cards[i].setValue(cardInfos[i].getValue());
	    	cards[i].setSuit(cardInfos[i].getSuit());
	    }
	    ////////////////////////////////
	    //세팅
	    record = new Record();
	    record.set_Before_bet_cards(cardInfos);
	    record.set_Bet_amount(get_How_Many_Bet_Coins());
		////////////////////////////////
		//////////   GUI 파트      /////////
		////////////////////////////////    
	    
		for(int i = 0; i < cards.length; ++i){
			cards[i].set_Is_Empty_Card(true);
			cards[i].setVisible(true);
			cards[i].set_current_degree_to_zero();
		}
		set_All_Card_Same_Location(CARD_DECK_CENTER_X, CARD_DECK_CENTER_Y);
		Hand_Top.setVisible(false);
		Hand_Bottom.setVisible(false);
		//움직임 관련 스레드들
		Thread[] create_Moving_Steps = {
			//PanelMover(스레드 번호(시작 순서 조정용), 조정할 패널, 어떠한 객체들, 몇초동안 움직임, 어디부터(x,y), 어디까지(x,y), cos의 몇제곱)
			//[0 ~ 4] STEP1. 카드 덱에서 카드를 Player에게 쏜다!
			new Thread(new PanelMover(0, mf, cards[0], 1500, 100, CARD_DECK_CENTER_X, CARD_DECK_CENTER_Y, CARD_DECK_CENTER_X, this.getHeight() - 200, 10)),
			new Thread(new PanelMover(1, mf, cards[1], 1500, 300, CARD_DECK_CENTER_X, CARD_DECK_CENTER_Y, CARD_DECK_CENTER_X, this.getHeight() - 200, 10)),
			new Thread(new PanelMover(2, mf, cards[2], 1500, 600, CARD_DECK_CENTER_X, CARD_DECK_CENTER_Y, CARD_DECK_CENTER_X, this.getHeight() - 200, 10)),
			new Thread(new PanelMover(3, mf, cards[3], 1500, 900, CARD_DECK_CENTER_X, CARD_DECK_CENTER_Y, CARD_DECK_CENTER_X, this.getHeight() - 200, 10)),
			new Thread(new PanelMover(4, mf, cards[4], 1500, 1200, CARD_DECK_CENTER_X, CARD_DECK_CENTER_Y, CARD_DECK_CENTER_X, this.getHeight() - 200, 10)),
			
			//[5] STEP2. 카드 다 쐈으면 기존 덱 날려버림(크기 작게)
			new Thread(new CardSizeChanger(5, mf, base_deck, 500, 10, 8, false)),
			//[6] STEP3. 쏴서 화면 하단에 모여있는 카드들 위로 손(뒷면)을 올린다
			new Thread(new PanelMover(6, mf, Hand_Top, 500, 10, CARD_DECK_CENTER_X, this.getHeight(), CARD_DECK_CENTER_X, this.getHeight() - 150, 8)),
			
			//[7] STEP4-1. 카드들이 화면밖(아래)로 없어진다
			new Thread(new PanelMover(7, mf, cards, 2000, 10, CARD_DECK_CENTER_X, this.getHeight() - 100, CARD_DECK_CENTER_X , this.getHeight(), 8)),
			//[8] STEP4-2. 손(뒷면)도 같이 없어진다
			new Thread(new PanelMover(8, mf, Hand_Top, 2000, 10, CARD_CENTER_X, this.getHeight() - 150, CARD_DECK_CENTER_X , this.getHeight(), 8)),
			
			//[9] STEP5-1. 손(앞면)이 카드와 함께 불쑥 튀어나온다	[카드]
			new Thread(new PanelMover(9, mf, cards, 1000, 10, CARD_CENTER_X, this.getHeight(), CARD_CENTER_X, CARD_CENTER_Y, 1)),
			//[10] STEP5-2. 손(앞면)이 카드와 함께 불쑥 튀어나온다	[손]
			new Thread(new PanelMover(10, mf, Hand_Bottom, 1500, 10, CARD_CENTER_X, this.getHeight(), CARD_CENTER_X - 100, CARD_CENTER_Y, 4)),
			
			//[11] STEP6. 카드가 좌르륵 펼쳐진다
			new Thread(new RotateAndMoveImage(11, cards, mf, 1000, 10, 8, true))
		}; 
		movingSteps = create_Moving_Steps;
		movingSteps[0].start();
		movingSteps[1].start();
		movingSteps[2].start();
		movingSteps[3].start();
		movingSteps[4].start();
	}
	
	//베팅 버튼을 누르고 시작
	
	
	public void bet_And_Get(){
		///////////////////////////////
		// 기존 게임으로부터 정보를 미리 받아옴 ///
		//////////////////////////////
		which_card_to_hold = new boolean[cards.length];
		for(int i = 0;i < cards.length; ++i){
			if(cards[i].isEmptyCard())
				which_card_to_hold[i] = false;
			else
				which_card_to_hold[i] = true;
		}
		cardInfos = pokerGame.discardOrHoldCards(which_card_to_hold);
		bankroll = pokerGame.getBankRoll();
		///////////////////////////////////
		//세팅
		
		record.set_After_bet_cards(cardInfos);
		record.set_Evaluated_Result(getDisplayResults(pokerGame.evaluateHand()));
		record.set_Eval(pokerGame.evaluateHand());
		record.set_Left_cash(bankroll.getBankroll());
		list.add(record);
		////////////////////////////////
		//////////   GUI 파트      /////////
		////////////////////////////////
		
		//움직임 관련 스레드들
		is_All_Card_Shown = false;
		is_Bet_Next_Enabled = false;
		card_click_enabled = false;
		Thread[] create_Moving_Steps = {
			//CardSizeChanger(스레드 번호(시작 순서 조정용), 조정할 카드 객체, 몇초동안 줄어들음, 초기 딜레이, 참 = 보임 / 거짓 = 안보임(줄어들음))
			//[0 /12] STEP1. 카드가 사라진다
			new Thread(new CardSizeChanger(12, mf, cards, 500, 10, 8, false)),
			
			/////////////////////////
			//     [0] ~ [1] 사이      //
			// 		카드가 교체된다.	   //
			/////////////////////////
			
			//[1 /13] STEP2-1. 남은 카드가 펼쳐진다. ** 모든 카드의 위치를 움직이는것일 뿐 [False 플래그]
			new Thread(new RotateAndMoveImage(13, cards, mf, 1000, 10, 8, false)),
			//[2 /14] STEP2-2. 손은 아래로 빠진다
			new Thread(new PanelMover(14, mf, Hand_Bottom, 500, 10, CARD_CENTER_X - 100, CARD_CENTER_Y, CARD_CENTER_X, this.getHeight(), 1)),
			//[3 /15] STEP3. 사라진 카드가 교체된 카드로 다시 나타난다
			new Thread(new CardSizeChanger(15, mf, cards, 500, 10, 8, true)),
			/////////////////////////
			// START 버튼을 다시 기다린다 //
			/////////////////////////
			//[4 /16] STEP4. 카드가 일단 다 사라진다
			new Thread(new CardSizeChanger(16, mf, cards, 500, 10, 8, false)),
			//[5 /17] STEP5. 카드를 버린다 	//혹시모를 상황에 대비하여 화면밖으로 던져버림
			new Thread(new PanelMover(17, mf, cards, 500, 10, CARD_CENTER_X, CARD_CENTER_Y, this.getWidth(), this.getHeight(), 1)),
			//[6 /18] STEP6-1. 기본 카드 덱 다시 꺼냄
			new Thread(new CardSizeChanger(18, mf, base_deck, 500, 10, 8, true)),
			//[6 /19] STEP6-2 카드 크기 원상복구
			new Thread(new CardSizeChanger(19, mf, cards, 500, 10, 1, true)),
		}; 
		movingSteps = create_Moving_Steps;
		movingSteps[0].start();
	}
	
	public void start_Next_Thread(int current_Thread_Index){
		/// START GAME 관련
		if(current_Thread_Index == 4){
			movingSteps[5].start();
		}else if(current_Thread_Index == 5){
			Hand_Top.setVisible(true);
			movingSteps[6].start();
		}else if(current_Thread_Index == 6){
			movingSteps[7].start();
			movingSteps[8].start();
		}else if(current_Thread_Index == 8){
			for(int i = 0; i < cards.length; ++i)
				flip_Card(i);
			Hand_Top.setVisible(false);
			Hand_Bottom.setVisible(true);
			movingSteps[9].start();
			movingSteps[10].start();
		}else if(current_Thread_Index == 9 ){
			movingSteps[11].start();
		}else if(current_Thread_Index == 11){
			BtnBet.setVisible(true);
			card_click_enabled = true;
		}
		////////////////////////////////
		/// Bet And Play 관련
		else if(current_Thread_Index == 12){
			card_click_enabled = false;
			////////////////////////////////
			////		카드 교체			////
			////////////////////////////////
			
			///cardInfos에서 변경된 정보의 위치 추적
			boolean[] was_in_hand ={true, true, true, true, true};
			boolean found = false;
			
			for(int i = 0; i < cardInfos.length; ++i){
				for(int k = 0; k < prev_cardInfos.length; ++k){
					found = false;
					if(cardInfos[i].getValue() == prev_cardInfos[k].getValue()
							&& cardInfos[i].getSuit() == prev_cardInfos[k].getSuit()){
						found = true;
						break;
					}
				}
				if(!found)
					was_in_hand[i] = false;
			}
			
			//위 정보를 바탕으로 실제 보이는 카드 교체
			int searched_till = 0;
			for(int i = 0;i < cards.length; ++i){
				if(which_card_to_hold[i]){
					cards[i].setValue(prev_cardInfos[i].getValue());
		    		cards[i].setSuit(prev_cardInfos[i].getSuit());
				}else{
					for(int k = searched_till; k < cards.length; ++k){
						if(!was_in_hand[k]){
							cards[i].setValue(cardInfos[k].getValue());
							cards[i].setSuit(cardInfos[k].getSuit());
							searched_till = k + 1;
							break;
						}
					}
				}
		    }
			/////////////////////////////////
			movingSteps[1].start();
			movingSteps[2].start();
		}else if(current_Thread_Index == 14){
			movingSteps[3].start();
		}else if(current_Thread_Index == 15){
			for(GUI_Card card : cards)
				card.set_Is_Empty_Card(false);
			is_All_Card_Shown = true;
			result_Label.setVisible(true);
			result_Payoff.setVisible(true);
			BtnBet.setVisible(true);
			result_Label.setText(getDisplayResults(pokerGame.evaluateHand()));
			result_Payoff.setText("Payoff = " + String.valueOf(pokerGame.evaluateHand()));
			current_Cash.setText("Current Cash = " + String.valueOf(bankroll.getBankroll()) + "(" + (Integer.signum(pokerGame.evaluateHand()) > 0 ? "+" : "") + String.valueOf(get_How_Many_Bet_Coins()*pokerGame.evaluateHand())+ ")");
		}else if(is_Bet_Next_Enabled){
			is_All_Card_Shown = false;
			is_Bet_Next_Enabled = false;
			reset_All_Coins();
			BtnBet.setVisible(false);
			for(GUI_Card card : cards)
				card.set_Is_Empty_Card(true);
			movingSteps[4].start();
		}else if(current_Thread_Index == 16){
			movingSteps[5].start();
			movingSteps[6].start();
		}else if(current_Thread_Index == 18){
			movingSteps[7].start();
		}else if(current_Thread_Index == 19){
			isGameStarted = false;
			result_Label.setVisible(false);
			result_Payoff.setVisible(false);
			if(bankroll.getBankroll() < 5){
				new ResultFrame(list);
				mf.dispose();
			}
		}
	}
	
	//카드 클릭 Action에만 해당됩니다!
	class cardAdapter extends MouseAdapter{
		public void mousePressed(MouseEvent e) {
			if(card_click_enabled){
				int relative_location_X = e.getXOnScreen() - (int)mf.getLocation().getX() - FRAME_PADDING_X;
				int relative_location_Y = e.getYOnScreen() - (int)mf.getLocation().getY() - FRAME_PADDING_Y;
				if(e.getSource() == cards[0]){
					if(cards[0].isTransparentXY(relative_location_X - (card_x_loc[0] - (card_x_loc[0] - card_x_loc[0])), relative_location_Y - (card_y_loc[0] - (card_y_loc[0] - card_y_loc[0]))))
						flip_Card(0);
					else if(cards[1].isTransparentXY(relative_location_X - (card_x_loc[0] - (card_x_loc[0] - card_x_loc[1])), relative_location_Y - (card_y_loc[0] - (card_y_loc[0] - card_y_loc[1]))))
						flip_Card(1);
					else if(cards[2].isTransparentXY(relative_location_X - (card_x_loc[0] - (card_x_loc[0] - card_x_loc[2])), relative_location_Y - (card_y_loc[0] - (card_y_loc[0] - card_y_loc[2]))))
						flip_Card(2);
					else if(cards[3].isTransparentXY(relative_location_X - (card_x_loc[0] - (card_x_loc[0] - card_x_loc[3])), relative_location_Y - (card_y_loc[0] - (card_y_loc[0] - card_y_loc[3]))))
						flip_Card(3);
					else if(cards[4].isTransparentXY(relative_location_X - (card_x_loc[0] - (card_x_loc[0] - card_x_loc[4])), relative_location_Y - (card_y_loc[0] - (card_y_loc[0] - card_y_loc[4]))))
						flip_Card(4);
				}
				else if(e.getSource() == cards[1]){
					if(cards[1].isTransparentXY(relative_location_X - (card_x_loc[1] - (card_x_loc[1] - card_x_loc[1])), relative_location_Y - (card_y_loc[1] - (card_y_loc[1] - card_y_loc[1]))))
						flip_Card(1);
					else if(cards[2].isTransparentXY(relative_location_X - (card_x_loc[1] - (card_x_loc[1] - card_x_loc[2])), relative_location_Y - (card_y_loc[1] - (card_y_loc[1] - card_y_loc[2]))))
						flip_Card(2);
					else if(cards[3].isTransparentXY(relative_location_X - (card_x_loc[1] - (card_x_loc[1] - card_x_loc[3])), relative_location_Y - (card_y_loc[1] - (card_y_loc[1] - card_y_loc[3]))))
						flip_Card(3);
					else if(cards[4].isTransparentXY(relative_location_X - (card_x_loc[1] - (card_x_loc[1] - card_x_loc[4])), relative_location_Y - (card_y_loc[1] - (card_y_loc[1] - card_y_loc[4]))))
						flip_Card(4);
				}
				else if(e.getSource() == cards[2]){
					if(cards[2].isTransparentXY(relative_location_X - (card_x_loc[2] - (card_x_loc[2] - card_x_loc[2])), relative_location_Y - (card_y_loc[2] - (card_y_loc[2] - card_y_loc[2]))))
						flip_Card(2);
					else if(cards[3].isTransparentXY(relative_location_X - (card_x_loc[2] - (card_x_loc[2] - card_x_loc[3])), relative_location_Y - (card_y_loc[2] - (card_y_loc[2] - card_y_loc[3]))))
						flip_Card(3);
					else if(cards[4].isTransparentXY(relative_location_X - (card_x_loc[2] - (card_x_loc[2] - card_x_loc[4])), relative_location_Y - (card_y_loc[2] - (card_y_loc[2] - card_y_loc[4]))))
						flip_Card(4);
				}
				else if(e.getSource() == cards[3]){
					if(cards[3].isTransparentXY(relative_location_X - (card_x_loc[3] - (card_x_loc[3] - card_x_loc[3])), relative_location_Y - (card_y_loc[3] - (card_y_loc[3] - card_y_loc[3]))))
						flip_Card(3);
					else if(cards[4].isTransparentXY(relative_location_X - (card_x_loc[3] - (card_x_loc[3] - card_x_loc[4])), relative_location_Y - (card_y_loc[3] - (card_y_loc[3] - card_y_loc[4])) ))
						flip_Card(4);
		
				}
				else if(e.getSource() == cards[4]){
					if(cards[4].isTransparentXY(relative_location_X - (card_x_loc[4] - (card_x_loc[4] - card_x_loc[4])) - FRAME_PADDING_X, relative_location_Y - (card_y_loc[4] - (card_y_loc[4] - card_y_loc[4])) - FRAME_PADDING_Y))
						flip_Card(4);
				}
			}
			mf.repaint();
			
		}
	}
	//코인 관련 움직임에만 해당합니다!
	class coinAdapter implements MouseListener, MouseMotionListener{
		public void mouseDragged(MouseEvent e) {
			if(!isGameStarted){
				int relative_location_X = e.getXOnScreen() - (int)mf.getLocation().getX() - FRAME_PADDING_X;
				int relative_location_Y = e.getYOnScreen() - (int)mf.getLocation().getY() - FRAME_PADDING_Y;
				//반응한 리스너의 소스가 GUI_Coin 객체였다면
				if(e.getSource() instanceof GUI_Coin)
					((GUI_Coin)e.getSource()).setLocation(relative_location_X - 68, relative_location_Y - 22);
				mf.repaint();
			}
		}
		public void mouseMoved(MouseEvent e) { }
		public void mouseClicked(MouseEvent e) { }
		public void mouseEntered(MouseEvent e) { }
		public void mouseExited(MouseEvent e) { }
		public void mousePressed(MouseEvent e) { }
		public void mouseReleased(MouseEvent e) {
			if(!isGameStarted){
				int relative_location_X = e.getXOnScreen() - (int)mf.getLocation().getX() - FRAME_PADDING_X;
				int relative_location_Y = e.getYOnScreen() - (int)mf.getLocation().getY() - FRAME_PADDING_Y;
				//각각 좌우 150픽셀, 상하 22 픽셀안에 들어오면 베팅영역 안으로 간주한다.
				if(relative_location_X  > COIN_BET_CENTER_X - 180 && relative_location_X < COIN_BET_CENTER_X + 80
						&& relative_location_Y > COIN_BET_CENTER_Y - 90 && relative_location_Y < COIN_BET_CENTER_Y + 20){
					if(e.getSource() == coins[0]){
						move_Coins_To_Betting();
						if(get_How_Many_Bet_Coins() < 5)
							move_Coins_To_Init(0);
					}
					else if(e.getSource() == coins[1]){
						move_Coins_To_Betting();
						move_Coins_To_Init(1);
					}
					else if(e.getSource() == coins[2]){
						move_Coins_To_Betting();
						move_Coins_To_Init(2);
					}
					else if(e.getSource() == coins[3]){
						move_Coins_To_Betting();
						move_Coins_To_Init(3);
					}
					else if(e.getSource() == coins[4]){
						move_Coins_To_Betting();
						move_Coins_To_Init(4);
					}
				}else{
					if(e.getSource() == coins[4])
						move_Coins_To_Init(4);
					else if(e.getSource() == coins[3])
						move_Coins_To_Init(3);
					else if(e.getSource() == coins[2])
						move_Coins_To_Init(2);
					else if(e.getSource() == coins[1])
						move_Coins_To_Init(1);
					else if(e.getSource() == coins[0])
						move_Coins_To_Init(0);					
				}
				//코인이 하나라도 들어가면
				boolean isAnyCoinBet = false;
				for(GUI_Coin coin : coins)
					isAnyCoinBet |= coin.get_Bet_State();
				BtnBet.setVisible(isAnyCoinBet);
				mf.repaint();
			}
		}		
	}
	//배경 그리기
	class MainBackground extends JPanel{
		Image img = (new ImageIcon(this.getClass().getClassLoader().getResource("images/Theme/main4.png"))).getImage();
		MainBackground(){
			this.setLayout(null);
		}
		public void paintComponent(Graphics g){
			super.paintComponents(g);
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
			mf.repaint();
		}
	}
}
