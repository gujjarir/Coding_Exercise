import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
    private static Writer network;   
    
    private static Writer console;

    public static void setNetwork(Writer network) {
        Message.network = network;
    }

    public static void setConsole(Writer console) {
        Message.console = console;
    }
    
    public static Writer getNetwork() {
		return network;
    }

    public static Writer getConsole() {
		return console;
    }

    public static void main(String... args) {
    	Message mObj = new Message();
    	
	//User story: 1, 2, 3, 4, 5, 6, 7, 8
    	String[] recipients = {};    	
    	String msg = "";
    	String recipientsList="";
    	boolean isIM=false;
    	
    	StringWriter sw= new StringWriter();
    	
	// Check if network isn’t available (Story - 8)
    	if(network.toString().length()>0){
    		sw.append("Connection error. Please try again.\n");
    		setConsole(sw);
    		return;
    	}
    		
	// If the first argument is "-im", consider it as chat messaging else as an email 
	// and pass respective arguments into their variables
    	if(args[0]=="-im") {
    		isIM=true;
    		recipientsList=args[1];
    		recipients =  recipientsList.split(",");
    	 	msg = args[2];  
    	}    		
    	else {
    		if(args.length>1){
        		recipientsList=args[0];
        		recipients =  recipientsList.split(",");
        	 	msg = args[1];    	 	
        	}    		
        	else if(args.length==1)
        		recipients = recipientsList.split(",");	    		
    	}
    	    	
    	// Check if message body is valid, if not setconsole
    	if(msg.length()>0){    		
    	 	//Validating email pattern using Regex	           	 
        	String temp=""; 
        	String str="" ;
        	ArrayList<String> inValidRec=new ArrayList<>();
        	ArrayList<String> validRec=new ArrayList<>();
        	int count=0;
        	
        	 for (String rep : recipients) {
        		 Matcher match = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(rep);
			 // Based on whether the recipient email address is valid or not, it is added into respective list
        		 if(match.find())
        			 validRec.add(rep);        			        		 
        		 else {      			 
        			 inValidRec.add(rep);
        		 }        		 
        	 }
        	 
		// If all the recipients are valid 
        	 if(inValidRec.isEmpty()) {
			 // If it is Chat message
        		 if(isIM) {
        			 sw.append("connect chat\n");
        			 for(String val:validRec){
    	        		 	sw.append("<" + val + ">("+msg+")\n");	        		   		 
    	        	 	 }      	 
        			 sw.append("disconnect\n");    
        		 }
        		 else{
        			 sw.append("connect smtp\n");
    	        	 	 for(String val:validRec){
    	        		 	sw.append("To: " + val + "\n");	        		   		 
    	        	 	 }      	 
    	        	 	 sw.append("\n" + msg + "\n\ndisconnect\n");          			 
        		 }  	
	        	 mObj.setNetwork(sw); 
        	 }
        	 else {
			 // slightly different error if more than one email address is invalid 
        		 if(inValidRec.size()==1)
        			 str = "Invalid email address: " + inValidRec.get(0) + "\n" ;  
        		 else
        			 str = "Invalid email addresses: "+String.join(" ", inValidRec)+"\n";
        		 sw.append(str);
        		 mObj.setConsole(sw);
        	 }        
    	}
    	else {
       	 String str = "Cannot send an email with no body.\n" ;
         sw.append(str);
       	 mObj.setConsole(sw);
       }  	    		
    
    }

}
