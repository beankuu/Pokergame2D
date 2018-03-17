package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
/**
 * 로그인 페이지를 생성하는 JFrame
 * JSlider는 CoinSlider 객체를 받아 생성.
 * 
 * 
 * @author Fami=ly
 *
 *@version 1.0
 *
 *
 *@author 제영이
 *
 *
 *
 */
public class LoginFrame extends JFrame{
	private JLabel name = new JLabel();
	private JTextField namejf = new JTextField(13);
	private JButton okbt = new JButton();
	CoinSlider cs = new CoinSlider(6, 20); // JSlider 설정
	
	private JFrame login_frame = this;	//Listener에게 전달하기 위하여
	
	Color[] color_palettes ={
		Color.decode("#E5EEC1"),
		Color.decode("#A2D4AB"),
		Color.decode("#3EACA8"),
		Color.decode("#547A82"),
		Color.decode("#5A5050"),
	};
	Color backgroundColor = color_palettes[4];
	Color foregroundColor1 = color_palettes[0];
	Color foregroundColor2 = color_palettes[1];
	Color foregroundColor3 = color_palettes[2];
	
	public LoginFrame(){
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setSize(400,200);   //Frame 기본 설정
		setContentPane(new LoginBackground());
		setLayout(null);
		setResizable(false);
		
		okbt.setSize(61,31);
		okbt.setBackground(backgroundColor);
		okbt.setBorderPainted(false);
		okbt.setContentAreaFilled(false);
		okbt.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/btn3.png")));
		okbt.setRolloverIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/btn3_1.png")));
		okbt.setPressedIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/Buttons/btn3_2.png")));
		
		
		name.setText("UserID");
		name.setFont(new Font("Jokerman", Font.BOLD, 16));
		name.setForeground(foregroundColor1);
		name.setSize(70,30);   //name Label
		
		namejf.setSize(115,30); //namejf 설정
		
		cs.setBackground(backgroundColor);
		cs.setForeground(foregroundColor2);
		cs.getColaLabel().setForeground(foregroundColor3);
		add(cs);
		add(name);
		add(namejf);
		add(okbt); // contentpane 삽입;
		
		
		namejf.setLocation(150, 30);
		name.setLocation(70, 30);
		cs.setLocation(70, 70);
		okbt.setLocation(300, 40);   // 각 Component 위치 설정
		
		okbt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(!namejf.getText().equals("")){
					new MainFrame(namejf.getText(), cs.getCoin());
					login_frame.dispose();
				}
			}
		});
		
		namejf.addKeyListener(new loginKeyAdapter());
		okbt.addKeyListener(new loginKeyAdapter());	
		cs.addKeyListener(new loginKeyAdapter());
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((dim.width/2)-(this.getWidth()/2), (dim.height/2)-(this.getHeight()/2));
		setVisible(true);
	}
	class loginKeyAdapter extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			if(e.getKeyChar() == KeyEvent.VK_ENTER){
				if(!namejf.getText().equals("")){
					new MainFrame(namejf.getText(), cs.getCoin());
					login_frame.dispose();
				}
			}
		}	
	}
	class LoginBackground extends JPanel{
		LoginBackground(){
			this.setLayout(null);
			this.setBackground(backgroundColor);
			this.setVisible(true);
		}
	}
}
