package animation;


import objects.GUI_Card;
import view.AnimatedFrame;

public class CardSizeChanger extends AnimationWorker{
	private boolean is_To_Visible;
	private GUI_Card[] cards;
	
	public CardSizeChanger(int thread_index, AnimatedFrame af, GUI_Card[] cards, int moving_time , int init_delay, int sliding_intensity, boolean is_To_Visible){
		super(thread_index, af, moving_time, init_delay, sliding_intensity);
		this.af = af;
		this.cards = cards;
		this.is_To_Visible = is_To_Visible;		
	}
	public CardSizeChanger(int thread_index, AnimatedFrame af, GUI_Card card, int moving_time, int init_delay,  int sliding_intensity, boolean is_To_Visible){
		super(thread_index, af, moving_time, init_delay, sliding_intensity);
		this.cards = new GUI_Card[1];
		this.cards[0] = card;
		this.is_To_Visible = is_To_Visible;
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
        while(i++ <= divident ){
        	tick_sum += each_tick;
        	percentage = 1- Math.pow(Math.cos(tick_sum),sliding_intensity);
        	
        	for(int k = 0; k < cards.length; ++k){
        		if(cards[k].isEmptyCard()){
	        		if(is_To_Visible)
	        			cards[k].set_Card_Size_Change(percentage);
	        		else
	        			cards[k].set_Card_Size_Change(1 - percentage);
        		}
        	}
        	try {
				Thread.sleep(moving_time/divident);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	af.repaint();
        }
        for(int k = 0; k < cards.length; ++k){
    		if(cards[k].isEmptyCard()){
        		if(is_To_Visible)
        			cards[k].set_Card_Size_Change(1);
        		else
        			cards[k].set_Card_Size_Change(0);
    		}
    	}
        af.repaint();
        af.start_Next_Thread(thread_index);
	}
}
