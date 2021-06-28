package io.github.butkoprojects.bots.preprocess.factory.annotation;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;

public interface AnnotationProcessor<T> {

    Class<T> getAnnotationClass();

    void process( T annotation, ControllerBuilder builder );

}
