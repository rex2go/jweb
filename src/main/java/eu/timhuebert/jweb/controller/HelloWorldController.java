package eu.timhuebert.jweb.controller;

import eu.timhuebert.jweb._core.controller.Controller;
import eu.timhuebert.jweb._core.request.Request;
import eu.timhuebert.jweb._core.response.Response;

public class HelloWorldController extends Controller {

    @Route(route = "/")
    public Response test(Request request) {
        return new Response(Response.StatusCode.OK, "Hello World!");
    }
}
