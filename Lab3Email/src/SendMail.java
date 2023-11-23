/*
 skicka ett mail till dig själv med hjälp av SMTP-protokollet.
 Webbmejlen har följande konfiguration (hämtat från KTH-intranät)
 I detta fall (SMTP med STARTTLS) går du över till kryptering under sessionen.
För att skicka användarnamn och lösenord i SMTP behöver du använda Base64-encoding
 och då kan https://docs.oracle.com/javase/8/docs/api/java/util/Base64.html vara användbart.

Inställningar för att skicka e-post (utgående)
Server: smtp.kth.se
Port: 587
Protokoll: STARTTLS
Autentisering: Normalt lösenord
 */

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Scanner;

public class SendMail {
    private static final String SERVER = "smtp.kth.se";
    private static final int PORT = 587;
    private static final String protocol = "STARTTLS";
    private static final String credentials = "src/credentials.txt";
    private static String userName;
    private static String password;
    private static String recipient;
    private static String subject;
    private static String message;
    private static BufferedReader incoming;
    private static PrintWriter outgoing;
    /*
    telnet smtp.kth.se 587
    Trying 2001:6b0:1:1300:250:56ff:fead:4565...
    Connected to smtp.kth.se.
    Escape character is '^]'.
    220 smtp-5.sys.kth.se ESMTP Postfix
    EHLO smtp.kth.se
    250-smtp-5.sys.kth.se
    250-PIPELINING
    250-SIZE 41943040
    250-STARTTLS
    250-ENHANCEDSTATUSCODES
    250-8BITMIME
    250-DSN
    250-SMTPUTF8
    250 CHUNKING
    STARTTLS
    220 2.0.0 Ready to start TLS
    EHLO smtp.kth.se
    Connection closed by foreign host.
    */
    public static void main(String[] args) {
        try (Scanner file = new Scanner(new File(credentials))) {
            userName = file.nextLine();
            password = file.nextLine();
        } catch (FileNotFoundException e) {
            System.err.println("File to read from could not be found.");
        }
        System.out.println("Email credentials have been read from file.");
        // First we get the recipient, subject and message
        Scanner input = new Scanner(System.in);
        System.out.printf("Recipient: ");
        recipient = input.next();
        input.nextLine();
        System.out.println();
        System.out.printf("Subject: ");
        subject = input.next();
        input.nextLine();
        System.out.println();
        System.out.println("Write your message bellow:");
        // message content; end with <CRLF>.<CRLF>
        message = input.nextLine() + "\r\n.";
        input.close();

        //Connect to the server and set up I/O streams
        try {
            Socket socket = new Socket(SERVER, PORT);
            incoming = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            outgoing = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            System.out.println("Starting SMTP mail service with kth...");
            System.out.println("Server: " + incoming.readLine());
//            System.out.println("Client: EHLO " + SERVER);
            send("EHLO " + SERVER, true);
            //upgrade to secure socket
            startTLS(socket);
            send("AUTH LOGIN", true);
            send(encode64(userName), false);
            send(encode64(password), false);
            send("MAIL FROM:<" + userName + "@kth.se>", false);
            send("RCPT TO:<" + recipient + "@kth.se>", false);
            send("DATA", false);
            send(createHeaders() + message, false);
            send("QUIT", false);

        } catch (IOException exception) {
            System.err.println(exception);
            exception.printStackTrace();
        }
    }

    private static String createHeaders() {
        String date = "Date: " + LocalDateTime.now();
        String from = "From: " + SERVER + " <" + userName + "@kth.se>";
        String subject = "Subject: " + SendMail.subject;
        String to = "To: " + recipient + "@kth.se";
        String newLine = "\r\n";
        return (date + newLine + from + newLine + subject + newLine + to + newLine + newLine);
    }

    private static String encode64(String stringToEncode) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(stringToEncode.getBytes(StandardCharsets.UTF_8));
    }

    //Upgrades the socket connection and sends the EHLO command again
    private static void startTLS(Socket socket) throws IOException {
        send(protocol, true);
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket) factory.createSocket(socket, SERVER, PORT, true);
        incoming = new BufferedReader(new InputStreamReader(sslSocket.getInputStream(), StandardCharsets.UTF_8));
        outgoing = new PrintWriter(new OutputStreamWriter(sslSocket.getOutputStream(), StandardCharsets.UTF_8));
        send("EHLO " + SERVER, true);

    }

    // Writes to the server and reads its response
    private static void send(String message, boolean readResponse) throws IOException {
        System.out.println("Client: " + message);
        outgoing.println(message);
        outgoing.flush();
        if (readResponse) {
            readResponse(message);
        } else {
            System.out.println("Server: " + incoming.readLine());
        }
    }

    private static void readResponse(String myMessage) throws IOException {
        String response;
        while ((response = incoming.readLine()) != null) {
            if (myMessage.startsWith("EHLO")) {
                System.out.println("Server: " + response);
                if (response.startsWith("250 ")) { // every line we get back from EHLO will start with 250
                    break;
                }
//                break; // I need a break for the readLine() function not to get stuck on the last line. Think there's no \r\n there
            } else if (myMessage.startsWith("STARTTLS")) {
                if (response.startsWith("220 ")) {
                    System.out.println("Server: " + response);
                    break;
                }
            } else if (myMessage.startsWith("AUTH LOGIN")) {
                if (response.startsWith("334 ") || response.startsWith("235 ")) {
                    System.out.println("Server: " + response);
                    break;
                }
            }
        }
    }



}
