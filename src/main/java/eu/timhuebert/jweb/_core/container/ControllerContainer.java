package eu.timhuebert.jweb._core.container;

import eu.timhuebert.jweb._core.controller.ControllerInterface;
import lombok.Getter;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Set;

public class ControllerContainer {

    @Getter
    private ArrayList<ControllerInterface> container = new ArrayList<ControllerInterface>();

    public ControllerContainer() {
        Reflections reflections = new Reflections("eu.timhuebert.jweb.controller");

        Set<Class<? extends ControllerInterface>> allClasses = reflections.getSubTypesOf(ControllerInterface.class);

        cl:
        for (Class clazz : allClasses) {
            for (ControllerInterface controller : container) {
                if (controller.getClass().getName().equals(clazz.getName())) continue cl;
            }

            try {
                container.add((ControllerInterface) clazz.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
