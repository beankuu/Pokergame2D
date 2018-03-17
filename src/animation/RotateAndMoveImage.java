package animation;

import objects.GUI_Card;
import view.AnimatedFrame;

public class RotateAndMoveImage extends AnimationWorker{
	private GUI_Card[] cards;
	private  boolean is_To_Rotate_To_Degree;
	public RotateAndMoveImage(int thread_index, GUI_Card card, AnimatedFrame af, int moving_time, int init_delay, int sliding_intensity, boolean is_To_Rotate_To_Degree){
		super(thread_index, af, moving_time, init_delay, sliding_intensity);
		this.cards = new GUI_Card[1];
		this.cards[0] = card;
		this.is_To_Rotate_To_Degree = is_To_Rotate_To_Degree;
	}
	public RotateAndMoveImage(int thread_index, GUI_Card[] cards, AnimatedFrame af, int moving_time, int init_delay, int sliding_intensity, boolean is_To_Rotate_To_Degree) {
		super(thread_index, af, moving_time, init_delay, sliding_intensity);
		this.cards = cards;
		this.is_To_Rotate_To_Degree = is_To_Rotate_To_Degree;
	}
	//based in Sin wave
	public void run(){
		final int divident = 90;
        double each_tick = Math.toRadians( 90 ) / divident;
        double tick_sum = 0;
        double percentage = 0;
        int i = 0;
        try {
			Thread.sleep(init_delay);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
        //tick은 최종적으로 1
        while(i++ <= divident){
        	tick_sum += each_tick;
        	percentage = 1- Math.pow(Math.cos(tick_sum),sliding_intensity);
        	for(GUI_Card card : cards){
        		if(is_To_Rotate_To_Degree)
        			card.rotate_Degree(percentage);
        		else
        			card.rotate_Degree(1- percentage);
        	}
        	if(is_To_Rotate_To_Degree)
        		af.move_cards(percentage);
        	else
        		af.spread_out_cards(percentage);
        	
        	try {
				Thread.sleep(moving_time / divident);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            af.repaint();
        }
        if(is_To_Rotate_To_Degree)
    		af.move_cards(1);
    	else
    		af.spread_out_cards(1);
        af.start_Next_Thread(thread_index);
	}
}
