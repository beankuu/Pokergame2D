package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Add_Coins_Frame extends JFrame {
	private Add_Coins_Frame acf;
	private MainFrame mf;
	CoinSlider cs2;
	Color[] color_palettes ={
			Color.decode("#E5EEC1"),
			Color.decode("#A2D4AB"),
			Color.decode("#3EACA8"),
			Color.decode("#547A82"),
			Color.decode("#5A5050"),
		};
	Color backgroundColor = color_palettes[4];
	Color foregroundColor1 = color_palettes[0];
	Color foregroundColor2 = color_palettes[2];
	Add_Coins_Frame(MainFrame mainf) {
		this.acf = this;
		this.mf = mainf;
		JButton okbt2 = new JButton();
		setTitle("Add_Coin");
		// 창 종료시 이 창만 닫히게 하려고 함
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(null);
		setSize(400, 150); // Frame 기본 설정
		setLayout(null);
		setContentPane(new AddCoinsBackground());// lp를 컨텐츠 팬으로
		setResizable(false);
		cs2 = new CoinSlider(1, 10); // JSlider 설정
		cs2.setValue(0);
		cs2.setLocation(70, 40);
		cs2.setBackground(backgroundColor);
		cs2.setForeground(foregroundColor1);
		cs2.getColaLabel().setForeground(foregroundColor2);
		
		
		add(cs2);
		
		okbt2.setSize(61, 31);
		okbt2.setLocation(290, 30); // 각 Component 위치 설정
		okbt2.setBackground(backgroundColor);
		okbt2.setBorderPainted(false);
		okbt2.setContentAreaFilled(false);
		okbt2.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/btn3.png")));
		okbt2.setRolloverIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/btn3_1.png")));
		okbt2.setPressedIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/btn3_2.png")));
		
		add(okbt2);
		
		okbt2.addKeyListener(new addCoinKeyAdpater());
		cs2.addKeyListener(new addCoinKeyAdpater());
		addKeyListener(new addCoinKeyAdpater());
		
		okbt2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				///mainframe에게 data전송
				mf.addCoin(cs2.getCoin());
				acf.setVisible(false);
			}
		});
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((dim.width/2)-(this.getWidth()/2), (dim.height/2)-(this.getHeight()/2));
		setVisible(false);
	}
	public void reset_Coin_Slider(){
		cs2.setValue(0);
	}
	public void set_Default_Coin(int Default_Coin){
		cs2.set_Default_Coin(Default_Coin);
	}
	class addCoinKeyAdpater extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			if(e.getKeyChar() == KeyEvent.VK_ENTER){
				///mainframe에게 data전송
				mf.addCoin(cs2.getCoin());
				acf.setVisible(false);
			}
		}	
	}
	class AddCoinsBackground extends JPanel{
		AddCoinsBackground(){
			this.setLayout(null);
			this.setBackground(backgroundColor);
			this.setVisible(true);
		}
	}
}
