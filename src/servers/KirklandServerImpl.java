package servers;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

//@author Binay
public class KirklandServerImpl extends UnicastRemoteObject
                 implements CampusServerInterface{
	
	//Contains Date as key and hRoom as value
	static HashMap<String, HashMap<Integer, HashMap<String, String>>> hDate = 
		   new HashMap<String, HashMap<Integer, HashMap<String, String>>>();
	//Contains Room No. as key and hTimeStudent as value
	static  HashMap<Integer, HashMap<String, String>> hRoom = new HashMap<Integer, HashMap<String, String>>();
	//contains Timeslot as Key and StudentID+Booking ID as Value
	static  HashMap<String, String> hTimeStudent = new HashMap<String, String>(); 

	static  int iBookingIdNo = 1;
	static String[] sStudentIdArray = new String[100];
	static int iStudentBookingCount[] = new int[100];
	static long iStudentBookingTime[] = new long[100];
	  
	protected KirklandServerImpl() throws RemoteException {
			super();
			// TODO Auto-generated constructor stub
		}
  
	public String createRoom (Integer roomNumber,String date,int nNoTimeSlots,String[] ListOfTimeSlots)
	    throws RemoteException
	    {
			BufferedWriter output = null;
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date d1 = new Date();
			String sStatus;
	        try {
	            FileWriter file = new FileWriter("./Logs/KirklandServer.txt",true); 
	            output = new BufferedWriter(file);
	            output.append(dateFormat.format(d1)+" Create Room"+System.lineSeparator());
	            output.append("Date:"+date+System.lineSeparator());
	                		
			if (hDate.containsKey(date))
			 {	if (hDate.get(date).containsKey(roomNumber))
			      { output.append("Room Number:"+roomNumber+" Already Exists"+System.lineSeparator());
				   for(int i=0;i<nNoTimeSlots;i++)
			    	  if ((hDate.get(date).get(roomNumber).containsKey(ListOfTimeSlots[i])))
			    		  output.append("Time Slot:"+ListOfTimeSlots[i]+" Already Present"+System.lineSeparator()); 	    
			    	  else
	  		           { (hDate.get(date).get(roomNumber)).put(ListOfTimeSlots[i], null);
		    		      output.append("Time Slot:"+ListOfTimeSlots[i]+" Created"+System.lineSeparator()); 
		    	       }
			      }
			    else
			    {output.append("Room Number:"+roomNumber+" Created"+System.lineSeparator());
			      for(int i=0;i<nNoTimeSlots;i++)
			      {	 hTimeStudent.put(ListOfTimeSlots[i], null);
				     output.append("Time Slot:"+ListOfTimeSlots[i]+" Created"+System.lineSeparator()); 
			      }
			    (hDate.get(date)).put(roomNumber, hTimeStudent);
			    }	
			 }
			else
			{ output.append("Date Created"+System.lineSeparator());
		      output.append("Room Number:"+roomNumber+" Created"+System.lineSeparator());
		       
		      for(int i=0;i<nNoTimeSlots;i++)
		       { hTimeStudent.put(ListOfTimeSlots[i], null);
		 	     output.append("Time Slot:"+ListOfTimeSlots[i]+" Created"+System.lineSeparator()); 
		       }
		     hRoom.put(roomNumber, hTimeStudent);
			 hDate.put(date, hRoom);
			}
			sStatus ="Room Created";
			output.append(sStatus+System.lineSeparator()); 
			output.append(System.lineSeparator());
	        output.close();	
	        } catch ( IOException e ) {
	        	sStatus ="Room Not Created";
	        	e.printStackTrace();
	        }
	  return sStatus;      
	}

    public String deleteRoom (Integer roomNumber,String date,String[] ListOfTimeSlots)
    throws RemoteException
    { BufferedWriter output = null;
	  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	  Date d1 = new Date();
	  String sStatus;
      try {
        FileWriter file = new FileWriter("./Logs/KirklandServer.txt",true); 
        output = new BufferedWriter(file);
        output.append(dateFormat.format(d1)+" Delete Room"+System.lineSeparator());
     
       for(int i=0;i<ListOfTimeSlots.length;i++)
       {  hRoom.get(roomNumber).remove(ListOfTimeSlots[i]); }
        
       hRoom.remove(roomNumber);
    //   hRoom.remove(date);
       //hDate.get(date)).remove();
      // hDate.get(date).get
      sStatus = "Room Deleted"; 
      
      output.append(sStatus+System.lineSeparator());
      output.append(System.lineSeparator());
      output.close();	
      }catch(Exception e){
    	  sStatus = "Room Not Deleted";
    	  e.printStackTrace();}
       return sStatus;
    }
	    
	public String bookRoomKirkland(String sStudentId,Integer roomNumber,String date,String timeslot) 
	    { String sBookingId;
		  String sBookingStatus=null;
		  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		  Date d1 = new Date();
		  BufferedWriter output = null;
		  synchronized(this){	    			
		  try {
		 	   
		  FileWriter file = new FileWriter("./Logs/KirklandServer.txt",true); 
	        output = new BufferedWriter(file);
	        output.append(dateFormat.format(d1)+" Book Room"+System.lineSeparator());
	        output.append("Booking Room:"+roomNumber+System.lineSeparator());        
	        output.append("Booking Date:"+date+System.lineSeparator());
	        output.append("Booking Time:"+timeslot.length()+System.lineSeparator());
	        
	      if (hDate.containsKey(date))
	       { if (hDate.get(date).containsKey(roomNumber))
	       		{  if (hDate.get(date).get(roomNumber).containsKey(timeslot))
	       			{  if (hDate.get(date).get(roomNumber).get(timeslot)==null)
	       			   {  sBookingId = "K"+(iBookingIdNo++);
	       			      (hDate.get(date).get(roomNumber)).put(timeslot, sStudentId+sBookingId);
	       			      sBookingStatus = "Room Booked. Booking Id: "+sBookingId;
	       			    }  
	       			   else 
                        {  sBookingStatus =  "Time Slot Already Taken by Someone";} 
	       			}
                  else 
                    {sBookingStatus =  "Booking Time not Present in System";} 
                }
	          else
	    	    {sBookingStatus =  "Room not present in System";}
	        }
	      else 
	    	{ sBookingStatus =  "Date not present in System";}
	      
	      output.append(sBookingStatus+System.lineSeparator());
		  output.append(System.lineSeparator());
		  output.close();
		    } catch ( IOException e ) {
		            e.printStackTrace();
		       }
	    }		    
	    return 	sBookingStatus;
	 }
	    
	public String bookRoom (String sStudentId,String campusName,Integer roomNumber,String date,String timeslot) 
	    throws RemoteException
	    {  String sBookingStatus=null;
	    
  	       for(int i=0;i<sStudentIdArray.length;i++)
	       { if (sStudentIdArray[i]!=null )
	         if ((sStudentIdArray[i].equals(sStudentId) &&
             (iStudentBookingCount[i]==3 &&
   	  	     (System.currentTimeMillis()/3600000-iStudentBookingTime[i])<168)))
               { sBookingStatus = "Booking Count Reached to Maximum for the Student";
   	            return sBookingStatus;
                }
	       }  

	      try{	    
	      if (campusName.equals("Kirkland"))
	    	{sBookingStatus = bookRoomKirkland(sStudentId,roomNumber,date,timeslot);}
	      else if (campusName.equals("Dorval")||campusName.equals("Westmount"))
	         {{DatagramSocket clientSocket = new DatagramSocket();
	         InetAddress IPAddress = InetAddress.getByName("localhost");
	         byte[] sendData = new byte[1024];
	         byte[] receiveData = new byte[1024];
	         String sentence =  sStudentId+"/"+roomNumber+"/"+date+"/"+timeslot;
	         sendData = sentence.getBytes();
	         if (campusName.equals("Dorval"))
	         {DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9878);
	         clientSocket.send(sendPacket);
	         }
	         else
	         {DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9877);
	          clientSocket.send(sendPacket);
	         }  	 
	         DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	         clientSocket.receive(receivePacket);
	         sBookingStatus = new String(receivePacket.getData());
	         clientSocket.close();
	        }
	      }
	      else 
	      {sBookingStatus =  "Server Name is Incorrect";}
	    } catch(IOException e ) {
	           e.printStackTrace();
	        }
	    if (sBookingStatus.contains("Room Booked. Booking Id:"))
	      { for(int i=0;i<sStudentIdArray.length;i++)
		       { if (sStudentIdArray[i]!=null && (sStudentIdArray[i].equals(sStudentId)))
	                  { if (System.currentTimeMillis()/3600000-iStudentBookingTime[i]>=168)
	                     { iStudentBookingCount[i]=1;
	                       iStudentBookingTime[i]=System.currentTimeMillis()/3600000;
	                       break;
	                     }
	                    else
	    	             iStudentBookingCount[i]++;
	                     break;
	                  }
	              else if (sStudentIdArray[i]==null)
	                  {  sStudentIdArray[i] = sStudentId;
	                     iStudentBookingCount[i]=1;
	                     iStudentBookingTime[i]=System.currentTimeMillis()/3600000;
	                     break;
	                  }
	            }      
	    }
	    
	    
	    return sBookingStatus;	    	
	    }
	    
	public String getAvailableTimeSlotKirkland(String date)
    throws RemoteException
     { String sBookingStatus="Kirkland:";
       int iTimeSlotCount=0;
        
       for (String keyTimeSlot : hTimeStudent.keySet())
        { if (hTimeStudent != null && hTimeStudent.get(keyTimeSlot) == null)
           {  iTimeSlotCount++;
           }
         }      
       return (sBookingStatus+iTimeSlotCount);
	}
    
    public String getAvailableTimeSlot(String sServer, String date)
	    throws RemoteException
	{  String sTimeSlotCount = getAvailableTimeSlotKirkland(date);
       if (sServer.equals("Kirkland"))   	
       {try
       {
           DatagramSocket clientSocket = new DatagramSocket();
           InetAddress IPAddress = InetAddress.getByName("localhost");
           byte[] sendData = new byte[1024];
           byte[] receiveData = new byte[1024];
           String sentence =  sServer+":"+date;
           sendData = sentence.getBytes();
          
          //To get TimeSlot from Westmount Server
          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9877);
          clientSocket.send(sendPacket);
          DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
          clientSocket.receive(receivePacket);
          sTimeSlotCount = (sTimeSlotCount +(new String(receivePacket.getData()))).trim();
          clientSocket.close();
       
          //Resetting variables
          receiveData = new byte[1024];
          
          //To get TimeSlot from Dorval Server
          clientSocket = new DatagramSocket();
          sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9878);
          clientSocket.send(sendPacket);
          receivePacket = new DatagramPacket(receiveData, receiveData.length);
          clientSocket.receive(receivePacket);
          sTimeSlotCount = (sTimeSlotCount +(new String(receivePacket.getData()))).trim();
          clientSocket.close();
          }catch(Exception e){e.printStackTrace();} 
       
       }
      return sTimeSlotCount;
	}

	public String cancelBookingKirkland(String sBookingID)
	{ BufferedWriter output = null;
	  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	  Date d1 = new Date();
	  String sBookingStatus="Wrong Booking ID";
      try {
    	  FileWriter file = new FileWriter("./Logs/KirklandServer.txt",true); 
        output = new BufferedWriter(file);
        output.append(dateFormat.format(d1)+" Cancel Booking"+System.lineSeparator());
        output.append("Booking ID:"+sBookingID.substring(8)+System.lineSeparator());        
       // output.close();
          
        for (String keyTimeSlot : hTimeStudent.keySet())
        { if (hTimeStudent.get(keyTimeSlot)!=null &&
        		 hTimeStudent.get(keyTimeSlot).equals(sBookingID))
           {  hTimeStudent.put(keyTimeSlot, null);
              sBookingStatus = "Booking Cancelled";
              break;
           }
         }      
        output.append(sBookingStatus+System.lineSeparator());
        output.append(System.lineSeparator());
        output.close();
      }
      catch ( Exception e ) {
          e.printStackTrace();
      }
    return sBookingStatus;
	}
		
	public String cancelBooking(String sStudentId,String sBookingID)
	    throws RemoteException
	    {  String sBookingStatus="Wrong Booking ID";
	        
	        if (sBookingID.charAt(0)=='K')
	         { sBookingStatus = cancelBookingKirkland(sStudentId+sBookingID);      
	    	  }
	        else if (sBookingID.charAt(0)=='W'||sBookingID.charAt(0)=='D')
	         {
	        	try {
	          DatagramSocket clientSocket = new DatagramSocket();
	          InetAddress IPAddress = InetAddress.getByName("localhost");
	          byte[] sendData = new byte[1024];
	          byte[] receiveData = new byte[1024];
	          sendData = (sStudentId+"]"+sBookingID).getBytes();
	          if (sBookingID.charAt(0)=='D')
	           { DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9878);
	             clientSocket.send(sendPacket);
	           }
	          else
	           { DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9877);
	             clientSocket.send(sendPacket);	 
	           }	 
	         DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	         clientSocket.receive(receivePacket);
	         sBookingStatus = new String(receivePacket.getData());
	         clientSocket.close();
          } catch ( IOException e ) {
	              e.printStackTrace();
	          }
	         }
	        if (sBookingStatus.contains("Booking Cancelled"))
	        { for(int i=0;i<sStudentIdArray.length;i++)
	  	       { if (sStudentIdArray[i]!=null && (sStudentIdArray[i].equals(sStudentId)))
	                    { if (System.currentTimeMillis()/3600000-iStudentBookingTime[i]>=168)
	                       { iStudentBookingCount[i]--;
	                         break;
	                       }
	                    }
	           }      
	      }
	         
	      return sBookingStatus;
	    }
	}