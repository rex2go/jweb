package eu.timhuebert.jweb.controller;

import eu.timhuebert.jweb.request.Request;
import eu.timhuebert.jweb.response.Response;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Controller implements ControllerInterface {

    public Response call(Request request) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = getClass().getMethods();

        for (Method method : methods) {
            if (!method.isAnnotationPresent(Route.class)) continue;
            Route route = method.getAnnotation(Route.class);

            if(!route.route().equals(request.getRoute())) continue;
            if(!route.method().equals(request.getMethod())) continue;

            return (Response) method.invoke(this, request);

        }

        return null;
    }


    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Route {
        String route();

        Request.Method method() default Request.Method.GET;

        Request.Method[] methods() default {};
    }
}
