package eu.timhuebert.jweb.core.controller;

import eu.timhuebert.jweb.core.request.Request;
import eu.timhuebert.jweb.core.response.Response;

import java.lang.reflect.InvocationTargetException;

public interface ControllerInterface {

    Response call(Request request) throws InvocationTargetException, IllegalAccessException;
}
