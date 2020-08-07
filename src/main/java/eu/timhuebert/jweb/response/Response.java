package eu.timhuebert.jweb.response;

import eu.timhuebert.jweb.connection.HTTPConnection;
import lombok.Data;
import lombok.Getter;

import java.io.PrintWriter;
import java.util.Map;

@Data
public class Response {

    private StatusCode statusCode;

    private Map<String, String> headers;

    private String body; // TODO

    public Response(StatusCode statusCode, Map<String, String> headers, String body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public boolean print(PrintWriter out) {
        out.println(HTTPConnection.getVersion() + " " + statusCode.getStatusCode() + " " + statusCode.getMessage());
        // TODO headers

        out.println();

        out.println(body);
        return true;
    }

    public enum StatusCode {
        OK(200),
        NOT_FOUND(404),
        METHOD_NOT_ALLOWED(405),
        INTERNAL_SERVER_ERROR(500),
        HTTP_VERSION_NOT_SUPPORTED(505);

        @Getter
        private int statusCode;

        StatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getMessage() {
            return this.name().replace("_", " ").toUpperCase();
        }
    }
}
