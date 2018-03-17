package objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GUI_Hand_Top extends JPanel{
	private final String image_location_Top = "images/Hands/Hand_Top.png";
	private ImageIcon image_Top = new ImageIcon(this.getClass().getClassLoader().getResource(image_location_Top));
	private Image image = image_Top.getImage();
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(image, 0,0,this.getWidth(),this.getHeight(), null);
	}
}
