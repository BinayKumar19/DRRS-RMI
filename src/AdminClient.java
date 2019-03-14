import servers.CampusServerInterface;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminClient {

    InputStreamReader is;
    BufferedReader br;
    LogWriter lw;

    AdminClient() {
        is = new InputStreamReader(System.in);
        br = new BufferedReader(is);
    }

    private String readLine(String messageToDisplay) {
        String line = null;

        System.out.println(messageToDisplay);
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    private String readLine() {
        String line = null;
        try {
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    public static void main(String[] args) {

        String adminId, status;
        AdminClient client = new AdminClient();

        adminId = client.readLine("Please enter your Admin ID");
        client.lw = new LogWriter("AdminClient_" + adminId + ".txt", true);
        client.lw.writeToLog("Admin ID:" + adminId + System.lineSeparator());

        status = client.performOperation(adminId);

        System.out.println(status);
        client.lw.writeToLog(status + System.lineSeparator() + System.lineSeparator());
        client.lw.closeLogWriter();

    }

    private String performOperation(String adminId) {
        String operationChoice, status = null, registryURL = null, date = null;
        int noTimeSlots;
        Integer roomNo;
        String[] timeSlotsArray = new String[30];

        String serverName = getServerName(adminId);

        // find the remote object and cast it to an interface object
        CampusServerInterface serverObj = getRMIObject(serverName);
        if (serverObj == null) {
            return "Registory Error";
        }

        System.out.println("what operation do you want to perform");
        System.out.println("Press 1-To create a room");
        System.out.println("2-to delete the room");
        operationChoice = readLine();

        roomNo = Integer.parseInt(readLine("Please Enter Room Number:"));
        date = readLine("Please Enter Date(DD-MON-YYYY Format):");
        noTimeSlots = Integer.parseInt(readLine("How many Time slot do you want to enter?"));

        lw.writeToLog("Server:" + registryURL + System.lineSeparator());
        lw.writeToLog("Date:" + date + System.lineSeparator());
        lw.writeToLog("Room:" + roomNo + System.lineSeparator());

        System.out.println("Please Enter Time Slot(HH:MM-HH:MM format):");
        for (int i = 0; i < noTimeSlots; i++) {
            timeSlotsArray[i] = readLine(i + ": ");
            lw.writeToLog("TimeSlot:" + timeSlotsArray[i] + System.lineSeparator());
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date d1 = new Date();

        if (operationChoice.equals("1"))  //To Create the Room
        {
            lw.writeToLog(dateFormat.format(d1) + " Create Room" + System.lineSeparator());
            status = createRoom(serverObj, roomNo, date, noTimeSlots, timeSlotsArray);
        } else if (operationChoice.equals("2")) //To Delete the Room
        {
            lw.writeToLog(dateFormat.format(d1) + " Delete Room" + System.lineSeparator());
            status = deleteRoom(serverObj, roomNo, date, timeSlotsArray);
        }

        return status;
    }

    private String createRoom(CampusServerInterface serverObj, int roomNo, String date, int noTimeSlots, String[] timeSlots) {
        String status = null;

        // invoke the remote methods
        try {
            status = serverObj.createRoom(roomNo, date, noTimeSlots, timeSlots);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return status;
    }

    private String deleteRoom(CampusServerInterface serverObj, int roomNo, String date, String[] timeSlots) {
        String status = null;

        try {
            status = serverObj.deleteRoom(roomNo, date, timeSlots);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return status;
    }

    private CampusServerInterface getRMIObject(String serverName) {
        CampusServerInterface serverObj = null;
        // find the remote object and cast it to an interface object
        try {
            serverObj = (CampusServerInterface) Naming.lookup(serverName);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return serverObj;
    }

    private String getServerName(String adminId) {
        String adminIdPart1 = adminId.substring(0, 4);
        String issue;
        String serverName = null;

        if (adminId.length() != 8) {
            issue = "Admin Id should have a length 8";
            logError(issue);
            return null;

        } else {
            try {
                int i = Integer.parseInt(adminId.substring(4));
            } catch (Exception e) {
                issue = "Admin Id should have last 4 digits numeric";
                logError(issue);
                return null;
            }
        }

        //Dorval-Campus (DVL), Kirkland-Campus (KKL) and Westmount-Campus (WST)
        if (adminIdPart1.equals("DVLA"))
            serverName = "Dorval";
        else if (adminIdPart1.equals("KKLA"))
            serverName = "Kirkland";
        else if (adminIdPart1.equals("WSTA"))
            serverName = "Westmount";
        else {
            issue = "Admin Id Incorrect";
            logError(issue);
            return null;
        }
        return serverName;
    }

    private void logError(String issue) {
        lw.writeToLog(issue + System.lineSeparator());
        lw.writeToLog(System.lineSeparator());
        lw.closeLogWriter();
        System.out.println(issue);

    }

}