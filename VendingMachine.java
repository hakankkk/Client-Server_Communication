VendingMachine.java
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.Integer;
import java.lang.Thread;

public class VendingMachine extends Thread {
	
	private void serverSide(int portNo) throws IOException
	{
		
		String clientInput, clientInput2; 
		String serverOutput;  
		
		System.out.println("items_list.exe is read");
		ServerSocket serverSocket = new ServerSocket(portNo);
		List<String> itemName = new ArrayList<String>();
		Scanner readFile = new Scanner(new File("item_list.txt"));
		String itemsList = "";
		
		while(readFile.hasNext())
		{
			itemsList = readFile.nextLine();
			Scanner itemScan = new Scanner(itemsList);
			itemName.add(itemsList);
		}
		
		readFile = new Scanner(new File("item_list.txt"));
		int countL= 0;
		while(readFile.hasNext())
		{
			readFile.nextLine();
			countL++;
		}
		
		String[] namesArray = new String[itemName.size()];
		namesArray = itemName.toArray(namesArray);
		String splitArr[];
		Integer[] idOfItem = new Integer[countL];
		String[] nameOfItem = new String[countL];
		Integer[] amountOfItem = new Integer[countL];
		int i = 0;
		int j = 0;
		int k = 0;
		System.out.println(Arrays.toString(namesArray));
	    for (String s : namesArray)
	    {
	      splitArr = s.split("\\s+");
	      idOfItem[i]= Integer.parseInt(splitArr[0]);
	      i++;
	      nameOfItem[j]= splitArr[1];
	      j++;
	      amountOfItem[k] = Integer.parseInt(splitArr[2]);
	      k++;
	    	
	    }
	    System.out.println(Arrays.toString(idOfItem));
	    System.out.println(Arrays.toString(nameOfItem));
	    System.out.println(Arrays.toString(amountOfItem));
	    
	    Socket clientSocket;
	    System.out.println("Waiting for a client");
	    clientSocket = serverSocket.accept();
	    System.out.println("Client has connected!");
	    
	    //Open an input stream on the socket
		InputStream is = clientSocket.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader inFromClient = new BufferedReader(isr);
	

		DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
		//----------------------
		while(true)
		{
			clientInput = inFromClient.readLine(); 
			
			/*if (clientInput.equals("Q")){
				System.out.println("\nClient has terminated the connection");
				break;
			}*/
			
			while(!clientInput.equals("Q"))
			{
				if (clientInput.equals("L"))
				{
					outToClient.writeUTF("The received message");
					outToClient.writeUTF(Arrays.toString(namesArray));
				}
				/*if (clientInput.equals("G"))
				{
					System.out.println("The received message:");
					System.out.println("\nGET ITEM");
				}*/
			
			}
			
			clientInput = inFromClient.readLine(); 
			
		
		   
		}
	}
	
	private void clientSide(int portNo, String ipAddress) throws IOException
	{
		Scanner scan = new Scanner(System.in);
		
		String messageType;
		String serverInput;
		
		// creating a socket object to open the client end of the socket that the server created
		Socket clientSocket = new Socket(ipAddress, portNo);
		
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		//BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		 InputStream in = clientSocket.getInputStream();
         DataInputStream inFromServer = new DataInputStream(in);
		System.out.println("The connection is established");
		
		System.out.print("Choose a message type (GET ITEM (L)IST, (G)ET ITEM, (Q)UIT) ");
	    messageType = scan.next();
	    
	   /* if (messageType.equals("Q")){
			
			System.out.println("\nThe summary of received items:");
			outToServer.writeBytes("Q".concat("\r\n"));
			outToServer.flush();
		}*/
	   
	    while(!messageType.equals("Q"))
	    {
	    	if(messageType.equals("L"))
	    	{
	    		outToServer.writeUTF("The received message: \n ");
	    		outToServer.writeUTF("GET ITEM LIST\r\n\r\n");
	    		//System.out.println(inFromServer.readUTF());
	    		
	    	}
	    	else if(messageType.equals("G"))
	    	{
	    		 int idOfItem;
                 int amountOfItem;
                 System.out.print("Enter item id:");
                 idOfItem = scan.nextInt();
                 System.out.print("Enter amount:");
                 amountOfItem = scan.nextInt();
                 
                 System.out.println("The received message: ");
	    		 outToServer.writeUTF("GET ITEM\r\n" + idOfItem + " " + amountOfItem+ "\r\n");
	    		 //String finalAmount = inFromServer.readUTF();
	    	}
	    	
	    	System.out.print("Choose a message type (GET ITEM (L)IST, (G)ET ITEM, (Q)UIT) ");
		    messageType = scan.next();
	    }
	    
	  
	    
		//close socket 
	   	clientSocket.close();
		outToServer.close(); 
	    inFromServer.close(); 

	}
	
	public static void main(String[] args) throws Exception{

		Scanner scan = new Scanner(System.in);

		VendingMachine v = new VendingMachine();

		if(args.length == 1){

			int portNumber = Integer.parseInt(args[0]);
			v.serverSide(portNumber);
		}

		else{

			String hostIP = args[0];		
			int portNumber = Integer.parseInt(args[1]);

			v.clientSide(portNumber, hostIP);
		}
	}

	

}
