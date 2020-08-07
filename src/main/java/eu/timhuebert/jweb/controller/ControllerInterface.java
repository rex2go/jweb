package eu.timhuebert.jweb.controller;

import eu.timhuebert.jweb.request.Request;
import eu.timhuebert.jweb.response.Response;

import java.lang.reflect.InvocationTargetException;

public interface ControllerInterface {

    Response call(Request request) throws InvocationTargetException, IllegalAccessException;
}
