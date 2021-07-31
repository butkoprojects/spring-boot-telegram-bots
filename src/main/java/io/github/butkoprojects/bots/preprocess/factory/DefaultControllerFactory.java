package io.github.butkoprojects.bots.preprocess.factory;

import io.github.butkoprojects.bots.preprocess.container.BotMethodContainer;
import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.preprocess.annotation.processor.AnnotationProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

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
            Class<? extends Annotation> requiredAnnotation = getRequiredAnnotation( annotationProcessor );
            if ( method.isAnnotationPresent( requiredAnnotation ) ) {
                annotationProcessor.process(
                        method.getAnnotation( requiredAnnotation ),
                        newBuilder
                );
            }
        });

        container.addBotController( newBuilder.getPath(), newBuilder.getControllerType(), newBuilder.build() );
    }

    private Class<? extends Annotation> getRequiredAnnotation( final AnnotationProcessor<? extends Annotation> processor ) {
        Class<? extends Annotation> result = ( Class<? extends Annotation> )
                ((ParameterizedType) processor.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        return result;
    }
}
