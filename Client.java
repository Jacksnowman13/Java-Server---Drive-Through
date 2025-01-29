package finalexam;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client {
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private Socket socket;
	private JTextArea responseText;
	private JTextField burgersField;
	private JTextField friesField;
	private JTextField shakesField;
	private String host;
	private int port;
	private boolean connected = false;

	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void runForever() {
		JFrame frame = new JFrame();
		frame.setSize(500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		GridLayout layout = new GridLayout(4, 5);
		panel.setLayout(layout);

		burgersField = new JTextField();
		panel.add(new JLabel("Number of Burgers"));
		panel.add(burgersField);
		panel.add(new JLabel(""));

		friesField = new JTextField();
		panel.add(new JLabel("Number of Fries:"));
		panel.add(friesField);
		panel.add(new JLabel(""));

		shakesField = new JTextField();
		panel.add(new JLabel("Number of Shakes:"));
		panel.add(shakesField);
		panel.add(new JLabel(""));

		JButton order = new JButton("Order");

		order.addActionListener(e -> sendToServer());

		panel.add(order);

		responseText = new JTextArea(20, 20);

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(BorderLayout.NORTH, panel);
		frame.getContentPane().add(BorderLayout.CENTER, responseText);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				try {
					if (connected) {
						outputStream.writeDouble(-1);
						outputStream.writeDouble(-1);
						outputStream.writeDouble(-1);

						outputStream.close();
						inputStream.close();
						socket.close();
					}
				} catch (IOException e1) {
				}
			};
		});
		frame.setVisible(true);
	}

	private void sendToServer() {
		try {
			if (!connected) {
				socket = new Socket(host, port);
				inputStream = new DataInputStream(socket.getInputStream());
				outputStream = new DataOutputStream(socket.getOutputStream());
				connected = true;
			}

			outputStream.writeDouble(Double.parseDouble(burgersField.getText().trim()));
			outputStream.writeDouble(Double.parseDouble(friesField.getText().trim()));
			outputStream.writeDouble(Double.parseDouble(shakesField.getText().trim()));

			outputStream.flush();
			String report = inputStream.readUTF();
			responseText.append(report + "\n");
		} catch (Exception e) {
			responseText.append(e.getMessage());
		}
	}

	public static void main(String[] args) {
		{
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			Client client = new Client(host, port);
			client.runForever();
		}

	}

}
