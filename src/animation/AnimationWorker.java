package animation;

import view.AnimatedFrame;

// 사용 안하고있음
// 애니메이션 양식 예시
public abstract class AnimationWorker implements Runnable{
	protected int thread_index;
	protected AnimatedFrame af;
	protected int moving_time;
	protected int init_delay;
	protected int sliding_intensity;
	protected AnimationWorker(){ };
	protected AnimationWorker(int thread_index, AnimatedFrame af, int moving_time, int init_delay, int sliding_intensity){
		this.thread_index = thread_index;
		this.af = af;
		this.moving_time = moving_time;
		this.init_delay = init_delay;
		this.sliding_intensity = sliding_intensity;
	}
	//Animation의 기본형태이긴 한데... 받는곳이 없어서 안되는 클래스
	public void run(){ }
}
