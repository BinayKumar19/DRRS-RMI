package servers;

import java.rmi.Remote;
import java.rmi.RemoteException;
//@author Binay
public interface CampusServerInterface extends Remote {

	String createRoom (Integer room_Number, String date, int nNoTimeSlots, String[] list_Of_Time_Slots)
                        throws RemoteException;
	
	String deleteRoom (Integer room_Number, String date, String[] List_Of_Time_Slots)
                        throws RemoteException;
	
	String bookRoom (String sStudentId, String campusName,Integer roomNumber,String date,String timeslot)
                        throws RemoteException;
    
	String getAvailableTimeSlot(String sServer, String date)
    					throws RemoteException;

	String cancelBooking(String sStudentId, String sBookingID)
						throws RemoteException;

}