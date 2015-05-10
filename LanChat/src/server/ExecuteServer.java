package server;

import java.io.IOException;

import common.Protocol;

public class ExecuteServer {
	public static void main(String[] args) {

		int port = Protocol.DEFAULT_PORT;

		try {
			new TheServer(port);
		} catch (IOException e) {
			System.out.println("Could not create server.");
		}
	}

	public static TheServer runServer(int port) throws IOException {
		return new TheServer(port);
	}
}
