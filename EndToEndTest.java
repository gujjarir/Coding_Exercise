import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class EndToEndTest {
    private static final String NO_OUTPUT = "";
    private StringWriter network = new StringWriter();
    private StringWriter console = new StringWriter();

    @Before public void configureMainClassWithFakeOutputs() {
    	Message.setNetwork(network);
    	Message.setConsole(console);
    }

    @Test
     public void sendAnEmail_story1() {
    	Message.main("joe@example.com", "Hi there!");
    	network=(StringWriter) Message.getNetwork();
    	console=(StringWriter)Message.getConsole();
        networkShouldReceive("connect smtp\n" +
                "To: joe@example.com\n" +
                "\n" +
                "Hi there!\n" +
                "\n" +
                "disconnect\n");
        consoleShouldReceive(NO_OUTPUT);
    }

    @Test public void sendAnEmail_AnotherExample_story1() {
    	Message.main("sally@example.com", "Greetings.\nHow's it going?");
    	network=(StringWriter) Message.getNetwork();
    	console=(StringWriter)Message.getConsole();
        networkShouldReceive("connect smtp\n" +
                "To: sally@example.com\n" +
                "\n" +
                "Greetings.\n" +
                "How's it going?\n" +
                "\n" +
                "disconnect\n");
        consoleShouldReceive(NO_OUTPUT);
        System.out.println();
    }

    @Test public void showAnErrorAndDoNotSendIfTheEmailAddressIsInvalid_story2() {
    	Message.main("noatsign", "Hi there!");
    	network=(StringWriter) Message.getNetwork();
    	console=(StringWriter)Message.getConsole();
        networkShouldReceive(NO_OUTPUT);
        consoleShouldReceive("Invalid email address: noatsign\n");
    }

    @Test public void showAnErrorAndDoNotSendIfTheBodyIsInvalid_story3() {
    	Message.main("dinah@example.com","");
    	network=(StringWriter) Message.getNetwork();
    	console=(StringWriter)Message.getConsole();
        networkShouldReceive(NO_OUTPUT);
        consoleShouldReceive("Cannot send an email with no body.\n");
    }
    
    @Test public void showAnErrorAndDoNotSendIfTheBodyIsNotProvided_story3() {
    	Message.main("dinah@example.com");
    	network=(StringWriter) Message.getNetwork();
    	console=(StringWriter)Message.getConsole();
        networkShouldReceive(NO_OUTPUT);
        consoleShouldReceive("Cannot send an email with no body.\n");
    }

    @Test public void sendAnEmailToMultipleAddresses_story4() {
    	Message.main("sally@example.com,joe@example.com", "Hi everyone!");
    	network=(StringWriter) Message.getNetwork();
    	console=(StringWriter)Message.getConsole();
        networkShouldReceive("connect smtp\n" +
                "To: sally@example.com\n" +
                "To: joe@example.com\n" +
                "\n" +
                "Hi everyone!\n" +
                "\n" +
                "disconnect\n");
        consoleShouldReceive(NO_OUTPUT);
    }

    @Ignore @Test public void betterErrorHandlingsForMultipleAddresses_story5() {
    	Message.main("sallyatexample.com,joeatexample.com", "Hi everyone!");
        networkShouldReceive(NO_OUTPUT);
        consoleShouldReceive("Invalid email addresses: sallyatexample.com joeateasmple.com\n");
    }

    @Ignore @Test public void sendAMessageInAnotherFormat_story6() {
    	Message.main("-im", "leslie@chat.example.com", ":-) hey there!");
        networkShouldReceive("connect chat\n" +
                "<leslie@chat.example.com>(:-) hey there!)\n" +
                "disconnect\n");
        consoleShouldReceive(NO_OUTPUT);
    }

    @Ignore @Test public void chatsToMultipleAddressesGetSentIndividually_story7() {
    	Message.main("-im", "leslie@chat.example.com,joey@chat.example.com", "Hello.");
        networkShouldReceive("connect chat\n" +
                "<leslie@chat.example.com>(Hello.)\n" +
                "<joey@chat.example.com>(Hello.)\n" +
                "disconnect\n");
        consoleShouldReceive(NO_OUTPUT);
    }

    @Ignore @Test public void handleErrorsGracefully_story8() {
    	Message.setNetwork(new BadNetworkConnection());
    	Message.main("joe@example.com", "Hi there!");
        consoleShouldReceive("Connection error. Please try again.\n");
    }

    private void networkShouldReceive(String output) {
        assertThat(network.toString(), equalTo(output));
    }

    private void consoleShouldReceive(String output) {
        assertThat(console.toString(), equalTo(output));
    }

    private static class BadNetworkConnection extends Writer {
        @Override public void write(char[] cbuf, int off, int len) throws IOException {
            throw new IOException("Oh no the network is down!!!!!!!111one");
        }

        @Override public void flush() throws IOException {
        }

        @Override public void close() throws IOException {
        }
    }
}
