package io.github.butkoprojects.bots.preprocess.annotation.processor;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;

public interface AnnotationProcessor<T> {

    Class<T> getAnnotationClass();

    void process( T annotation, ControllerBuilder builder );

}
