package vc.main;

import java.util.HashMap;

import vc.gui.Window;

public class Main {

	public HashMap<String, Thread> threads = new HashMap<String, Thread>();
	public static Main main;
	
	public SocketConnector connector;
	
	public Window window;
	
	public int chunkSize = 1024;
	public int transferSize = 100000;
	
	public boolean isEnabled = true;
	
	public static void main(String[] args) {
		Main m = new Main();
		m.enable();
	}
	
	public void enable() {
		main = this;
		new Thread(new CommandHandler()).start();
		
		initWindow();
	}
	
	private void initWindow() {	
		window = new Window(); // TODO extract button listeners
		window.launch();
		
		try {
			window.log("started");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void registerThread(Thread t, String name) {
		t.setName(name);
		t.start();
		threads.put(name, t);
		
		System.out.println("New thread: " + name);
	}
	
}
