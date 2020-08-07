package eu.timhuebert.jweb.core.connection;

import eu.timhuebert.jweb.core.JWeb;
import eu.timhuebert.jweb.core.exception.InternalServerErrorException;
import eu.timhuebert.jweb.core.request.Request;
import eu.timhuebert.jweb.core.response.Response;
import lombok.Getter;

import java.io.IOException;
import java.io.PrintWriter;
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
            if (exception instanceof InternalServerErrorException) {
                Response.StatusCode statusCode = Response.StatusCode.METHOD_NOT_ALLOWED;
                Response response = new Response(
                        statusCode,
                        statusCode.getStatusCode() + " " + statusCode.getMessage());
                PrintWriter out = connection.getOut();
                response.print(out);
                out.flush();
                // TODO message
                return;
            }

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
