package eu.timhuebert.jweb._core.request;

import lombok.Data;

import java.util.HashMap;

@Data
public class Request {

    private Method method;
    private String route;
    private String version;

    private HashMap<String, String> parameters = new HashMap<String, String>();

    public Request(Method method, String route, String version) {
        this.method = method;
        this.route = route;
        this.version = version;
    }

    public enum Method {
        GET, POST, UNKNOWN
    }
}