package eu.timhuebert.jweb.connection;

import eu.timhuebert.jweb.JWeb;
import eu.timhuebert.jweb.request.Request;
import lombok.Getter;

import java.io.IOException;
import java.net.Socket;

public class HTTPConnection implements Runnable {

    @Getter
    private static final String version = "HTTP/1.1";

    private JWeb jWeb;

    private Socket socket;

    @Getter
    private Connection connection;

    public HTTPConnection(JWeb jWeb, Socket socket) {
        this.jWeb = jWeb;
        this.socket = socket;
    }

    public void run() {
        try {
            connection = new Connection(socket);

            String input = connection.getIn().readLine();
            Request request = jWeb.getRequestHandler().buildRequest(input);

            jWeb.getRequestHandler().handle(this, request);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                assert connection != null;
                connection.close();

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
