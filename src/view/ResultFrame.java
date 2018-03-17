package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import objects.GUI_Card;
import objects.Record;

public class ResultFrame extends JFrame {
	// DATA 관련
	private List<Record> list;

	// 상단에 GAME END라고 장식할 표지
	private JLabel GAME_END;
	private JButton ReRun;

	// 게임 결과 리스트 출력 관련
	private JButton Btn_Up;
	private JButton Btn_Down;

	private static int list_index_top;
	private static int list_index_bottom;
	private static int list_index_inList;
	private static int list_index_current;

	private JLabel[] list_viewer = new JLabel[6];
	private JLabel[] before_bet = new JLabel[6];
	private JLabel[] after_bet = new JLabel[6];

	private JLabel[] title = { new JLabel("게임회차"), new JLabel("베팅 금액"), new JLabel("획득 금액"), };

	// 우측 게임 결과별 출력관련
	private GUI_Card[] before_cards = { // new GUI_Card[5];
			new GUI_Card(0, false), 
			new GUI_Card(0, false), 
			new GUI_Card(0, false), 
			new GUI_Card(0, false),
			new GUI_Card(0, false)
	};
	private GUI_Card[] after_cards = { // new GUI_Card[5];
			new GUI_Card(0, false), 
			new GUI_Card(0, false), 
			new GUI_Card(0, false), 
			new GUI_Card(0, false),
			new GUI_Card(0, false) 
	};
	//색상 팔레트
	private Color color_Background = Color.decode("#C8C8A9");
	
	
	
	// This Frame
	private ResultFrame resf = this;

	/// 일단 아래꺼 먼저 작성하고 이걸로 교체
	public ResultFrame(List<Record> list) {
		this.list = list;
		this.setTitle("결과창");
		this.setSize(800, 600);
		this.setLayout(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((dim.width / 2) - (this.getWidth() / 2), (dim.height / 2) - (this.getHeight() / 2));
		this.setContentPane(new ResultPanelBackground());
		///////////// 객체 생성 ///////////////
		GAME_END = new JLabel("GAME END");
		ReRun = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/ReRun.gif")));
		ReRun.setRolloverIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/ReRun_Ask.png")));
		Btn_Up = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/Btn_Up.png")));
		Btn_Down = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/Btn_Down.png")));
		///////////// Boundary Settings1 ////////////
		GAME_END.setBounds(250, 5, 200, 50);
		ReRun.setBounds(510, 5, 267, 150);
		Btn_Up.setBounds(210, 280 - 30, 40, 40);
		Btn_Down.setBounds(210, 280 + 30, 40, 40);

		///////////////////////////////////// 라벨 제목 삽입
		for (int i = 0; i < title.length; i++) {
			title[i].setBounds(10 + 70 * i, 50, 60, 50);
		}

		//////////////////////// list 회차
		list_index_current =list_index_inList = list_index_top = 0;
		list_index_bottom = (list.size() > 6) ? 6 : list.size();
		for (int i = list_index_top; i < list_index_bottom; i++) {
			list_viewer[i] = new JLabel(String.valueOf(i+1));
			before_bet[i] = new JLabel(String.valueOf(list.get(i).get_Bet_amount()));
			after_bet[i] = new JLabel(
					String.valueOf(list.get(i).get_Left_cash())
					+"(" 
					+ (Integer.signum(list.get(i).get_Eval()) > 0 ? "+" : "-") //0은 없다
					+ String.valueOf(list.get(i).get_Bet_amount())
					+ ")"
			);
			
			list_viewer[i].setFont(new Font("굴림", Font.BOLD, 14));
			before_bet[i].setFont(new Font("굴림", Font.BOLD, 14));
			after_bet[i].setFont(new Font("굴림", Font.BOLD, 14));
			
			list_viewer[i].setBounds(40, 110 + 70 * i, 30, 50);
			before_bet[i].setBounds(100, 110 + 70 * i, 30, 50);
			after_bet[i].setBounds(150, 110 + 70 * i, 130, 50);
			
			if(list.get(i).get_Eval() > 0){
				after_bet[i].setForeground(Color.BLUE);
				before_bet[i].setForeground(Color.BLUE);
			}else{
				after_bet[i].setForeground(Color.RED);
				before_bet[i].setForeground(Color.RED);
			}
			list_viewer[i].setBackground(color_Background);
			list_viewer[i].setOpaque(true);
		}
		if(list_index_bottom > 0)
			list_viewer[list_index_inList].setBackground(Color.CYAN);
		////////////////////// 배팅 전 금액

		////////////// FONT & Background /////////////
		GAME_END.setFont(new Font("Jokerman", Font.BOLD, 30));
		GAME_END.setForeground(Color.decode("#F05053"));

		Btn_Up.setBackground(color_Background);
		Btn_Down.setBackground(color_Background);		
		
		////////////// add to panel + Background(minimalize-Loop-usage)
		////////////// /////////
		this.add(GAME_END);
		this.add(ReRun);
		this.add(Btn_Up);
		this.add(Btn_Down);
		
		
		for (JLabel name : title)
			this.add(name);
		
		for(int i = 0; i < list_index_bottom; ++i){
			this.add(before_bet[i]);
			this.add(after_bet[i]);
			this.add(list_viewer[i]);
		}

		for (GUI_Card card : before_cards) {
			card.setBackground(color_Background);
			card.setSize(150, 150);
			card.setVisible(true);
			if(list_index_bottom == 0)
				card.set_Is_Empty_Card(true);//아무것도 없을�� 뒤집어놓기
			this.add(card);
		}
		
		for (GUI_Card card : after_cards) {
			card.setBackground(color_Background);
			card.setSize(150, 150);
			card.setVisible(true);
			if(list_index_bottom == 0)
				card.set_Is_Empty_Card(true);//아무것도 없을��뒤집어놓기
			this.add(card);
		}

		///////////// Boundary Settings2 (CARDS) ////////////
		for(int i = 0; i < before_cards.length; ++i){
			before_cards[i].setLocation(220 + i*105, 150);
			after_cards[i].setLocation(220+ i*105, 300);
			if(list_index_bottom > 0){
				before_cards[i].setSuit(list.get(list_index_current).get_Before_bet_cards()[i].getSuit());
				before_cards[i].setValue(list.get(list_index_current).get_Before_bet_cards()[i].getValue());
				after_cards[i].setSuit(list.get(list_index_current).get_After_bet_cards()[i].getSuit());
				after_cards[i].setValue(list.get(list_index_current).get_After_bet_cards()[i].getValue());
			}
		}
		
		
		//////////////////////////////////////////
		// Actions
		////////////////////////////////////////
		
		////////// ActionListeners ///////////////
		
		ReRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new LoginFrame();
				resf.dispose();
			}
		});
		Btn_Up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnUpAction();
			}
		});
		Btn_Up.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					btnUpAction();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					btnDownAction();
				}
			}
		});
		Btn_Down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDownAction();
			}
		});
		Btn_Down.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					btnUpAction();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					btnDownAction();
				}
			}
		});
		this.requestFocus();
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					btnUpAction();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					btnDownAction();
				}
			}
		});
	}
	public void btnUpAction(){
		if (list_index_current - 1 < 0)
			return;
		int before = list_index_inList;
		if(list_index_inList != 0)
			list_index_inList--;
		if( --list_index_current <= list_index_top){
			if(before == 0){
				list_index_top--;
				list_index_bottom--;
			}
		}
		
		for (int i = list_index_top; i < list_index_bottom; i++) {
			list_viewer[i - list_index_top].setText(String.valueOf(i+1));
			before_bet[i - list_index_top].setText(String.valueOf(list.get(i).get_Bet_amount()));
			after_bet[i - list_index_top].setText(
					String.valueOf(list.get(i).get_Left_cash())
					+"(" 
					+ (Integer.signum(list.get(i).get_Eval()) > 0 ? "+" : "-") //0은 없다 
					+ String.valueOf(list.get(i).get_Bet_amount())
					+ ")"
			);
			if(list.get(i).get_Eval() > 0){
				after_bet[i - list_index_top].setForeground(Color.BLUE);
				before_bet[i - list_index_top].setForeground(Color.BLUE);
			}else{
				after_bet[i - list_index_top].setForeground(Color.RED);
				before_bet[i - list_index_top].setForeground(Color.RED);
			}
		}
		////////////
		if(list_index_inList + 1 <= list.size() - 1)
			list_viewer[list_index_inList + 1].setBackground(color_Background);
		list_viewer[list_index_inList].setBackground(Color.CYAN);
		
		for(int i=0; i<before_cards.length; i++){
			before_cards[i].setSuit(list.get(list_index_current).get_Before_bet_cards()[i].getSuit());
			before_cards[i].setValue(list.get(list_index_current).get_Before_bet_cards()[i].getValue());
			after_cards[i].setSuit(list.get(list_index_current).get_After_bet_cards()[i].getSuit());
			after_cards[i].setValue(list.get(list_index_current).get_After_bet_cards()[i].getValue());
		}
		resf.repaint();
	}
	public void btnDownAction(){
		if (list_index_current + 1 > list.size() - 1)
			return;
		int before = list_index_inList;
		if(list_index_inList != list_index_bottom - list_index_top - 1)
			list_index_inList++;
		if(++list_index_current >= list_index_bottom){
			if(before == list_index_bottom - list_index_top - 1){
				list_index_top++;
				list_index_bottom++;
			}
		}
		for (int i = list_index_top; i < list_index_bottom; i++) {
			list_viewer[i - list_index_top].setText(String.valueOf(i+1));
			before_bet[i - list_index_top].setText(String.valueOf(list.get(i).get_Bet_amount()));
			after_bet[i - list_index_top].setText(
					String.valueOf(list.get(i).get_Left_cash())
					+"(" 
					+ (Integer.signum(list.get(i).get_Eval()) > 0 ? "+" : "-") //0은 없다 
					+ String.valueOf(list.get(i).get_Bet_amount())
					+ ")"
			);
			if(list.get(i).get_Eval() > 0){
				after_bet[i - list_index_top].setForeground(Color.BLUE);
				before_bet[i - list_index_top].setForeground(Color.BLUE);
			}else{
				after_bet[i - list_index_top].setForeground(Color.RED);
				before_bet[i - list_index_top].setForeground(Color.RED);
			}
		}
		
		////////
		if(list_index_inList - 1 >= 0)
			list_viewer[list_index_inList - 1].setBackground(color_Background);
		list_viewer[list_index_inList].setBackground(Color.CYAN);
		for(int i=0; i<before_cards.length; i++){
			before_cards[i].setSuit(list.get(list_index_current).get_Before_bet_cards()[i].getSuit());
			before_cards[i].setValue(list.get(list_index_current).get_Before_bet_cards()[i].getValue());
			after_cards[i].setSuit(list.get(list_index_current).get_After_bet_cards()[i].getSuit());
			after_cards[i].setValue(list.get(list_index_current).get_After_bet_cards()[i].getValue());					
		}
		resf.repaint();
	}
	class ResultPanelBackground extends JPanel {
		ResultPanelBackground() {
			this.setLayout(null);
			this.setBackground(color_Background);
			this.setVisible(true);
		}
	}
}
