package eu.timhuebert.jweb.request;

import lombok.Data;

@Data
public class Request {

    private Method method;
    private String route;
    private String version;

    public Request(Method method, String route, String version) {
        this.method = method;
        this.route = route;
        this.version = version;
    }

    public enum Method {
        GET, POST, UNKNOWN
    }
}