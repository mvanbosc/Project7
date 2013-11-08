import java.util.InputMismatchException;
import java.util.Scanner;

import edu.purdue.cs.cs180.channel.*;


public class Volunteer implements MessageListener {

    private Channel chan = null;
    private Scanner scan;
    /**
     * Constructor.
     */
    public Volunteer(Channel channel) {
        this.chan = channel;
        createChannel();
    }

    /**
     * Does x on message received from server.
     */
    @
    Override
    public void messageReceived(String message, int channelID) {
        // simply print the message.
        message = message.substring(9, message.length());
        System.out.println("Proceed to " + message);

        onEnter();
    }

    /**
     * Creates the TCP channel with host and port from given constructor and listens for message.
     */
    public void createChannel() {
        chan.setMessageListener(this); // ready for message
        onEnter(); //onKeyPress{Enter} - do something
    }
    public void onEnter() {
        scan = new Scanner(System. in ); // initializes the private scanner object
        //checks if enter is pressed
        System.out.println("Press ENTER when ready:");
        String nums = "";
        try {
            nums = scan.nextLine(); //get input from user
        } catch (InputMismatchException e) {
            System.out.println("Error, you didn't press enter");
            onEnter();
        }
        if (nums.equals("")) {
            try {
                chan.sendMessage("VOLUNTEER " + chan.getID());
            } catch (ChannelException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            System.out.println("Waiting for assignment...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error, you didn't press enter");
            onEnter();
        }
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

    /**
     * Creates a new Volunteers with args[] (the line after Java Volunteers host port)
     */
    public static void main(String[] args) {
        try {
            new Volunteer(new TCPChannel("localhost", 8888));
        } catch (ChannelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}