/*
Ett program som ansluter till ditt @kth.se-konto, listar innehållet och sedan
 hämtar ett godtyckligt mejl. Du får (ännu) inte använda JavaMail utan ska istället
 göra det "manuellt" enligt IMAP-protokollet.

 I detta fall (IMAP med SSL/TLS) börjar du med en krypterad session.
 För att skicka användarnamn och lösenord i SMTP behöver du använda Base64-encoding
 och då kan https://docs.oracle.com/javase/8/docs/api/java/util/Base64.html vara användbart.

Inställningar för att ta emot e-post (inkommande)
Server: webmail.kth.se
Port: 993
Protokoll: SSL/TLS
Autentisering: Normalt lösenord
 */

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.sql.Statement;
import java.util.Scanner;

public class RetrieveMail {
    private static final String HOST = "webmail.kth.se";
    private static final int PORT = 993;
    private static final String credentials = "src/credentials.txt";
    private static String userName;
    private static String password;
    //    private static String
    private static BufferedReader incoming;
    private static PrintWriter outgoing;

    /*
    2.2.1.  Client Protocol Sender and Server Protocol Receiver

       The client command begins an operation.  Each client command is
       prefixed with an identifier (typically a short alphanumeric string,
       e.g., A0001, A0002, etc.) called a "tag".  A different tag is
       generated by the client for each command.
     */
    public static void main(String[] args) {
        try (Scanner file = new Scanner(new File(credentials))) {
            userName = file.nextLine();
            userName = userName + "@ug.kth.se";
            password = file.nextLine();
        } catch (FileNotFoundException e) {
            System.err.println("File to read from could not be found.");
        }
        /*
        You login using the 'LOGIN USERNAME PASSWORD\n\r' command
         */
        String loginCommand = "A01 LOGIN " + userName + " " + password;
        //create the secure socket connection and the I/O streams
//        SSLSocketFactory factory
//                = (SSLSocketFactory) SSLSocketFactory.getDefault();
//        SSLSocket sslSocket = null;
        try {
            SSLSocket sslSocket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(HOST, PORT);
            incoming = new BufferedReader(new InputStreamReader(sslSocket.getInputStream(), StandardCharsets.UTF_8));
            outgoing = new PrintWriter(new OutputStreamWriter(sslSocket.getOutputStream(), StandardCharsets.UTF_8));
            System.out.println("Server: " + incoming.readLine());
            outgoing.println(loginCommand);
            outgoing.flush();
            incoming.readLine();

        } catch (IOException exception) {
            System.err.println("Error while trying to establish connection: " + exception.getMessage());
            exception.printStackTrace();
        }

    }
}