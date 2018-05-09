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
    	
    	String[] recipients = {};    	
    	String msg = "";
    	String recipientsList="";
    	StringWriter sw= new StringWriter();
    	
    	if(args.length>1){
    		recipientsList=args[0];
    		recipients =  recipientsList.split(",");
    	 	msg = args[1];    	 	
    	}    		
    	else if(args.length==1)
    		recipients = recipientsList.split(",");	
    	    	
    	//User story: 1, 2, 3, 4
    	if(msg.length()>0){    		
    	 	//Validating email pattern using Regex
        	    	           	 
        	String temp="";
        	 for (String rep : recipients) {
        		 Matcher match = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(rep);
        		 if(match.find())
        			 temp +="To: " + rep + "\n";        		 
        		 else
        		 {
        			 String str = "Invalid email address: " + rep + "\n" ;
                 	 sw.append(str);
                	 mObj.setConsole(sw);
                	 return;
        		 }        		 
        	 }
        	 sw.append("connect smtp\n");
        	 sw.append(temp);
        	 sw.append("\n" + msg + "\n\ndisconnect\n");        	
        	 mObj.setNetwork(sw); 
        	            
    	}
    	else {
       	 String str = "Cannot send an email with no body.\n" ;
        	 sw.append(str);
       	 mObj.setConsole(sw);
       }  	    		
    
    }

}
