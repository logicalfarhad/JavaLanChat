package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import common.Constants;
import common.Protocol;

public class Client extends Socket implements Protocol, Constants {
	private DataInputStream receiver;
	private DataOutputStream sender;

	private String name;

	public Client(String host, int port) throws UnknownHostException,
			IOException {
		super(host, port);
		receiver = new DataInputStream(getInputStream());
		sender = new DataOutputStream(getOutputStream());
	}

	public int recieveFrom() throws IOException {
		return receiver.readInt();
	}

	public String receiveMessage() throws IOException {
		return receiver.readUTF();
	}

	public void sendMessage(String message) {
		try {
			sender.writeUTF(message);
			sender.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean recieveConfirmation() throws IOException {
		return receiver.readBoolean();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void closeAll() throws IOException {
		sender.close();
		receiver.close();
		close();
	}

	@Override
	public String toString() {
		return getInetAddress().getHostAddress() + ":" + getPort();
	}
}
