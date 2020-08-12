package eu.timhuebert.jweb._core;

import eu.timhuebert.jweb._core.connection.HTTPConnection;
import eu.timhuebert.jweb._core.controller.ControllerContainer;
import eu.timhuebert.jweb._core.request.RequestHandler;
import eu.timhuebert.jweb._core.resource.ResourceManager;
import eu.timhuebert.jweb._core.resource.TemplateManager;
import lombok.Getter;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.ServerSocket;

public class JWeb {

    @Getter
    private static JWeb instance;

    private boolean exit = false;

    @Getter
    private ControllerContainer controllerContainer;

    @Getter
    private RequestHandler requestHandler;

    @Getter
    private TemplateManager templateManager;

    @Getter
    private ResourceManager resourceManager;

    public JWeb() {
        init();
    }

    private void init() {
        instance = this;

        controllerContainer = new ControllerContainer();

        requestHandler = new RequestHandler();

        templateManager = new TemplateManager();
        resourceManager = new ResourceManager();
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
