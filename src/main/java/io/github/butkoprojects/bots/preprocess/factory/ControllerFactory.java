package io.github.butkoprojects.bots.preprocess.factory;

import java.lang.reflect.Method;

public interface ControllerFactory {

    void generateController( Object bean, Method method );

}
