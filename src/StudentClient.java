import servers.CampusServerInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
//@author Binay
public class StudentClient {
	public static void main(String[] args) {
	      
	         InputStreamReader is = new InputStreamReader(System.in);
	         BufferedReader br = new BufferedReader(is);
	         
	         String sStudentId,sChoice,sBookingId,registryURL = null,sServerName = null,
	                sDate = null, sTimeSlot = null,sStatus=null;
	         Integer iRoomNo;
	         
		     BufferedWriter output = null;
	  	     DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	  	     Date d1 = new Date();
	         
	  	     try {
		     System.out.println("Please enter your student ID");
		     sStudentId = br.readLine();  
	  	     FileWriter file = new FileWriter("./Logs/StudentClient_"+sStudentId+".txt",true); 
	         output = new BufferedWriter(file);
	          
	         output.append("Student ID:"+sStudentId+System.lineSeparator());
              
	         if (sStudentId.length()!=8)
	         { sStatus="Invalid Student ID";}
	         
	         try{
	            int i=Integer.parseInt(sStudentId.substring(4));
	            }catch(Exception e)
	             {sStatus="Invalid Student ID";}
	         
	         //Dorval-Campus (DVL), Kirkland-Campus (KKL) and Westmount-Campus (WST)
		     if (sStudentId.substring(0,4).equals("DVLS"))
	        	 registryURL = "Dorval"; 
	         else if (sStudentId.substring(0,4).equals("KKLS"))
	        	 registryURL = "Kirkland";
	         else if (sStudentId.substring(0,4).equals("WSTS"))
	        	 registryURL = "Westmount";
	         else
	          sStatus="Invalid Student ID";
	         
		     if (sStatus !=null)
		      {output.append(sStatus+System.lineSeparator());
	           output.append(System.lineSeparator());
	           output.close();	
	           System.out.println(sStatus);
	           return;
	         }   
	         sServerName = registryURL;
	         // find the remote object and cast it to an interface object
	         CampusServerInterface serverObj = (CampusServerInterface)Naming.lookup(registryURL);
	     
	         System.out.println("what operation do you want to perform");
	         System.out.println("Press 1-To book the room");
	         System.out.println("Press 2-To get the information about available time slot");
	         System.out.println("Press 3-To cancel the booking");
	            
	         sChoice = br.readLine();	
	        
	         if (sChoice.equals("1"))
	          { System.out.println("Please Enter Server Name");
		        sServerName = br.readLine();
	            System.out.println("Please Enter Room Number");
	            iRoomNo = Integer.parseInt(br.readLine());
	            System.out.println("Please Enter Date(DD-MON-YYYY Format)");
	            sDate = br.readLine();
	            System.out.println("Please Enter Time Slot(HH:MM-HH:MM format)");
	            sTimeSlot = br.readLine();
	      
	            output.append(dateFormat.format(d1)+" Book Room"+System.lineSeparator());
	            output.append("Server Name:"+sServerName+System.lineSeparator());        
	            output.append("Booking Room:"+iRoomNo+System.lineSeparator());        
	            output.append("Booking Date:"+sDate+System.lineSeparator());
	            output.append("Booking Time:"+sTimeSlot+System.lineSeparator());
	       
	            // invoke the remote methods
	            sStatus = (serverObj.bookRoom(sStudentId, sServerName, iRoomNo, sDate, sTimeSlot)).trim();
	   	    
	          }    
	         else if (sChoice.equals("2")) //to know about available Time Slots
	          { System.out.println("Please Enter the Date(DD-MON-YYYY Format)");
	            sDate = br.readLine();
	            output.append(dateFormat.format(d1)+" Total Time Slots"+System.lineSeparator());
	            output.append("Date:"+sDate+System.lineSeparator());
	            
	            sStatus = (serverObj.getAvailableTimeSlot(sServerName, sDate)).trim();
	          }  
	         else if (sChoice.equals("3"))  //To Cancel a booking
	          { System.out.println("Please Enter the Booking Id");
	            sBookingId = br.readLine();
	          
	            output.append(dateFormat.format(d1)+" Cancel Booking"+System.lineSeparator());
	            output.append("Server Name:"+sServerName+System.lineSeparator());        
	            output.append("Booking ID:"+sBookingId+System.lineSeparator());
	            
	            sStatus = (serverObj.cancelBooking(sStudentId,sBookingId)).trim();
	          } 
	        
	         System.out.println(sStatus);     
		   	 output.append(sStatus+System.lineSeparator());
		     output.append(System.lineSeparator());
		     output.close();	
		        		         
	      } // end try 
	      catch (Exception e) {
	         System.out.println("Exception in StudentClient: " + e);
	      }     
	   }
}