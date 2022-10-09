package vc.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import vc.handlers.VoiceStreamRecieverHandler;
import vc.handlers.VoiceStreamTransmitterHandler;
import vc.main.Main;
import vc.main.SocketConnector;
import vc.voice.BufferedVoiceTransmitter;
import vc.voice.StreamVoiceTransmitter;

public class Window {

	private JFrame frmMessenger;
	private JTextField ip_field, listen_port, msg_field;
	private JTextPane log_pane;

	public Window() {}
	
	public void launch() {
		initialize();
		frmMessenger.setVisible(true);
	}
	
	public void log(String s) throws Exception {
		if (!(frmMessenger.isShowing())) throw new Exception("Windows not launched");
		String c;
		if ((c = this.log_pane.getText()).length() == 0) {
			this.log_pane.setText(s);
		} else {
			this.log_pane.setText(c + "\n" + s);
		}
	}

	private void initialize() {
		frmMessenger = new JFrame();
		frmMessenger.setTitle("Messenger");
		frmMessenger.setResizable(false);
		frmMessenger.setBounds(100, 100, 450, 300);
		frmMessenger.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMessenger.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel init = new JPanel();
		frmMessenger.getContentPane().add(init);
		init.setLayout(new BorderLayout(0, 0));
		
		JPanel connect_panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) connect_panel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		init.add(connect_panel, BorderLayout.NORTH);
		
		listen_port = new JTextField();
		listen_port.setColumns(6);
		connect_panel.add(listen_port);
		
		ip_field = new JTextField();
		ip_field.setColumns(18);
		connect_panel.add(ip_field);

		
		JButton connectButton = new JButton("Connect");
		connectButton.addActionListener(getConnectButtonListener());
		connect_panel.add(connectButton);
		
		JPanel status_panel = new JPanel();
		init.add(status_panel, BorderLayout.CENTER);
		status_panel.setLayout(new BorderLayout(0, 0));
		
		Box verticalBox = Box.createVerticalBox();
		status_panel.add(verticalBox, BorderLayout.WEST);



		JButton thousandBytesButton = new JButton("Talk 1e5b");
		verticalBox.add(thousandBytesButton);
		thousandBytesButton.addActionListener(getTalkButtonListener());

		
		
		log_pane = new JTextPane();
		JScrollPane scroll_pane = new JScrollPane(log_pane);
		status_panel.add(scroll_pane, BorderLayout.CENTER);
		
		
		
		JPanel communication_panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) communication_panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		status_panel.add(communication_panel, BorderLayout.SOUTH);
		
		msg_field = new JTextField();
		msg_field.setColumns(20);
		msg_field.setHorizontalAlignment(SwingConstants.RIGHT);
		communication_panel.add(msg_field);
		
		JButton sendmsg_button = new JButton("Send");
		sendmsg_button.addActionListener(getSendButtonListener());
		sendmsg_button.setHorizontalAlignment(SwingConstants.RIGHT);
		communication_panel.add(sendmsg_button);
	}

	private ActionListener getTalkButtonListener() {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				BufferedVoiceTransmitter vc = new BufferedVoiceTransmitter(Main.main.connector.udpOutStream);
				vc.captureAndTransmit(Main.main.transferSize); // 100.000 bytes of data
			}
		};
	}

	private ActionListener getSendButtonListener() {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					Main.main.connector.udpOutStream.write(msg_field.getText().getBytes());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}

	private ActionListener getConnectButtonListener() {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				int writePort = Integer.parseInt(ip_field.getText().split(":")[1]);
				String writeAddr = ip_field.getText().split(":")[0];
				
				int listenPort = Integer.parseInt(listen_port.getText());
				
				/* connector stuff */
				
				Main.main.connector = new SocketConnector(Main.main.chunkSize, listenPort, writePort, writeAddr);
				Main.main.connector.connect();
				
				/* transmitter stuff */
				Main.main.voiceTransmitter = new StreamVoiceTransmitter(Main.main.connector.udpOutStream); // transmit
				Main.main.voiceStreamTransmitterHandler = new VoiceStreamTransmitterHandler(Main.main.voiceTransmitter); // enable reciever to write to speaker
				
				Main.main.registerThread(new Thread(Main.main.voiceStreamTransmitterHandler), "VoiceStreamTransmitterHandler");
				
				/* reciever stuff */
				Main.main.voicePlayer.enable(); // enable speaker
				Main.main.voiceStreamRecieverHandler = new VoiceStreamRecieverHandler(Main.main.connector.udpInStream, Main.main.voicePlayer); // enable reciever to write to speaker
				
				Main.main.registerThread(new Thread(Main.main.voiceStreamRecieverHandler), "VoiceStreamRecieverHandler");
			}
		};
	}
	
}
