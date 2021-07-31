package io.github.butkoprojects.bots.preprocess.annotation.processor;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;

public interface AnnotationProcessor<T> {

    void process( T annotation, ControllerBuilder builder );

}
