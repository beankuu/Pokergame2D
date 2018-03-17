package objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GUI_Coin extends JPanel{
	private final String image_location = "images/Coins/chip1.png";
	private ImageIcon image = new ImageIcon(this.getClass().getClassLoader().getResource(image_location));
	private Image img = image.getImage();
	private boolean isBet = false;
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(img, 0,0,this.getWidth(),this.getHeight(), null);
	}
	public void flip_is_Bet(){
		isBet ^= false;
	}
	public void set_As_Bet(){
		isBet = true;
	}
	public void set_As_Not_Bet(){
		isBet = false;
	}
	public boolean get_Bet_State(){
		return isBet;
	}
}
