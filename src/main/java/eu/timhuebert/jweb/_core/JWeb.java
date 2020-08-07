package eu.timhuebert.jweb._core;

import eu.timhuebert.jweb._core.connection.HTTPConnection;
import eu.timhuebert.jweb._core.container.ControllerContainer;
import eu.timhuebert.jweb._core.request.RequestHandler;
import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;

public class JWeb {

    private static boolean exit = false;

    public static void main(String[] args) {
        JWeb jWeb = new JWeb();
        jWeb.start();
    }

    public static void exit() {
        exit = true;
    }

    @Getter
    private ControllerContainer controllerContainer;

    @Getter
    private RequestHandler requestHandler;

    private JWeb() {
        init();
    }

    private void init() {
        controllerContainer = new ControllerContainer();

        requestHandler = new RequestHandler(this);
    }

    private void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(2020);

            while (!exit) {
                HTTPConnection connection = new HTTPConnection(this, serverSocket.accept());

                new Thread(connection).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
