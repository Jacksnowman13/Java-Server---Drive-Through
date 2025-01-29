package finalexam;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicBorders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;

public class Server {
	private int port;
	private static int orderNumber = 999;

	public Server(int port) {
		this.port = port;
	}

	File orderLog = new File("orderlog.txt");

	ArrayList<String> IPaddress = new ArrayList<>();
	ArrayList<String> Orders = new ArrayList<>();

	public void runForever() {
		try (ServerSocket serverSocket = new ServerSocket(this.port)) {
			while (true) {
				System.out.println("listening on port " + this.port);
				Socket clientSocket = serverSocket.accept();
				System.out.println("connection from " + clientSocket.getInetAddress().getHostAddress());

				Thread client = new Thread(new ClientTask(clientSocket));
				client.start();

				IPaddress.add(clientSocket.getInetAddress().getHostAddress());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException {
		int port = Integer.parseInt(args[0]);
		Server server = new Server(port);
		server.runForever();

	}

	class ClientTask implements Runnable {
		private Socket socket;

		public ClientTask(Socket socket) {
			this.socket = socket;

		}

		public void run() {
			try {

				DataInputStream input = new DataInputStream(socket.getInputStream());
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				while (true) {
					double burgers = input.readDouble();
					double fries = input.readDouble();
					double shakes = input.readDouble();

					if (burgers < 0 && fries < 0 && shakes < 0) {
						break;
					}
					Order order = new Order(1000, burgers, fries, shakes);
					++orderNumber;

					FileWriter log = new FileWriter("orderlog.txt");
					PrintWriter tlog = new PrintWriter(log);

					String report = "Order total is " + order.getTotal() + " order number is " + orderNumber;
					Orders.add(report);

					output.writeUTF(report);
					output.flush();

					for (int i = 0; i < Orders.size(); i++) {
						tlog.println(Orders.get(i) + " " + "IP Address is: ");
						tlog.println(IPaddress);
						tlog.flush();

					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
