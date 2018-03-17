package animation;

import javax.swing.JPanel;
import view.AnimatedFrame;

public class PanelMover extends AnimationWorker{
	private int from_X;
	private int from_Y;
	private int current_X;
	private int current_Y;
	private int difference_X;
	private int difference_Y;
	private JPanel[] panels;
	
	public PanelMover(int thread_index, AnimatedFrame af, JPanel[] panels, int moving_time, int init_delay, int from_X, int from_Y, int to_X, int to_Y, int sliding_intensity){
		super(thread_index, af, moving_time, init_delay, sliding_intensity);
		this.panels = panels;
		this.from_X = from_X;
		this.from_Y = from_Y;
	
		this.difference_X = to_X - from_X;
		this.difference_Y = to_Y - from_Y;
	}
	public PanelMover(int thread_index, AnimatedFrame af, JPanel panel, int moving_time, int init_delay, int from_X, int from_Y, int to_X, int to_Y, int sliding_intensity){
		super(thread_index, af, moving_time, init_delay, sliding_intensity);
		this.panels = new JPanel[1];
		this.panels[0] = panel;
		this.from_X = from_X;
		this.from_Y = from_Y;		
		
		this.difference_X = to_X - from_X;
		this.difference_Y = to_Y - from_Y;
	}
	public void run() {
		final int divident = 90;
		double each_tick = Math.toRadians( 90 ) / divident;
		double tick_sum = 0;
		double percentage = 0;
		int i = 0;
		
		//이전 Run이랑 같은객체 움직일 경우 충돌방지용 Bumper
		try {
			Thread.sleep(init_delay);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		for(JPanel panel: panels)
    		panel.setLocation(current_X, current_Y);
        while(i++ <= divident ){
        	tick_sum += each_tick;
        	percentage = 1- Math.pow(Math.cos(tick_sum),sliding_intensity);
        	current_X = (int)(percentage * difference_X) + from_X;
        	current_Y = (int)(percentage * difference_Y) + from_Y;
        	for(JPanel panel: panels)
        		panel.setLocation(current_X, current_Y);
        	try {
				Thread.sleep(moving_time/divident);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	af.repaint();
        }
        af.start_Next_Thread(thread_index);
	}
}
