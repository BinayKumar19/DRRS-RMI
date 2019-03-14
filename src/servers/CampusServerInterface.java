package servers;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CampusServerInterface extends Remote {

	String createRoom (Integer roomNumber, String date, int noTimeSlots, String[] timeSlotsList)
                        throws RemoteException;
	
	String deleteRoom (Integer roomNumber, String date, String[] timeSlotsList)
                        throws RemoteException;
	
	String bookRoom (String studentId, String campusName, Integer roomNumber, String date, String timeSlot)
                        throws RemoteException;
    
	String getAvailableTimeSlot(String server, String date)
    					throws RemoteException;

	String cancelBooking(String studentId, String bookingID)
						throws RemoteException;
}