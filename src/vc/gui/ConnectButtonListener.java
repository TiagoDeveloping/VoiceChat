package vc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectButtonListener implements ActionListener {

	
	public ConnectButtonListener() {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Connect  | " + e.getID());
	}

}
