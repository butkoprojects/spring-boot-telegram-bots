package io.github.butkoprojects.bots.preprocess.factory;

import io.github.butkoprojects.bots.preprocess.container.BotMethodContainer;
import io.github.butkoprojects.bots.preprocess.controller.BotApiMethodController;
import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.preprocess.factory.annotation.AnnotationProcessor;
import io.github.butkoprojects.bots.util.annotation.*;
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

    public void generateController2( Object bean, Method method ) {
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

        container.addBotController( newBuilder.getPath(), newBuilder.build() );
    }

    @Override
    public void generateController( Object bean, Method method ) {
        generateController2( bean, method );
//        ControllerBuilder newBuilder = builder.instance();
//        BotController botController = bean.getClass().getAnnotation( BotController.class );
//
//        if ( method.isAnnotationPresent( MessageRequest.class ) ) {
//            createMessageController( bean, method, botController );
//        }
//        if ( method.isAnnotationPresent( CallbackRequest.class ) ) {
//            createCallbackController( bean, method, botController );
//        }
    }

    private void createMessageController( Object bean, Method method, BotController botController ) {
        MessageRequest messageRequest = method.getAnnotation( MessageRequest.class );
        Keyboard keyboard = method.getAnnotation( Keyboard.class );
        KeyBoardRow keyBoardRow = method.getAnnotation( KeyBoardRow.class );
        KeyBoardButton keyBoardButton = method.getAnnotation( KeyBoardButton.class );

        CallbackKeyboard callbackKeyboard = method.getAnnotation( CallbackKeyboard.class );
        CallbackButtonRow callbackButtonRow = method.getAnnotation( CallbackButtonRow.class );
        CallbackButton callbackButton = method.getAnnotation( CallbackButton.class );

        String path = ( botController.value().length != 0 ? botController.value()[0] : "" )
                + messageRequest.value();

        Predicate<Update> updatePredicate = update -> update != null && update.hasMessage() && update.getMessage().hasText();

        BotApiMethodController.BotApiMethodControllerBuilder builder = BotApiMethodController.builder()
                .setWorkingBean( bean )
                .setMethod( method )
                .setPredicate( updatePredicate )
                .messageRequest()
                .setKeyBoard( keyboard )
                .setKeyBoardRow( keyBoardRow )
                .setKeyBoardButton( keyBoardButton )
                .setCallbackKeyboard( callbackKeyboard )
                .setCallbackButtonRow( callbackButtonRow )
                .setCallbackButton( callbackButton );

        container.addBotController( path, builder.build() );
    }

    private void createCallbackController( Object bean, Method method, BotController botController ) {
        CallbackRequest callbackRequest = method.getAnnotation( CallbackRequest.class );

        String path = ( botController.value().length != 0 ? botController.value()[0] : "" )
                + callbackRequest.value();

        Predicate<Update> updatePredicate = update ->
                update != null &&
                        update.hasCallbackQuery() &&
                        update.getCallbackQuery().getData() != null;

        BotApiMethodController controller = BotApiMethodController.builder()
                .setWorkingBean( bean )
                .setMethod( method )
                .setPredicate( updatePredicate )
                .callbackRequest( callbackRequest )
                .build();
        container.addBotController( path, controller );
    }
}
