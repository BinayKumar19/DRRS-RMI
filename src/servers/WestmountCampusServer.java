package servers;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
//@author Binay
public class WestmountCampusServer {

	public static void main(String[] str)
	{  String registryURL,sStatus="Action couldn't carried out",sStudentId,date,timeslot,sServer;
	   Integer roomNumber;
    
		try{
			    startRegistry(1099);
			    WestmountServerImpl exportedObj = new WestmountServerImpl();
			    
		         registryURL = "Westmount";
		         Naming.rebind(registryURL, exportedObj);
		       
		         System.out.println("Westmount Server ready.");
		         
		         DatagramSocket serverSocket = new DatagramSocket(9877);
		   	     while(true)
		   	 			{ byte[] receiveData = new byte[1024];
		   	 			  byte[] sendData;
		   	 			  String[] parts;
				   	 	  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		   	 			  serverSocket.receive(receivePacket);
		   	 			  String sReceivedData = new String( receivePacket.getData());
		   	 			if (sReceivedData.contains("/"))
		   	 			  { parts = sReceivedData.split("/");
		   	 			    sStudentId = parts[0];
		   	 		        roomNumber = Integer.parseInt(parts[1]);
		   	 	            date = parts[2];
		   	 	            timeslot =parts[3].trim();
		   	 	        sStatus = exportedObj.bookRoom(sStudentId,"Westmount",roomNumber,date,timeslot);
		   	 			  }
		   	    		else if (sReceivedData.contains(":"))
			   	  	    { parts = sReceivedData.split(":");
			   	 		  sServer = parts[0];
			   	 		  date = parts[1].trim();
			   	 		  sStatus=exportedObj.getAvailableTimeSlot(sServer, date);
			   	 	   } 
		   	    		else if (sReceivedData.contains("]"))
 	 			          {  parts = sReceivedData.split("]");
	 			             sStudentId = parts[0];
	 			             String sBookingId = parts[1].trim();
	 			             sStatus = exportedObj.cancelBooking(sStudentId,sBookingId);  
	 			          }
		   	 			
		   	 	          InetAddress IPAddress = receivePacket.getAddress();
		   	 			  int port = receivePacket.getPort();
		   	 			  sendData = sStatus.getBytes();
		   	 			  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		   	 			  serverSocket.send(sendPacket);
		   	 			}   
		         
		}catch(Exception e)
		    {System.out.println("Exception"+e);
		    }
	}

	 private static void startRegistry(int RMIPortNum)
     throws RemoteException{
     try {
        Registry registry = LocateRegistry.getRegistry(RMIPortNum);
        registry.list( );  // This call will throw an exception
                           // if the registry does not already exist
     }
     catch (RemoteException e) { 
        // No valid registry at that port.
    System.out.println("RMI registry cannot be located at port "+ RMIPortNum);
    Registry registry = LocateRegistry.createRegistry(RMIPortNum);
     System.out.println("RMI registry created at port " + RMIPortNum);
     }
  } // end startRegistry
	
}
