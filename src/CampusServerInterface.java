import java.rmi.Remote;
import java.rmi.RemoteException;
//@author Binay
public interface CampusServerInterface extends Remote {

	public String createRoom (Integer room_Number, String date, int nNoTimeSlots, String[] list_Of_Time_Slots)
                        throws RemoteException;
	
	public String deleteRoom (Integer room_Number, String date, String[] List_Of_Time_Slots)
                        throws RemoteException;
	
	public String bookRoom (String sStudentId, String campusName,Integer roomNumber,String date,String timeslot) 
                        throws RemoteException;
    
	public String getAvailableTimeSlot(String sServer, String date)
    					throws RemoteException;

	public String cancelBooking(String sStudentId, String sBookingID)
						throws RemoteException;

}