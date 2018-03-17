package view;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CoinSlider extends JSlider{
	
	private CoinSlider cs = this;
	private JLabel cola;
	private int coin=0;
	private int default_coin = 0;
	
	CoinSlider(int min, int max){
		super(min,max,(min+max)/2);
		
		coin = (min+max)/2;
		
		setSize(200,50);
		this.setOrientation(JSlider.HORIZONTAL);
		this.setPaintLabels(true);
		this.setPaintTicks(true);
		this.setPaintTrack(true); // JSlider Paint, 정렬상태, 기본 설정 사항
		
		this.setMinorTickSpacing(1);
		this.setMajorTickSpacing(max-min);
		
		cola = new JLabel();
		
		cola.setText("현재 코인 수 :  " +coin);
		cola.setSize(150,50);
		this.add(cola);
		cola.setLocation(40, 15);
		cola.setForeground(Color.BLUE);;
		setVisible(true);
		
		this.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				coin = ((JSlider)e.getSource()).getValue();
				cola.setText("현재 코인 수 :  " + (default_coin + coin));
			}
		});
		this.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_LEFT && cs.getValue()-1 > cs.getMinimum())
					cs.setValue(cs.getValue() - 1);
				else if(e.getKeyChar() == KeyEvent.VK_RIGHT && cs.getValue() + 1 < cs.getMaximum())
					cs.setValue(cs.getValue() + 1);
			}
		});
	}
	public int getCoin(){
		return coin;
	}
	public JLabel getColaLabel(){
		return cola;
	}
	public void set_Default_Coin(int default_coin){
		this.default_coin = default_coin;
		cola.setText("현재 코인 수 :  " + (default_coin + coin));
	}
}
