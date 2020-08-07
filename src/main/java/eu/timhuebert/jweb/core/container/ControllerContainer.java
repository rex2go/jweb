package eu.timhuebert.jweb.core.container;

import eu.timhuebert.jweb.core.controller.Controller;
import lombok.Getter;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Set;

public class ControllerContainer {

    @Getter
    private ArrayList<Controller> container = new ArrayList<Controller>();

    public ControllerContainer() {
        Reflections reflections = new Reflections("eu.timhuebert.jweb.controller");

        Set<Class<? extends Controller>> allClasses = reflections.getSubTypesOf(Controller.class);

        cl:
        for (Class clazz : allClasses) {
            for (Controller controller : container) {
                if (controller.getClass().getName().equals(clazz.getName())) continue cl;
            }

            try {
                container.add((Controller) clazz.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
