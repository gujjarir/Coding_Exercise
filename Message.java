import java.io.*;
import java.util.ArrayList;
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
    	
    	String recipient = "";
    	String msg = "";
    	StringWriter sw= new StringWriter();
    	
    	if(args.length>1){
    		recipient = args[0];
    	 	msg = args[1];    	 	
    	}    		
    	else if(args.length==1)
    		recipient = args[0];    	
    		
    	
    	//User story: 1, 2, 3
    	if(msg.length()>0){    		
    	 	//Validating email pattern using Regex
        	Matcher match = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(recipient);
            if(match.find()) {
            	String str = "connect smtp\nTo: " + recipient + "\n\n" + msg + "\n\ndisconnect\n";
            	sw.append(str);
            	mObj.setNetwork(sw);        	
             }
            else {
            	 String str = "Invalid email address: " + recipient + "\n" ;
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
