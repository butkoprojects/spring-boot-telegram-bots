package io.github.butkoprojects.bots.preprocess.factory.annotation;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.util.annotation.MessageRequest;
import org.springframework.stereotype.Component;

@Component
public class MessageRequestAnnotationProcessor implements AnnotationProcessor<MessageRequest> {

    @Override
    public Class getAnnotationClass() {
        return MessageRequest.class;
    }

    @Override
    public void process( MessageRequest annotation, ControllerBuilder builder ) {
        builder.setPath( annotation.value() );
        builder.setControllerCouldBeExecuted( update -> update != null && update.hasMessage() && update.getMessage().hasText() );
    }
}
