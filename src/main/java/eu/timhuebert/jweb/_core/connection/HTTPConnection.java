package eu.timhuebert.jweb._core.connection;

import eu.timhuebert.jweb._core.JWeb;
import eu.timhuebert.jweb._core.exception.InternalServerErrorException;
import eu.timhuebert.jweb._core.request.Request;
import eu.timhuebert.jweb._core.response.Response;
import lombok.Getter;

import java.io.*;
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

            BufferedReader input = connection.getIn();
            Request request = jWeb.getRequestHandler().buildRequest(input);

            jWeb.getRequestHandler().handle(this, request);
        } catch (Exception exception) {
            if (exception instanceof InternalServerErrorException) {
                Response.StatusCode statusCode = Response.StatusCode.METHOD_NOT_ALLOWED;
                Response response = new Response(
                        statusCode,
                        statusCode.getStatusCode() + " " + statusCode.getMessage());

                PrintWriter out = connection.getOut();
                BufferedOutputStream dataOut = connection.getDataOut();

                response.print(out, dataOut);
                out.flush();

                try {
                    dataOut.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
