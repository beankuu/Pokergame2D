package objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GUI_Hand_Bottom extends JPanel{
	private final String image_location_Bottom = "images/Hands/Hand_Bottom.png";
	private ImageIcon image_Bottom = new ImageIcon(this.getClass().getClassLoader().getResource(image_location_Bottom));
	private Image image = image_Bottom.getImage();
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(image, 0,0,this.getWidth(),this.getHeight(), null);
	}
}
