package model;

import java.util.*;

public class Process {
	int PID;
	double startTime;
	double duration;
	int size;
	ArrayList<Page> pages = new ArrayList<Page>();
	boolean isFinished;
	double turnaround;
	double finishTime;
	double waitTime;
	
	public Process(int PID, double startTime, double duration, int size, ArrayList<Page>pages) {
		this.PID = PID;
		this.startTime = startTime;
		this.duration = duration;
		this.size = size;
		this.pages = pages;
		this.isFinished = false;
		this.waitTime = 0;
	}
	
	public Process() {
		
	}
	
}