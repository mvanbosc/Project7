import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import edu.purdue.cs.cs180.channel.Channel;
import edu.purdue.cs.cs180.channel.ChannelException;
import edu.purdue.cs.cs180.channel.MessageListener;
import edu.purdue.cs.cs180.channel.TCPChannel;

public class Requester implements MessageListener {

	private Channel chan = null;
	private Scanner scan;

	/**
	 * Overridden Constructor. Calls method createChannel with initialized
	 * parameters.
	 */
	public Requester(Channel channel) {
		this.chan = channel;
		createChannel();
	}

	/**
	 * Runs le Code
	 */
	public void runner() {
		System.out.println("1. CL50 - Class of 1950 Lecture Hall");
		System.out.println("2. EE - Electrical Engineering Building");
		System.out.println("3. LWSN - Lawson Computer Science Building");
		System.out.println("4. PMU - Purdue Memorial Union");
		System.out.println("5. PUSH - Purdue University Student Health Center");
		String a = "";
		System.out.print("Enter your location (1-5): ");
		a = getInput();
		if (a.equals("b")) {
			System.out.println("Invalid input. Please try again.");
			runner();
		} else if (a.equals("f")) {
			System.out.println("Invalid number format. Please try again.");
			runner();
		} else {
			sendMessage(a);
			System.out.println("Waiting for volunteer...");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * gets input form user Requester
	 * 
	 * @return String
	 */
	public String getInput() {

		ArrayList<String> a = new ArrayList<String>();
		a.add("CL50");
		a.add("EE");
		a.add("LWSN");
		a.add("PMU");
		a.add("PUSH"); // adds abrev's
		int nums = 0;
		String s = "";
		try {
			s = scan.next(); // get input from user
		} catch (InputMismatchException e) {
			return "f";
		}
		try {
			nums = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return "f";
		}

		if (nums == 1 || nums == 2 || nums == 3 || nums == 4 || nums == 5) {
			return ("REQUEST " + a.get(nums - 1)); // returns Request (location)
		}
		return "b"; // returns Request (location) unless there is an input
					// error.
	}

	@Override
	public void messageReceived(String message, int channelID) {
		// simply print the message.
		message = message.substring(message.length() - 1, message.length());
		System.out.println("Volunteer " + message
				+ " assigned and will arrive shortly.");
		runner();
	}

	/**
	 * Creates the TCP channel with host and port from given constructor and
	 * listens for message.
	 */
	public void createChannel() {
		chan.setMessageListener(this); // starts thread to listen for message
										// back from server
		scan = new Scanner(System.in);
		runner();

	}

	public void sendMessage(String message) {
		// send a message, since we did not specify a client ID, then the
		// message will be sent to the server.
		try {
			chan.sendMessage(message);
		} catch (ChannelException e) {
			e.printStackTrace();
		}
	}

	// Construct the Volunteer with input from the stream
	public static void main(String[] args) { // args[] is the line after Java
												// Volunteers host port
		try {
			new Requester(new TCPChannel("localhost", 8888));
		} catch (ChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}//halp
