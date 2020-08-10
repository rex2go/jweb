package eu.timhuebert.jweb._core;

import eu.timhuebert.jweb._core.connection.HTTPConnection;
import eu.timhuebert.jweb._core.container.ControllerContainer;
import eu.timhuebert.jweb._core.request.RequestHandler;
import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;

public class JWeb {

    private boolean exit = false;

    @Getter
    private ControllerContainer controllerContainer;

    @Getter
    private RequestHandler requestHandler;

    public JWeb() {
        init();
    }

    private void init() {
        controllerContainer = new ControllerContainer();

        requestHandler = new RequestHandler(this);
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(2020);

            while (!exit) {
                HTTPConnection connection = new HTTPConnection(this, serverSocket.accept());
                Thread thread = new Thread(connection);
                
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        exit = true;
    }
}
