package eu.timhuebert.jweb._core.controller;

import eu.timhuebert.jweb._core.request.Request;
import eu.timhuebert.jweb._core.response.Response;

import java.lang.reflect.InvocationTargetException;

public interface ControllerInterface {

    Response call(Request request) throws InvocationTargetException, IllegalAccessException;
}
