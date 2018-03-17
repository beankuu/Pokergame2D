package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GUI_Card extends JPanel{
	private int suit; // 1= Hearts, 2= Diamonds, 3=Clubs, 4 = Spades
	private int value; // 1 = Ace占?11 = Jack, 12 = Queen, 13 = King
	
	private int card_Size_X;
	private int card_Size_Y;
	private int roatation_degree;
	private double current_rad = 0;
	private boolean isEmptyCard;
	private boolean isCardSelected = false;
	
	
	private final String image_location_base = "images/Cards/";
	private final String image_type_png = ".png";
	private final String empty_card = "Empty";
	private BufferedImage image;
	private BufferedImage image_non_empty;
	private BufferedImage image_empty;
	
	
	
	public GUI_Card(int roatation_degree, boolean isEmptyCard){
		this.suit = 1;
		this.value = 1;
		this.roatation_degree = roatation_degree;
		this.isEmptyCard = isEmptyCard;
		this.setBackground(new Color(0,0,0,0));
		//img = new ImageIcon(parse_image_location()).getImage();
		try {
			image_empty = ImageIO.read(this.getClass().getClassLoader().getResource(image_location_base + empty_card + image_type_png));
			image_non_empty = ImageIO.read(parse_image_location());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(isEmptyCard)
			image = image_empty;
		else
			image = image_non_empty;
		repaint();
	}
	
	public void setSuit(int s) {
		suit = s;
		//img = new ImageIcon(parse_image_location()).getImage();
		try {
			image_non_empty = ImageIO.read(parse_image_location());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(isEmptyCard)
			image = image_empty;
		else
			image = image_non_empty;
		repaint();
	}
	

	public void setValue(int v) {
		value = v;
		//img = new ImageIcon(parse_image_location()).getImage();
		try {
			image_non_empty = ImageIO.read(parse_image_location());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(isEmptyCard)
			image = image_empty;
		else
			image = image_non_empty;
		repaint();
	}
	public void set_Degree(int degree){
		roatation_degree = degree;
	}
	public void set_current_degree_to_zero(){
		current_rad = 0;
	}
	public void setSize(int width, int height) {
		super.setSize(width, height);
		card_Size_X = width;
		card_Size_Y = height;
	}
	public void set_Is_Empty_Card(boolean isEmptyCard){
		this.isEmptyCard = isEmptyCard;
		if(isEmptyCard)
			image = image_empty;
		else
			image = image_non_empty;
		repaint();
	}
	public boolean isEmptyCard(){
		return isEmptyCard;
	}
	//앞-뒷면 변경할때
	public void flip_Card(){
		isEmptyCard ^= true;
		if(isEmptyCard)
			image = image_empty;
		else
			image = image_non_empty;
	}
	//주위 테두리 유무관련
	public void check_Card(){
		isCardSelected ^= true;
	}
	private URL parse_image_location(){
		return this.getClass().getClassLoader().getResource(image_location_base + parse_card_type() + image_type_png);
	}
	private String parse_card_type(){
		switch(suit){
			case 1: return "h" + String.valueOf(value);
			case 2: return "d" + String.valueOf(value);
			case 3: return "c" + String.valueOf(value);
			case 4: return "s" + String.valueOf(value);
			default: return "h1";	//in case of error >> set as a1
		}
	}
	public boolean isTransparentXY( int x, int y ) {
		if(x < 0 || y < 0) return false;
		if(x >= 180 || y >= 180) return false;
		int color = image.getRGB(x, y);
		// 0xff ?? ?? ??
		if(!Integer.toHexString(color >> 24 & 0xff).equals("0"))
			return true;
		return false;
	}
	//스레드가 사용하는 메소드
	public void rotate_Degree(double percentage){
		this.current_rad = percentage * Math.toRadians( roatation_degree );
		this.repaint();
	}
	public void set_Card_Size_Change(double percentage){
		this.card_Size_X = (int)(this.getWidth() * percentage);
		this.card_Size_Y = (int)(this.getHeight() * percentage);
		this.repaint();
	}
	
	protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.rotate(current_rad,this.getWidth()/2,this.getHeight()/2);
        g2.drawImage(image, 0,0,card_Size_X, card_Size_Y, null);
        if(isCardSelected){
        	g2.setColor(Color.CYAN);
        	g2.drawRect( 40 - 5, 28 - 5, card_Size_X - (40 - 5)*2, card_Size_Y - (28 - 5)*2);
        }
	}

	public int getSuit() {
		return suit;
	}
	public int getValue() {
		return value;
	}

}
