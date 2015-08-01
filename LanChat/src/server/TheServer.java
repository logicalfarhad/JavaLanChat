package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import common.Protocol;

public class TheServer extends ServerSocket implements Protocol {
	private HashMap<String, ClientManager>	clients;

	private Semaphore halt;

	public TheServer(int port) throws IOException {
		super(port);
		halt = new Semaphore(1); //instead of synchronized method

		clients = new HashMap<>();

		while (true) {
			System.out.println("Waitting for clients to join");
			runClientAcceptingThread();
		}
	}

	private void runClientAcceptingThread() {
		try {
			halt.acquire();
		} catch (InterruptedException e) {
			System.out.println("Could not acquire semaphore");
			return;
		}
		new Thread() {
			@Override
			public void run() {
				try {
					ClientManager temp = new ClientManager(TheServer.this); // 1 connecting
					halt.release();

					String name = temp.receiveMessage(); // 2 Receiving name
					if (clients.containsKey(name)) {
						temp.sendConfirmation(false); // 3... Sending confirmation
						temp.closeAll();
						return;
					}

					temp.sendConfirmation(true); // ...3 Sending confirmation
					temp.setName(name);
					clients.put(temp.getName(), temp);
					sendNotificationToAll(name + " connected");
					startMessaging(temp);
				} catch (IOException e) {
					System.out
							.println("Could not communicate the newly connected client");
				}
			}
		}.start();
	}

	private void startMessaging(ClientManager client) {
		try {
			while (true) {
				String message = client.receiveMessage(); // 4 Receiving message from a client
				sendMessageToAll(client.getName(), message); // 5 Sending message to all clients
			}
		} catch (IOException e) {
			clients.remove(client.getName());
			sendNotificationToAll(client.getName() + " Disconnected");
		}
	}

	private void sendMessageToAll(String from, String message)
			throws IOException {
		System.out.println(from + ": " + message);

		for (String client : clients.keySet()) {
			clients.get(client).sendMessage(message, from);
		}
	}

	private void sendNotificationToAll(String notification) {
		try {
			sendMessageToAll(FROM_SERVER, notification);
		} catch (IOException e) {
			System.out.println("Failed to notify some clients");
		}
	}
}
