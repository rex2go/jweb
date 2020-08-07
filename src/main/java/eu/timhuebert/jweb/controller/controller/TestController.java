package eu.timhuebert.jweb.controller.controller;

import eu.timhuebert.jweb.controller.Controller;
import eu.timhuebert.jweb.request.Request;
import eu.timhuebert.jweb.response.Response;

import java.util.HashMap;

public class TestController extends Controller {

    @Route(route = "/test")
    public Response test(Request request) {
        return new Response(Response.StatusCode.OK, new HashMap<String, String>(), "test");
    }
}
