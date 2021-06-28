package io.github.butkoprojects.bots.preprocess.factory.annotation;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.util.annotation.CallbackRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Predicate;

@Component
public class CallbackRequest_AnnotationProcessor implements AnnotationProcessor<CallbackRequest> {

    @Override
    public Class getAnnotationClass() {
        return CallbackRequest.class;
    }

    @Override
    public void process( CallbackRequest annotation,
                         ControllerBuilder builder ) {

        Predicate<Update> updatePredicate = update ->
                update != null &&
                        update.hasCallbackQuery() &&
                        update.getCallbackQuery().getData() != null;

        builder.setPath( annotation.value() );
        builder.setControllerCouldBeExecuted( updatePredicate );
        builder.setCallbackConfiguration( annotation );
    }
}
