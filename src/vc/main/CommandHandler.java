package vc.main;

import java.util.Scanner;

public class CommandHandler implements Runnable {
	
	private Scanner s = new Scanner(System.in);
	
	@Override
	public void run() {
		String cmd;
		while (s.hasNext()) {
			cmd = s.nextLine();
			switch (cmd) {
			case "exit":
				for (Thread thread : Main.main.threads.values()) {
					System.out.print("Closing: " + thread.getName() + " --> ");
					thread.interrupt();
					System.out.println("√");
				}
				s.close();
				System.exit(0);
			}
		}
	}

}
