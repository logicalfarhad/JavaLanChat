package client;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Execute {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {

		} catch (InstantiationException e) {

		} catch (IllegalAccessException e) {

		} catch (UnsupportedLookAndFeelException e) {

		}

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				ClientWindow w = new ClientWindow();
				w.setAlwaysOnTop(true);
				w.setSize(225, 300);
				w.setDefaultCloseOperation(ClientWindow.EXIT_ON_CLOSE);
				w.setVisible(true);
				Converter.setImageBase(Converter.YAHOO_BASE);
			}
		});
	}
}
