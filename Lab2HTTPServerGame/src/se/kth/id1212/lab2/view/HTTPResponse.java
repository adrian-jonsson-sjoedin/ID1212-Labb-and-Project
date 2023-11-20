package se.kth.id1212.lab2.view;

import java.io.PrintWriter;
import java.util.Map;

/**
 * This class generates the servers HTTP and HTML response and sends it to the
 * client browser.
 *
 */
public class HTTPResponse {
    private static final Map<Integer, String> HTTPResponseMessages = Map.of(
            200, "200 OK",
            400, "400 Bad Request",
            404, "404 Not Found",
            405, "405 Method Not Allowed",
            500, "500 Internal Server Error",
            505, "505 HTTP Version Not Supported");
    private PrintWriter outgoing;

    public HTTPResponse(PrintWriter outgoing) {
        this.outgoing = outgoing;
    }

    /**
     *This method constructs the servers response to the browser. It adds the status line, the
     * headers and the body (the HTLM view) to a single String that is then written to the client.
     * @param requestStatusCode the requests status code
     * @param cookie the cookie
     * @param content the servers HTLM response
     */
    public void sendServerResponse(int requestStatusCode, String cookie, String content) {
        String responseLine = "HTTP/1.1 " + HTTPResponseMessages.get(requestStatusCode) + "\r\n";
        String headers = getResponseHeaders(cookie, content.length());
        String body = content + "\r\n\r\n";
        String response = responseLine + headers + body;
//        System.out.println(response); // debugging
        this.outgoing.write(response);
        this.outgoing.flush();
    }

    private String getResponseHeaders(String cookie, int length) {
        System.out.println(cookie);
        String newLine = "\r\n";
        StringBuilder headers = new StringBuilder();
        headers.append("User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/112.0").append(newLine);
        headers.append("Content-length: ").append(length).append(newLine);
        headers.append("Content-Type: text/html; charset=utf-8").append(newLine);
        if (!cookie.equals("")) {
            headers.append("Set-Cookie: clientID=").append(cookie).append(newLine);
//            headers.append("; SameSite=None").append(newLine);
        }
        headers.append(newLine);
        System.out.println("[getResponseHeaders()] "+headers.toString());
        return headers.toString();
    }
}
