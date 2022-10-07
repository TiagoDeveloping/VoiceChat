package vc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import vc.main.SocketConnector;

public class SendButtonListener implements ActionListener {

	
	public SendButtonListener() {
		
	}
	
	public SendButtonListener(SocketConnector connector) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Send{ " + e.getID() + "}");
	}

}
