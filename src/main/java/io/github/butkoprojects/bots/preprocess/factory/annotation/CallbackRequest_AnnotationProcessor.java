package io.github.butkoprojects.bots.preprocess.factory.annotation;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.util.annotation.CallbackRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class CallbackRequest_AnnotationProcessor
        extends BaseAnnotationProcessor
        implements AnnotationProcessor<CallbackRequest> {

    @Override
    public Class<CallbackRequest> getAnnotationClass() {
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
        Function<Update, List<BotApiMethod>> processFunction =
                isReturnTypeIsString( builder.getMethod() ) ?
                    processCallbackWithStringReturnType( builder ) :
                    returnTypeIsList( builder.getMethod() ) ?
                        processList( builder.getMethod(), builder.getBean() ) :
                        processSingle( builder );
        builder.setProcessFunction( processFunction );
    }

    private Function<Update, List<BotApiMethod>> processCallbackWithStringReturnType( ControllerBuilder builder ) {
        return update -> {
            Object returnObject;
            try {
                returnObject = builder.getMethod().invoke( builder.getBean(), update );
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(e);
            }
            List<BotApiMethod> resultList = new ArrayList<>();
            if ( returnObject != null ) {
                resultList.add(
                        AnswerCallbackQuery.builder()
                                .callbackQueryId( update.getCallbackQuery().getId() )
                                .text( String.valueOf( returnObject ) )
                                .showAlert( builder.getCallbackConfiguration().showAlert() )
                                .cacheTime( builder.getCallbackConfiguration().cacheTime() )
                                .build()
                );
            }
            return resultList;
        };
    }
}
