import servers.CampusServerInterface;

import java.io.*;
import java.rmi.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
// @author Binay
public class AdminClient {

	public static void main(String[] args) {
	      	 
	         String sAdminId, sChoice,sStatus=null,registryURL = null, sDate = null;
	         int nNoTimeSlots;
	         BufferedWriter output = null;
	         Integer  iRoomNo;
		     String[] sTimeSlots = new String[30];
		     
		     InputStreamReader is = new InputStreamReader(System.in);
	         BufferedReader br = new BufferedReader(is);
	         DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	  	     Date d1 = new Date();
	  	  
		     try {   
	         
	         System.out.println("Please enter your Admin ID");
	         sAdminId = br.readLine();
	         
	         FileWriter file = new FileWriter("./Logs/AdminClient_"+sAdminId+".txt",true); 
	         output = new BufferedWriter(file);
	         output.append("Admin ID:" + sAdminId+System.lineSeparator());
	       
	         if (sAdminId.length()!=8)
	           sStatus="Invalid Admin ID";
	         
	         try{
	            int i=Integer.parseInt(sAdminId.substring(4));
	            }catch(Exception e)
	             {sStatus="Invalid Admin ID";}
	       
	         
	         //Dorval-Campus (DVL), Kirkland-Campus (KKL) and Westmount-Campus (WST)
	         if (sAdminId.substring(0,4).equals("DVLA"))
	         	 registryURL = "Dorval"; 
	         else if (sAdminId.substring(0,4).equals("KKLA"))
	        	 registryURL = "Kirkland";
	         else if (sAdminId.substring(0,4).equals("WSTA"))
	        	 registryURL = "Westmount";
	         else
	            sStatus="Invalid Admin ID";	
	              
	         if (sStatus !=null)
	          {output.append(sStatus+System.lineSeparator());
	           output.append(System.lineSeparator());
	           output.close();	
	           System.out.println(sStatus);
	           return;
	         }
	             
	         // find the remote object and cast it to an interface object
	         CampusServerInterface serverObj = (CampusServerInterface)Naming.lookup(registryURL);
	            
	         System.out.println("what operation do you want to perform");
	         System.out.println("Press 1-To create a room");
	         System.out.println("2-to delete the room");
	            
	         sChoice = br.readLine();	
	        
	         if (sChoice.equals("1"))  //To Create the Room
	          { System.out.println("Please Enter Room Number:");
	            iRoomNo = Integer.parseInt(br.readLine());
	            System.out.println("Please Enter Date(DD-MON-YYYY Format):");
	            sDate = br.readLine();
	            System.out.println("How many Time slot do you want to enter?");
	            nNoTimeSlots = Integer.parseInt(br.readLine());
	          
	            output.append(dateFormat.format(d1)+" Create Room"+System.lineSeparator());
	            output.append("Server:"+registryURL+System.lineSeparator());
	            output.append("Date:"+sDate+System.lineSeparator());
	            output.append("Room:"+iRoomNo+System.lineSeparator());
		      
	            System.out.println("Please Enter "+nNoTimeSlots +" Time Slots(HH:MM-HH:MM format):");
	            for(int i=0;i<nNoTimeSlots;i++)
	             { sTimeSlots[i] = br.readLine();
	               output.append("TimeSlot:"+sTimeSlots[i]+System.lineSeparator());
	             }
	          
	            // invoke the remote methods
	            sStatus=  serverObj.createRoom(iRoomNo, sDate, nNoTimeSlots,sTimeSlots);
		       
	          }  
	         else if (sChoice.equals("2")) //To Delete the Room
	          { System.out.println("Please Enter Room Number:");
	            iRoomNo = Integer.parseInt(br.readLine());
	          
	            System.out.println("Please Enter Date(DD-MON-YYYY Format):");
	            sDate = br.readLine();
	         
	            System.out.println("How many Time slot do you want to enter?");
	            nNoTimeSlots = Integer.parseInt(br.readLine());
	            
	            output.append(dateFormat.format(d1)+" Delete Room"+System.lineSeparator());
	            output.append("Server:"+registryURL+System.lineSeparator());
	            output.append("Date:"+sDate+System.lineSeparator());
	            output.append("Room:"+iRoomNo+System.lineSeparator()); 
	           
	            System.out.println("Please Enter Time Slot(HH:MM-HH:MM format):");
	            for(int i=0;i<nNoTimeSlots;i++)
	            {   sTimeSlots[i] = br.readLine();
	                output.append("TimeSlot:"+sTimeSlots[i]+System.lineSeparator()); 
	            }
	            sStatus= serverObj.deleteRoom(iRoomNo, sDate, sTimeSlots);
		       }  
	         System.out.println(sStatus);
	         output.append(sStatus+System.lineSeparator());
	         output.append(System.lineSeparator());
	         output.close();	
	         
		     } // end try 
	      catch (Exception e) {
	         System.out.println("Exception in AdminClient: " + e);
	          } 
  }	   
}