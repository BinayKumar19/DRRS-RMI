package servers;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DorvalCampusServer {

    public static void main(String[] str) {
        String registryURL, status = "Action couldn't carried out", studentId, timeSlot, date, server;
        Integer roomNumber;

        try {
            startRegistry(1099);
            DorvalServerImpl exportedObj = new DorvalServerImpl();

            registryURL = "Dorval";
            Naming.rebind(registryURL, exportedObj);
            System.out.println("Dorval Server ready.");

            DatagramSocket serverSocket = new DatagramSocket(9878);
            DatagramPacket receivePacket;
            byte[] receiveData = new byte[1024];
            byte[] sendData;
            String[] parts;
            String sReceivedData;

            while (true) {
                receiveData = new byte[1024];
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                sReceivedData = new String(receivePacket.getData());
                if (sReceivedData.contains("/")) {
                    parts = sReceivedData.split("/");
                    studentId = parts[0];
                    roomNumber = Integer.parseInt(parts[1]);
                    date = parts[2];
                    timeSlot = parts[3].trim();
                    status = exportedObj.bookRoom(studentId, "Dorval", roomNumber, date, timeSlot);
                } else if (sReceivedData.contains(":")) {
                    parts = sReceivedData.split(":");
                    server = parts[0];
                    date = parts[1].trim();
                    status = exportedObj.getAvailableTimeSlot(server, date);
                } else if (sReceivedData.contains("]")) {
                    parts = sReceivedData.split("]");
                    studentId = parts[0];
                    String sBookingId = parts[1].trim();
                    status = exportedObj.cancelBooking(studentId, sBookingId);
                }

                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                sendData = status.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            }
        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
    }

    private static void startRegistry(int RMIPortNum)
            throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list();
        } catch (RemoteException e) {
            // No valid registry at that port.
            System.out.println("RMI registry cannot be located at port " + RMIPortNum);
            Registry registry = LocateRegistry.createRegistry(RMIPortNum);
            System.out.println("RMI registry created at port " + RMIPortNum);
        }
    }

}
