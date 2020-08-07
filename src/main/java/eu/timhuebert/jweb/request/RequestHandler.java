package eu.timhuebert.jweb.request;

import eu.timhuebert.jweb.JWeb;
import eu.timhuebert.jweb.connection.HTTPConnection;
import eu.timhuebert.jweb.controller.Controller;
import eu.timhuebert.jweb.response.Response;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

public class RequestHandler {

    private JWeb jWeb;

    public RequestHandler(JWeb jWeb) {
        this.jWeb = jWeb;
    }

    public boolean handle(HTTPConnection httpConnection, Request request) {
        if (!validate(httpConnection, request)) return false;

        // TODO middleware

        Response response = null;
        for (Controller controller : jWeb.getControllerContainer().getContainer()) {
            try {
                Response tempResponse = controller.call(request);

                if (tempResponse != null) response = tempResponse;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        if (response == null) {
            Response.StatusCode statusCode = Response.StatusCode.NOT_FOUND;
            response = new Response(
                    statusCode,
                    new HashMap<String, String>(),
                    statusCode.getStatusCode() + " " + statusCode.getMessage()
            );
        }

        PrintWriter out = httpConnection.getConnection().getOut();
        response.print(out);
        out.flush();

        return true;
    }

    public boolean validate(HTTPConnection httpConnection, Request request) {
        Response errorResponse = null;

        if (!request.getVersion().equals(HTTPConnection.getVersion())) {
            Response.StatusCode statusCode = Response.StatusCode.HTTP_VERSION_NOT_SUPPORTED;
            errorResponse = new Response(
                    statusCode,
                    new HashMap<String, String>(),
                    statusCode.getStatusCode() + " " + statusCode.getMessage()
            );
        } else if (request.getMethod() == Request.Method.UNKNOWN) {
            Response.StatusCode statusCode = Response.StatusCode.METHOD_NOT_ALLOWED;
            errorResponse = new Response(
                    statusCode,
                    new HashMap<String, String>(),
                    statusCode.getStatusCode() + " " + statusCode.getMessage()
            );
        }

        if (errorResponse != null) {
            PrintWriter out = httpConnection.getConnection().getOut();
            errorResponse.print(out);
            out.flush();
            return false;
        }

        return true;
    }

    public Request buildRequest(String str) {
        StringTokenizer parse = new StringTokenizer(str);
        String methodStr = parse.nextToken().toUpperCase().toUpperCase();
        String route = parse.nextToken().toLowerCase();
        String version = parse.nextToken().toLowerCase().toUpperCase();
        Request.Method method;

        try {
            method = Request.Method.valueOf(methodStr);
        } catch (Exception exception) {
            method = Request.Method.UNKNOWN;
        }

        return new Request(method, route, version);
    }
}
