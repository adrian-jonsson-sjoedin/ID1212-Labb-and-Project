package se.kth.id1212.lab2.contoller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * This class represent one HTTP request from the browser and checks it
 */
public class HTTPRequest {
    private final String[] requestLineParameters;
    private final HashMap<String, String> headers;

    public HTTPRequest(BufferedReader incoming) {
        // the request line is on the form 'GET /example/path/resource HTTP/1.1'
        String requestLine = parseRequestLine(incoming);
        System.out.println("[HTTPRequest] constructor: " + requestLine);
        this.requestLineParameters = requestLine.trim().split("\\s+");
//      \\s+ is a regular expression that matches one or more whitespace characters,
//      including space, tab, or newline.
        this.headers = parseHeaders(incoming);

    }

    public String getMethod() {
        return this.requestLineParameters[0];
    }

    public String getURI() {
        return requestLineParameters[1];
    }

    /**
     * Gets the HTTPS request cookie.
     *
     * @return Will return the cookie, or "" if it's not the cookie that our server have set.
     */
    public String getCookie() {
        System.out.println("[HTTPRequest getCookie()]");
        String cookie = "";
        System.out.println("[HTTPRequest getCookie()] "+this.headers.get("Cookie"));
        /* The headers from the browser might not always contain a cookie. For example Firefox gave
         * me 'Cookie: sessionID=9232awZE9ZEGKpTn7gNjPAZMLOc' while chrome did not give me a line.
         * This was in incognito mode on both of them. We don't want the server to use that cookie,
         * instead we want the browser to use the cookie that our server sends it.
         */
        if (this.headers.containsKey("Cookie") && this.headers.get("Cookie").startsWith("clientID=")) {
            String[] temp = this.headers.get("Cookie").trim().split("=");
            System.out.println("[HTTPRequest] cookie: " + temp[1]);
            cookie = temp[1];
        }
        return cookie;
    }

    public int validateHTTPRequest() {
        // debugging
//        System.out.println(this.requestLineParameters.length);
//        for (int i = 0; i < this.requestLineParameters.length; i++) {
//            System.out.println(this.requestLineParameters[i]);
//        }
        if (this.requestLineParameters.length != 3) {
            return 400; //bad request
        }
        if (!this.requestLineParameters[2].equals("HTTP/1.1")) {
            return 505; //HTTP Version Not Supported
        }
        /*
         * Browsers often automatically request a favicon (the small icon displayed in the browser tab)
         * from the server. This request is typically sent separately from the main page request. Thus,
         * we only want to do something if we have a valid URI that is not a favicon
         */
        if (!getURI().equals("/favicon.ico") && getURI().startsWith("/")) {
            if (getMethod().equals("GET")) { // we only handle the GET method
                return 200; // OK
            } else {
                return 405; // Method not allowed
            }
        } else {
            return 404;
        }
    }

    private String parseRequestLine(BufferedReader incoming) {
        String requestLine = new String();
        try {
            requestLine = incoming.readLine();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
//        System.out.println(requestLine); //debugging
        return requestLine;
    }

    private HashMap<String, String> parseHeaders(BufferedReader incoming) {
        HashMap<String, String> headers = new HashMap<>();
        String headerLine;
        /*
         * The !headerLine.isBlank() check ensures that the loop continues as long as there
         * are non-empty lines in the headers. This is necessary because HTTP headers have
         * an empty line at the end marking the end of the header section
         */
        try {
            while ((headerLine = incoming.readLine()) != null && !headerLine.isBlank()) {
//                System.out.println(headerLine); //debugging
                String[] header = headerLine.split(": ");
                headers.put(header[0], header[1]);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return headers;
    }

}
