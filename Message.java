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
    	
    	//User story: 1
    	String recipient = args[0];
    	String msg = args[1];
    	
    	String str = "connect smtp\nTo: " + recipient + "\n\n" + msg + "\n\ndisconnect\n";
    	StringWriter sw= new StringWriter();
    	sw.append(str);

    	mObj.setNetwork(sw);
 
        
    }

}