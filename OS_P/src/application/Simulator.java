package application;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;
//from all to ready
import model.Frame;
import model.Page;
import model.MyProcess;
import model.ProcessThread;
import model.Scheduler;
//import model.SchedulerThread;
import model.SchedulerThread;

public class Simulator{
	public static int numOfProcesses;
	public static int physicalMemorySize;
	public static int minNumOfFrames;
	public static ArrayList<MyProcess> processList;
	public static ArrayList<ProcessThread> allThreads;
	public static int totalPageFaults = 0;
	
	public static Frame[] memory;
	public static int filledSize;
	public static int memoryIndex;
	public static Page nextPage; 
	public static Queue<ProcessThread> readyQueue;
	public static Queue<ProcessThread> blockedQueue;
	public static int quantum;
	//1000 cycles in a second
	public static double time;
	public static int algorithm=1;
	public static String finalResult = "";
	public Simulator() {
		memory = new Frame[physicalMemorySize];
		for(int i=0; i<physicalMemorySize; i++) {
			memory[i] = new Frame();
		}
		readyQueue = new LinkedList<ProcessThread>();
		blockedQueue = new LinkedList<ProcessThread>();
		quantum = 20; 
		time = 0;
		filledSize = 0;
		memoryIndex = 0;
		allThreads = new ArrayList<ProcessThread>();
	}
	

	
	public boolean readFile(String filename) throws FileNotFoundException {
		
		File input = new File(filename);
		try {
			
			Scanner sc = new Scanner(input);
			if(!sc.hasNextLine()) { errorInFile(); sc.close(); return false;}
			String line = sc.nextLine();
			if(line.contains(" ")) {
				errorInFile();
				sc.close();
				return false;
			}
			else {
				numOfProcesses = Integer.parseInt(line);
			}
			if(!sc.hasNextLine()) {errorInFile(); sc.close(); return false;}
			line = sc.nextLine();
			if(line.contains(" ")) {
				errorInFile();
				sc.close();
				return false;
			}
			else {
				physicalMemorySize = Integer.parseInt(line);
			}
			if(!sc.hasNextLine()) {  errorInFile(); sc.close(); return false;}
			line = sc.nextLine();
			if(line.contains(" ")) {
				errorInFile();
				sc.close();
				return false;
			}
			else {
				minNumOfFrames = Integer.parseInt(line);
			}
			
			ArrayList<MyProcess> tempList = new ArrayList<MyProcess>();
			if(!sc.hasNextLine()) {errorInFile(); sc.close(); return false;}
			
			while(sc.hasNextLine()) {
				
				line = sc.nextLine();
				if(line.equals("")) {
					continue;
				}
				if(!line.startsWith("-")) {
					String[] contS = line.split(" ");
					for(int i=0; i<contS.length; i++) {
						int pageNumber = (int) (Integer.parseInt(contS[i].trim(), 16) / Math.pow(2, 12));
						if(pageNumber >= 0 && pageNumber < tempList.get(tempList.size()-1).size) {
							int PID = tempList.get(tempList.size()-1).PID;
							tempList.get(tempList.size()-1).pages.add(new Page(PID, pageNumber ,contS[i]));
						}
					}
					continue;
				}
				String[] lineSplit = line.split(" ");
				if(lineSplit.length >= 5) {
					int PID = Integer.parseInt(lineSplit[0]);
					PID*=-1;
					if(!searchPID(PID, tempList)) {
						continue;
					}
					double startTime = Double.parseDouble(lineSplit[1]);
					double duration = Double.parseDouble(lineSplit[2]);
					int size = Integer.parseInt(lineSplit[3]);
					ArrayList<Page> pages = new ArrayList<Page>();
					for(int j=4; j<lineSplit.length; j++) {
						int pageNumber = (int) (Integer.parseInt(lineSplit[j].trim(), 16) / Math.pow(2, 12));
						if(pageNumber >= 0 && pageNumber < size) {
							Page p = new Page(PID, pageNumber, lineSplit[j]);
							pages.add(p);
						}
						else {
							JOptionPane.showInternalMessageDialog(null, "Some pages were neglected");
						}
					}
					
					MyProcess pr = new MyProcess(PID, startTime, duration, size, pages);
					tempList.add(pr);
				}
				else {
					errorInFile();
					sc.close();
					return false;
				}
				
			}
			
			processList = new ArrayList<MyProcess>();
			for(int k=0; k<tempList.size(); k++) {
				processList.add(tempList.get(k));
			}
			makeThreadList();			
			sc.close();
			return true;
		} catch(Exception FileNotFoundException) {
			errorInFile();
			return false;
		}
		
	}
	
	public boolean searchPID(int PID, ArrayList<MyProcess> tempList) {
		for(MyProcess p: tempList) {
			if(p.PID == PID) {
				return false;
			}
		}
		return true;
	}
	
	
	public void errorInFile() {
		JOptionPane.showInternalMessageDialog(null, "Error in File\nTry another file");
	}
	
	public void generateFile(String filename) {
		File newFile = new File(filename);
		try {
			FileWriter myWriter = new FileWriter(newFile, true);

			Random rand = new Random();
			int upperbound = 10;
			int processesNum = rand.nextInt(upperbound) + 1;

			int physicalMemSize = rand.nextInt(1000); // size of physical memory in frames
			int minimumFrames = rand.nextInt(200);// minimum frames per process

			myWriter.write(processesNum + "\n" + physicalMemSize + "\n" + minimumFrames + "\n");

			for (int i = 1; i <= processesNum; i++) {
				// id , start , duration , size , memory traces
				double duration = 1 + 9*rand.nextDouble();
				int size = rand.nextInt(500);
				int numOfMemoryTraces = rand.nextInt(500)+1;
				String memoryTraces = "";
				
				for(int j=0;j<=numOfMemoryTraces;j++) {
					int num = rand.nextInt(500);
					if(num >= size) {
						continue;
					}
					memoryTraces += Integer.toHexString(num);
					if(j!=numOfMemoryTraces-1) {
						memoryTraces+=" ";
					}
				}
				
				myWriter.write("-" + i + " " + 1000*rand.nextDouble() + " " + duration + " " + size + " " + memoryTraces + "\n");
			}
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	
	public void makeThreadList() {
		allThreads = new ArrayList<ProcessThread> ();
		for(int i=0; i<processList.size(); i++) {
			ProcessThread th = new ProcessThread(processList.get(i));
			allThreads.add(th);
		}
	}
	
	public static void startSimulation(){
		memory = new Frame[physicalMemorySize];
		for(int i=0; i<physicalMemorySize; i++) {
			memory[i] = new Frame();
		}
		Thread th = new Thread(new SchedulerThread(new Scheduler()));
		th.run();
		try {
			th.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
