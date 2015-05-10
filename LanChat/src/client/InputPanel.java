package client;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import common.Protocol;

public class InputPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -839500804997024583L;
	private JTextField[] fields;

	private InputPanel(String... labels) {
		super(new GridBagLayout());
		fields = new JTextField[labels.length];
		GridBagConstraints cons = new GridBagConstraints();

		cons.gridx = 0;
		cons.gridy = GridBagConstraints.RELATIVE;
		cons.fill = GridBagConstraints.BOTH;
		cons.anchor = GridBagConstraints.CENTER;
		cons.insets = new Insets(2, 5, 2, 5);
		for (int i = 0; i < labels.length; i++) {
			add(new JLabel(labels[i], JLabel.RIGHT), cons);
		}

		cons.gridx = 1;
		for (int i = 0; i < labels.length; i++) {
			add(fields[i] = new JTextField(12), cons);
		}
	}

	public String getInput(int i) {
		return fields[i].getText();
	}

	public int getInputAsInt(int i) throws NumberFormatException {
		return Integer.parseInt(fields[i].getText());
	}

	public void setDefaultValues(String... values) {
		for (int i = 0; i < values.length; i++) {
			fields[i].setText(values[i]);
		}
	}

	public static boolean show(Component parentComponent, InputPanel message,
			String title) {
		message.requestFocusInWindow();
		return JOptionPane.showConfirmDialog(parentComponent, message, title,
				JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
	}

	public void disableFields(int... index) {
		for (int i = 0; i < index.length; i++) {
			fields[index[i]].setEnabled(false);
		}
	}

	public static InputPanel getJoinInput() {
		InputPanel joinDialog = new InputPanel("IP", "Port", "Alias");
		joinDialog.setDefaultValues(Protocol.DEFAUT_HOST, Protocol.DEFAULT_PORT
				+ "");

		return joinDialog;
	}

	public static InputPanel getCreateInput() {
		InputPanel createDialog = new InputPanel("Port");
		createDialog.setDefaultValues(Protocol.DEFAULT_PORT + "");

		return createDialog;
	}
}
