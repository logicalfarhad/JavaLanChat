package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import server.ExecuteServer;

public class ClientWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5097465214863225878L;

	private String yahooCommand = "Yahoo!", gnomeCommand = "Default";

	private MessageBoxPane messageBox;
	private Client messenger;

	public ClientWindow() {
		manageComponents();
	}

	private void manageComponents() {
		messageBox = new MessageBoxPane();

		JMenuBar menuBar = new JMenuBar();

		JMenuItem join = new JMenuItem("Join");
		JMenuItem create = new JMenuItem("Create");
		JMenu connectMenu = new JMenu("Connect");

		connectMenu.add(join);
		connectMenu.add(create);

		JMenuItem gnome = new JMenuItem(gnomeCommand, new ImageIcon(
				ClassLoader
						.getSystemResource(Converter.GNOME_BASE + "ICON.png")));
		JMenuItem yahoo = new JMenuItem(yahooCommand, new ImageIcon(
				ClassLoader
						.getSystemResource(Converter.YAHOO_BASE + "ICON.png")));
		JMenu smileyMenu = new JMenu("Smiley");

		smileyMenu.add(gnome);
		smileyMenu.add(yahoo);

		menuBar.add(connectMenu);
		menuBar.add(smileyMenu);

		join.addActionListener(getJoinAction());
		create.addActionListener(getCreateAction());
		gnome.addActionListener(getEmoteChangeAction());
		yahoo.addActionListener(getEmoteChangeAction());

		add(messageBox);
		setJMenuBar(menuBar);
	}

	private ActionListener getJoinAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					InputPanel connectDialog = InputPanel.getJoinInput();
					if (!InputPanel.show(ClientWindow.this, connectDialog,
							"Server Info and Alias")) {
						return;
					}

					messenger = new Client(connectDialog.getInput(0),
							connectDialog.getInputAsInt(1)); // 1 connecting

					String name = connectDialog.getInput(2);
					if (name.trim().length() < 1) {
						messageBox
								.addNotification("Name must contain at least one characted");
						return;
					}

					messenger.sendMessage(connectDialog.getInput(2)); // 2
					// sending
					// name

					if (!messenger.recieveConfirmation()) { // 3 receiving
						// confirmation
						messageBox
								.addNotification("Name already taken, choose another one");
						messenger.closeAll();
						return;
					}

					messenger.setName(connectDialog.getInput(2));
					messageBox.setMessenger(messenger); // 4 & 5
				} catch (UnknownHostException ex) {
					messageBox.addNotification("Server not found.");
				} catch (IOException ex) {
					messageBox.addNotification("Server not found.");
				} catch (NumberFormatException ex) {
					messageBox.addNotification("Invalid port address.");
				}
			}
		};
	}

	private ActionListener getCreateAction() {
		return new ActionListener() {
			private boolean running = true;
			int port;

			@Override
			public void actionPerformed(ActionEvent e) {
				InputPanel input = InputPanel.getCreateInput();
				if (!InputPanel.show(ClientWindow.this, input,
						"Insert port number.")) {
					return;
				}

				try {
					port = input.getInputAsInt(0);
					new Thread("Server Running") {
						public void run() {
							try {
								ExecuteServer.runServer(port);
							} catch (IOException ex) {
								running = false;
								messageBox
										.addNotification("Server Disconnected.");
								return;
							}
						};
					}.start();

				} catch (NumberFormatException ex) {
					running = false;
					messageBox.addNotification("Invalid port address.");
				}

				if (running) {
					messageBox
							.addNotification("Server created at port " + port);
				} else {
					messageBox
							.addNotification("Failed to create server at port "
									+ port);
				}
			}
		};
	}

	private ActionListener getEmoteChangeAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals(yahooCommand)) {
					Converter.setImageBase(Converter.YAHOO_BASE);
				} else if (e.getActionCommand().equals(gnomeCommand)) {
					Converter.setImageBase(Converter.GNOME_BASE);
				}
			}
		};
	}
}
