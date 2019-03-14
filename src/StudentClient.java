import servers.CampusServerInterface;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentClient {
    InputStreamReader is;
    BufferedReader br;
    LogWriter lw;

    StudentClient() {
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

        String studentId, status = null;

        StudentClient client = new StudentClient();

        studentId = client.readLine("Please enter your student ID");

        client.lw = new LogWriter("StudentClient_" + studentId + ".txt", true);
        client.lw.writeToLog("Student ID:" + studentId + System.lineSeparator());

        String serverName = client.getServerName(studentId);

        // find the remote object and cast it to an interface object
        CampusServerInterface serverObj = client.getRMIObject(serverName);
        if (serverObj == null) {
            return;
        }

        System.out.println("what operation do you want to perform");
        System.out.println("Press 1-To book the room");
        System.out.println("Press 2-To get the information about available time slot");
        System.out.println("Press 3-To cancel the booking");

        String operationChoice = client.readLine();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date d1 = new Date();

        if (operationChoice.equals("1")) {
            status = client.bookRoom(serverObj, studentId, dateFormat.format(d1));
        } else if (operationChoice.equals("2")) {
            status = client.getAvailableTimeSlot(serverObj, serverName, dateFormat.format(d1));
        } else if (operationChoice.equals("3")) {
            status = client.cancelBooking(serverObj, studentId, serverName, dateFormat.format(d1));
        }

        System.out.println(status);
        client.lw.writeToLog(status + System.lineSeparator() + System.lineSeparator());
        client.lw.closeLogWriter();
    }

    private String bookRoom(CampusServerInterface serverObj, String studentId, String currentDate) {
        String serverName = readLine("Please Enter Server Name");
        int roomNo = Integer.parseInt(readLine("Please Enter Room Number"));
        String date = readLine("Please Enter Date(DD-MON-YYYY Format)");
        String timeSlot = readLine("Please Enter Time Slot(HH:MM-HH:MM format)");

        lw.writeToLog(currentDate + " Book Room" + System.lineSeparator());
        lw.writeToLog("Server Name:" + serverName + System.lineSeparator());
        lw.writeToLog("Booking Room:" + roomNo + System.lineSeparator());
        lw.writeToLog("Booking Date:" + date + System.lineSeparator());
        lw.writeToLog("Booking Time:" + timeSlot + System.lineSeparator());

        String status = null;
        try {
            status = (serverObj.bookRoom(studentId, serverName, roomNo, date, timeSlot)).trim();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }

        return status;
    }

    private String getAvailableTimeSlot(CampusServerInterface serverObj, String serverName, String currentDate) {
        String Date = readLine("Please Enter the Date(DD-MON-YYYY Format)");
        lw.writeToLog(currentDate + " Total Time Slots" + System.lineSeparator());
        lw.writeToLog("Date:" + Date + System.lineSeparator());

        String status = null;
        try {
            status = (serverObj.getAvailableTimeSlot(serverName, Date)).trim();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }

        return status;
    }

    private String cancelBooking(CampusServerInterface serverObj, String studentId, String serverName, String currentDate) {
        String sBookingId = readLine("Please Enter the Booking Id");

        lw.writeToLog(currentDate + " Cancel Booking" + System.lineSeparator());
        lw.writeToLog("Server Name:" + serverName + System.lineSeparator());
        lw.writeToLog("Booking ID:" + sBookingId + System.lineSeparator());
        String status = null;
        try {
            status = (serverObj.cancelBooking(studentId, sBookingId)).trim();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }

        return status;
    }

    private CampusServerInterface getRMIObject(String server) {
        CampusServerInterface serverObj = null;

        // find the remote object and cast it to an interface object
        try {
            serverObj = (CampusServerInterface) Naming.lookup(server);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return serverObj;
    }

    private String getServerName(String studentId) {
        String adminIdPart1 = studentId.substring(0, 4);
        String issue;
        String serverName = null;

        if (studentId.length() != 8) {
            issue = "Student Id should have a length 8";
            logError(issue);
            return null;

        } else {
            try {
                int i = Integer.parseInt(studentId.substring(4));
            } catch (Exception e) {
                issue = "Student Id should have last 4 digits numeric";
                logError(issue);
                return null;
            }
        }

        //Dorval-Campus (DVL), Kirkland-Campus (KKL) and Westmount-Campus (WST)
        if (adminIdPart1.equals("DVLS"))
            serverName = "Dorval";
        else if (adminIdPart1.equals("KKLS"))
            serverName = "Kirkland";
        else if (adminIdPart1.equals("WSTS"))
            serverName = "Westmount";
        else {
            issue = "Student Id Incorrect";
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