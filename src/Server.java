import java.util.*;
//penis
import edu.purdue.cs.cs180.channel.*;

public class Server implements MessageListener {
    /**
     * Creates a server channel on the current machine and assigns port 8888 to it.
     */
    private Channel channel;
    //lists for FCFS
    private LinkedList < String > requests; //requests
    private LinkedList < String > volunteer; //volunteers
    private LinkedList < Integer > recID; //requests
    private LinkedList < Integer > volID; //volunteers
    private LinkedList < String > recLoc; //requester location
    private LinkedList < String > volLoc; //volunteer location
    //lists for FCFS
    private String algorithm = "";
    private int countVol=0;
    private String bldg = ""; //VOLUNTEER BUILDING IN MESSAGE
    int distance = 20000000; // distance between the 2 locations
    int[][] bldgArray; //ARRAY WITH BLDG DISTANCES
    /**
     * Constructor.
     */
    public Server(Channel channel, String algorithm) {
        // inform the channel that when new messages are received forward them
        // to the current server object.
            this.algorithm = algorithm;
        this.channel = channel;
        openPort();
    }
    /**
     * Opens the TCP port on the current machine for listening.
     * initializes the linked lists.
     * @param port
     */
    public void openPort() {
            System.out.println("Server running on port " + 8888 + " using the algorithm: " + algorithm);
                            
        channel.setMessageListener(this);
        requests = new LinkedList < String > ();
        volunteer = new LinkedList < String > ();
        recID = new LinkedList < Integer > ();
        volID = new LinkedList < Integer > ();
    }

    public void messageReceived(String message, int clientID) {
            System.out.println("Message receieved= " + message + " clientID= " + clientID);
            if (algorithm.equals("FCFS")) {
            FCFS(message,clientID);
            }
            else if (algorithm.equals("CLOSEST")) {
                    
            }
            else if (algorithm.equals("URGENCY"));
    }
    
            /**
             * Method to Send on Message Recieved 
             * @param message
             * @param clientID
             * 
             */
            public void FCFS(String message,int clientID) {

            if (message.substring(0, 7).equals("REQUEST")) {

                if (volunteer.size() == 0) { // if there is no volunteers, add to queue
                    recID.add(clientID); // add client id to a list
                    requests.add(message.substring(8, message.length()));
                } else if (volunteer.size() > 0) {
                    try {
                        channel.sendMessage("LOCATION " + message.substring(7, message.length()), volID.removeFirst());
                        channel.sendMessage("VOLUNTEER " + volunteer.removeFirst(), clientID);
                    } catch (ChannelException e) {
                        e.printStackTrace();
                    }
                }
            } else if (message.substring(0, 9).equals("VOLUNTEER")) {
                if (requests.size() == 0) {
                    volID.add(clientID); //adds the client id of the volunteer to a list
                    volunteer.add(message.substring(10, message.length()));
                } else if (requests.size() > 0) {
                    try {
                        channel.sendMessage("LOCATION " + requests.removeFirst(), clientID);
                        channel.sendMessage("VOLUNTEER " + message.substring(10, message.length()), recID.removeFirst());
                    } catch (NumberFormatException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ChannelException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
    }
            public void Closest(String message, int clientID) {
                    if(message.substring(0, 9).equals("VOLUNTEER")){
                            bldg = message.substring(10,message.length());
                            volLoc.add(bldg);
                            for(int i=0;i<volLoc.size();i++){
                                    int curDist = getDistance(volLoc.get(i), recLoc.get(0));
                                    if(distance > curDist ){
                                    distance = curDist;
                                    }
                                    else if(distance == curDist)
                                    {
                                            if(i+1 == volLoc.size()){
                                                    volLoc
                                            }
                                    }
                            }
                    }
            }
            //returns distance from volunteers location to requesters
            public int getDistance(String v, String r){
                    int[][] bldgArray = {
                    //CL50,EE,LWSN,PMU,PUSH
                            {0,8,6,5,4},
                            {8,0,4,2,5},
                            {6,4,0,3,1},
                            {5,2,3,0,7},
                            {4,5,1,7,0}};
                    return bldgArray[getBuildInt(v)][getBuildInt(r)];
            }
            //returns the building position in the 2d array bldgArray[][]
            public int getBuildInt(String f){
                    int i = 0;
                    if(f.equals("CL50"))
                            i = 0;
                    else if(f.equals("EE"))
                            i = 1;
                    else if(f.equals("LWSN"))
                            i = 2;
                    else if(f.equals("PMU"))
                            i = 3;
                    else if(f.equals("PUSH"))
                            i = 4;
                    
                    return i;
                    
            }
            
    
    /**
     * constructs a new Server with the port at position args[0] ex. java Server 8888 runs with the port 8888
     */

    public static void main(String[] args) {
        new Server(new TCPChannel(8888),"FCFS");
    }
}