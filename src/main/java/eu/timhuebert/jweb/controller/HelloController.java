package eu.timhuebert.jweb.controller;

import eu.timhuebert.jweb._core.controller.Controller;
import eu.timhuebert.jweb._core.request.Request;
import eu.timhuebert.jweb._core.response.Response;

public class HelloController extends Controller {

    @Route(route = "/hello")
    public Response test(Request request) {
        return new Response(Response.StatusCode.OK, "Hello " + request.getParameters().get("name"));
    }
}
