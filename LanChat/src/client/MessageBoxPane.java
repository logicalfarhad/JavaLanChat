package client;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import common.Constants;
import common.Protocol;

public class MessageBoxPane extends JPanel implements Constants {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String I[] = { "<i>", "</i>" },
			B[] = { "<b>", "</b>" }, BR = "<br>", HR = "<hr>";

	private Client messenger;
	private WritingBox writer;
	private ReadingBox reader;
	private JScrollBar verticalReadScrollBar;

	public MessageBoxPane() {
		super(new BorderLayout(5, 10));
		writer = new WritingBox();
		reader = new ReadingBox();

		JScrollPane readScroll = new JScrollPane(reader);
		readScroll
				.setVerticalScrollBar(verticalReadScrollBar = new JScrollBar());
		verticalReadScrollBar.setBlockIncrement(20);
		add(readScroll);
		add(new JScrollPane(writer), BorderLayout.SOUTH);

		setBorder(BorderFactory.createEtchedBorder());
	}

	public void setMessenger(Client messenger) {
		this.messenger = messenger;
		writer.addKeyListener(writer);
		writer.setEditable(true);
		reader.addNotification("Connected to " + messenger);
		runRecievingThread(); // 5 in here
	}

	public void addMessage(String from, String message) {
		reader.addMessage(from, message);
	}

	public void addNotification(String message) {
		reader.addNotification(message);
	}

	private void runRecievingThread() {
		new Thread() {
			@Override
			public void run() {

				try {
					while (true) {
						String from = messenger.receiveMessage(); // 5..
						// receiving
						// message
						// from
						// server

						if (!from.equals(Protocol.FROM_SERVER)) {
							reader.addMessage(from, messenger.receiveMessage()); // ...5
						} else {
							reader.addNotification(messenger.receiveMessage());
						}
					}
				} catch (IOException e) {
					reader.addNotification("Disconnected");
				}
			}
		}.start();
	}

	class WritingBox extends JTextArea implements KeyListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public WritingBox() {
			super(3, 30);
			setEditable(false);
			setWrapStyleWord(false);
			setLineWrap(true);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				messenger.sendMessage(getText()); // 4 sending message to server
			} else if (e.getKeyCode() == KeyEvent.VK_ALT) {
				append("\n");
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				setText("");
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
	}

	class ReadingBox extends JEditorPane {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String messages = I[0] + "You are not connected" + I[1];
		private String lastFrom = Protocol.FROM_SERVER;

		public ReadingBox() {
			super("text/html", I[0] + "You are not connected" + I[1]);
			setFocusable(false);
			setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					verticalReadScrollBar.setValue(verticalReadScrollBar
							.getMaximum());
				}
			});
		}

		public void addMessage(String from, String newMessage) {
			String lineBreak = lastFrom.equals(from) ? BR : (HR + I[0] + B[0])
					+ from + (B[1] + I[1] + BR);
			append(from, lineBreak + Converter.convert(newMessage));
		}

		public void addNotification(String message) {
			append(Protocol.FROM_SERVER, HR + I[0] + message + I[1]);
		}

		private void append(String from, String msg) {
			messages += msg;
			lastFrom = from;
			setText(messages);
		}
	}
}
