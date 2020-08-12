package eu.timhuebert.jweb.controller;

import eu.timhuebert.jweb._core.JWeb;
import eu.timhuebert.jweb._core.controller.Controller;
import eu.timhuebert.jweb._core.request.Request;
import eu.timhuebert.jweb._core.response.Response;

public class LoginController extends Controller {

    @Route(route = "/login", method = Request.Method.GET)
    public Response login(Request request) {
        return JWeb.getInstance().getTemplateManager().load("login");
    }

    @Route(route = "/login", method = Request.Method.POST)
    public Response processLogin(Request request) {
        return null;
    }
}
