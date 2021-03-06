package model;

public class SchedulerThread implements Runnable{
	Scheduler sc;
	
	public SchedulerThread(Scheduler sc) {
		this.sc = sc;
	}
	
	@Override
	public void run() {
		sc.simulate();
	}
	
}
