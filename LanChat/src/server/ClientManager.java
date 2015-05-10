package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientManager {
	private DataInputStream	receiver;
	private DataOutputStream sender;

	private Socket	socket;

	private String	name;

	public ClientManager(ServerSocket server) throws IOException {
		socket = server.accept();
		receiver = new DataInputStream(socket.getInputStream());
		sender = new DataOutputStream(socket.getOutputStream());
	}

	public void sendMessage(String message, String from) throws IOException {
		sender.writeUTF(from);
		sender.flush();
		sender.writeUTF(message);
		sender.flush();
	}

	public String receiveMessage() throws IOException {
		return receiver.readUTF();
	}
	
	public void sendConfirmation(boolean confirm) throws IOException{
		sender.writeBoolean(confirm);
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void closeAll() throws IOException{
		receiver.close();
		sender.close();
		socket.close();
	}
}
