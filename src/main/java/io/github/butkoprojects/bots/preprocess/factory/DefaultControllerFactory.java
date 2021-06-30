package io.github.butkoprojects.bots.preprocess.factory;

import io.github.butkoprojects.bots.preprocess.container.BotMethodContainer;
import io.github.butkoprojects.bots.preprocess.controller.BotApiMethodController;
import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.preprocess.factory.annotation.AnnotationProcessor;
import io.github.butkoprojects.bots.preprocess.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Predicate;

@Component
public class DefaultControllerFactory implements ControllerFactory {

    @Autowired
    private BotMethodContainer container;

    @Autowired
    private ControllerBuilder builder;

    @Autowired
    private List<AnnotationProcessor> processors;

    @Override
    public void generateController( Object bean, Method method ) {
        ControllerBuilder newBuilder = builder.instance();
        newBuilder.setMethod( method ).setBean( bean );

        processors.forEach( annotationProcessor -> {
            if ( method.isAnnotationPresent( annotationProcessor.getAnnotationClass() ) ) {
                annotationProcessor.process(
                        method.getAnnotation( annotationProcessor.getAnnotationClass() ),
                        newBuilder
                );
            }
        });

        container.addBotController( newBuilder.getPath(), newBuilder.getControllerType(), newBuilder.build() );
    }
}
