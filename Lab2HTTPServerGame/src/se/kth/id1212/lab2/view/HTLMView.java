package se.kth.id1212.lab2.view;

/**
 * Predefined HTLM strings that will be used to construct the browser view
 */
public final class HTLMView {

    private HTLMView() {
    }

    public static String HTML_TEMPLATE =
            "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "    <head>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "        <title>Number Guess Game</title>\n" +
                    "    </head>\n" +
                    "    <body> \n" +
                    "        %s\n" +
                    "        <form name=\"guessform\">\n" +
                    "            <input type=\"text\" name=\"guess\">\n" +
                    "            <input type=\"submit\" value=\"Guess\">\n" +
                    "        </form>\n" +
                    "    </body>\n" +
                    "</html>";

    public static String BODY_INITIAL =
            "<div>Welcome to the Number Guess Game. Guess a number between 1 and 100.</div>";

    public static String BODY_CORRECT =
            "<div>You made it!!!</div>";

    public static String BODY_TOO_LOW =
            "<div>Nope, guess higher!<br>You have made %d guess(es).</div>";

    public static String BODY_TOO_HIGH =
            "<div>Nope, guess lower!<br>You have made %d guess(es).</div>";

    public static String BODY_WRONG_INPUT =
            "<div>You have to guess on an integer between 1 and 100!<br>You have made %d guess(es).</div>";

    public static String HTML_ERROR_TEMPLATE =
            "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "    <head>\n" +
                    "        <meta charset=\"UTF-8\">\n" +
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "        <title>%s ERROR</title>\n" +
                    "    </head>\n" +
                    "    <body> \n" +
                    "        <div>%s ERROR</div>\n" +
                    "    </body>\n" +
                    "</html>";
}
