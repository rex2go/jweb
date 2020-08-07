package eu.timhuebert.jweb._core.request;

import eu.timhuebert.jweb._core.JWeb;
import eu.timhuebert.jweb._core.connection.HTTPConnection;
import eu.timhuebert.jweb._core.controller.ControllerInterface;
import eu.timhuebert.jweb._core.exception.InternalServerErrorException;
import eu.timhuebert.jweb._core.response.Response;

import java.io.PrintWriter;
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
        for (ControllerInterface controller : jWeb.getControllerContainer().getContainer()) {
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
            errorResponse.print(out);
            out.flush();
            return false;
        }

        return true;
    }

    public Request buildRequest(String str) throws InternalServerErrorException {
        if (str == null) throw new InternalServerErrorException();

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
    }
}
