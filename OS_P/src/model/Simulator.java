package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Simulator {
	public static int numOfProcesses;
	public static int physicalMemorySize;
	public static int minNumOfFrames;
	public static ArrayList<Process> processList;
	public ArrayList<ProcessThread> allThreads;

	public boolean readFile(String filename) throws FileNotFoundException {
		File input = new File(filename);
		try {
			Scanner sc = new Scanner(input);
			if (!sc.hasNextLine()) {
				errorInFile();
				sc.close();
				return false;
			}
			String line = sc.nextLine();
			if (line.contains(" ")) {
				errorInFile();
				sc.close();
				return false;
			} else {
				numOfProcesses = Integer.parseInt(line);
			}
			if (!sc.hasNextLine()) {
				errorInFile();
				sc.close();
				return false;
			}
			line = sc.nextLine();
			if (line.contains(" ")) {
				errorInFile();
				sc.close();
				return false;
			} else {
				physicalMemorySize = Integer.parseInt(line);
			}
			if (!sc.hasNextLine()) {
				errorInFile();
				sc.close();
				return false;
			}
			line = sc.nextLine();
			if (line.contains(" ")) {
				errorInFile();
				sc.close();
				return false;
			} else {
				minNumOfFrames = Integer.parseInt(line);
			}
			ArrayList<Process> tempList = new ArrayList<Process>();
			if (!sc.hasNextLine()) {
				errorInFile();
				sc.close();
				return false;
			}
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				if (line.equals("")) {
					continue;
				}
				String[] lineSplit = line.split(" ");
				if (lineSplit.length >= 5) {
					int PID = Integer.parseInt(lineSplit[0]);
					// check if PID is unique
					double startTime = Double.parseDouble(lineSplit[1]);
					double duration = Double.parseDouble(lineSplit[2]);
					int size = Integer.parseInt(lineSplit[3]);
					ArrayList<Page> pages = new ArrayList<Page>();
					for (int j = 4; j < lineSplit.length; j++) {
						// !check if page number in range
						Page p = new Page(PID, lineSplit[j]);
						pages.add(p);
					}

					Process pr = new Process(PID, startTime, duration, size, pages);
					tempList.add(pr);
				} else {
					errorInFile();
					sc.close();
					return false;
				}

			}
			for (int i = 0; i < tempList.size(); i++) {
				processList.add(tempList.get(i));
			}
			sc.close();
			return true;
		} catch (Exception FileNotFoundException) {
			errorInFile();
			return false;
		}

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
			int processesNum = rand.nextInt(upperbound);

			int physicalMemSize = rand.nextInt(); // size of physical memory in frames
			int minimumFrames = rand.nextInt();// minimum frames per process

			myWriter.write(processesNum + " " + physicalMemSize + " " + minimumFrames + "\n");

			for (int i = 0; i <= processesNum; i++) {
				// id , start , duration , size , memory traces
				int duration = rand.nextInt(10) + 1;
				int size = rand.nextInt();
				String memoryTraces = Integer.toHexString(rand.nextInt()) + " " + Integer.toHexString(rand.nextInt())
						+ " " + Integer.toHexString(rand.nextInt());
				myWriter.write(i + " " + i + " " + duration + " " + size + " " + memoryTraces + "\n");
			}
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public void makeThreadList() {
		for (int i = 0; i < processList.size(); i++) {
			ProcessThread th = new ProcessThread(processList.get(i));
			allThreads.add(th);
		}
	}

}
