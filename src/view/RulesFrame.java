package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RulesFrame extends JFrame{
	private RulesFrame rf = this;
	public RulesFrame(){
		this.setTitle("규칙 창");
		this.setSize(500, 610);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((dim.width/2)-(this.getWidth()/2), (dim.height/2)-(this.getHeight()/2));
		this.setVisible(false);
		this.setLayout(null);
		this.setContentPane(new RulesBackground());
		this.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					rf.setVisible(false);
			}
		});
	}
	class RulesBackground extends JPanel{
		Image img = (new ImageIcon(this.getClass().getClassLoader().getResource("images/Theme/Rules.png"))).getImage();
		public void paintComponent(Graphics g){
			super.paintComponents(g);
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
			rf.repaint();
		}
	}
}
