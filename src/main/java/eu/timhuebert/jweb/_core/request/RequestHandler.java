package eu.timhuebert.jweb._core.request;

import eu.timhuebert.jweb._core.JWeb;
import eu.timhuebert.jweb._core.connection.HTTPConnection;
import eu.timhuebert.jweb._core.controller.ControllerInterface;
import eu.timhuebert.jweb._core.exception.InternalServerErrorException;
import eu.timhuebert.jweb._core.response.Response;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

public class RequestHandler {

    public boolean handle(HTTPConnection httpConnection, Request request) {
        if (!validate(httpConnection, request)) return false;

        // TODO middleware

        Response response = null;
        for (ControllerInterface controller : JWeb.getInstance().getControllerContainer().getContainer()) {
            try {
                Response tempResponse = controller.call(request);

                if (tempResponse != null) response = tempResponse;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        if (response == null) {
            response = JWeb.getInstance().getResourceManager().load(request.getRoute().substring(1));

            if (response == null) {
                Response.StatusCode statusCode = Response.StatusCode.NOT_FOUND;
                response = new Response(
                        statusCode,
                        statusCode.getStatusCode() + " " + statusCode.getMessage()
                );
            }
        }

        PrintWriter out = httpConnection.getConnection().getOut();
        BufferedOutputStream dataOut = httpConnection.getConnection().getDataOut();

        response.print(out, dataOut);
        out.flush();

        try {
            dataOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean validate(HTTPConnection httpConnection, Request request) {
        Response errorResponse = null;

        if (!request.getVersion().equals(HTTPConnection.getVersion())) {
            Response.StatusCode statusCode = Response.StatusCode.HTTP_VERSION_NOT_SUPPORTED;
            errorResponse = new Response(
                    statusCode,
                    statusCode.getStatusCode() + " " + statusCode.getMessage()
            );
        } else if (request.getMethod() == Request.Method.UNKNOWN) {
            Response.StatusCode statusCode = Response.StatusCode.METHOD_NOT_ALLOWED;
            errorResponse = new Response(
                    statusCode,
                    statusCode.getStatusCode() + " " + statusCode.getMessage()
            );
        }

        if (errorResponse != null) {
            PrintWriter out = httpConnection.getConnection().getOut();
            BufferedOutputStream dataOut = httpConnection.getConnection().getDataOut();

            errorResponse.print(out, dataOut);
            out.flush();

            try {
                dataOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        return true;
    }

    public Request buildRequest(BufferedReader inputStream) throws InternalServerErrorException {
        if (inputStream == null) throw new InternalServerErrorException();

        try {
            String str = inputStream.readLine();
            StringTokenizer parse = new StringTokenizer(str);
            String methodStr = parse.nextToken().toUpperCase().toUpperCase();
            String route = parse.nextToken();
            String version = parse.nextToken().toLowerCase().toUpperCase();
            Request.Method method;

            String[] routeParts = route.split("\\?");
            route = routeParts[0].toLowerCase();

            try {
                method = Request.Method.valueOf(methodStr);
            } catch (Exception exception) {
                method = Request.Method.UNKNOWN;
            }

            Request request = new Request(method, route, version);
            boolean body = false;
            HashMap<String, String> headers = new HashMap<String, String>();

            while(inputStream.ready()) {
                str = inputStream.readLine();

                if(str.isEmpty()) {
                    body = true;
                    continue;
                }

                if(!body) {
                    System.out.println("header " + str);
                } else {
                    System.out.println("body " + str);
                }
            }

            if (routeParts.length > 1) {
                for (String parameters : routeParts[1].split("&")) {
                    String[] parameterParts = parameters.split("=");
                    String key = parameterParts[0];
                    String value = "";

                    if (parameterParts.length > 1) {
                        value = parameterParts[1];
                    }

                    request.getParameters().put(key, value);
                }
            }

            return request;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        }
    }
}
