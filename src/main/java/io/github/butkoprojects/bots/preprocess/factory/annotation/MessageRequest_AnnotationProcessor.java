package io.github.butkoprojects.bots.preprocess.factory.annotation;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.preprocess.annotation.MessageRequest;
import io.github.butkoprojects.bots.preprocess.controller.type.BotControllerTypeEnum;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
@Order( 7 )
public class MessageRequest_AnnotationProcessor
        extends BaseAnnotationProcessor
        implements AnnotationProcessor<MessageRequest> {

    @Override
    public Class<MessageRequest> getAnnotationClass() {
        return MessageRequest.class;
    }

    @Override
    public void process( MessageRequest annotation, ControllerBuilder builder ) {
        builder.setPath( annotation.value() );
        builder.setControllerCouldBeExecuted( BotControllerTypeEnum.MESSAGE.updatePredicate );
        builder.setControllerType( BotControllerTypeEnum.MESSAGE.type );

        Function<Update, List<BotApiMethod>> processFunction =
                isReturnTypeIsString( builder.getMethod() ) ?
                    processNonBotApiReturnType( builder ) :
                    returnTypeIsList( builder.getMethod() ) ?
                        processList( builder.getMethod(), builder.getBean() ) :
                        processSingle( builder );
        builder.setProcessFunction( processFunction );
    }

    private Function<Update, List<BotApiMethod>> processNonBotApiReturnType( ControllerBuilder builder ) {
        return update -> {
            ReplyKeyboard keyboard =
                    builder.getKeyboardMarkup() != null ? builder.getKeyboardMarkup()
                            : builder.getInlineKeyboardMarkup() != null ? builder.getInlineKeyboardMarkup()
                            : null;
            Object returnObject;
            try {
                returnObject = builder.getMethod().invoke( builder.getBean(), update );
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(e);
            }
            List<BotApiMethod> resultList = new ArrayList<>();
            if ( returnObject != null ) {
                if ( List.class.equals( returnObject.getClass() ) ) {
                    ( ( List ) returnObject ).forEach( obj ->
                            resultList.add( SendMessage.builder()
                                    .text( String.valueOf( obj ) )
                                    .replyMarkup( keyboard )
                                    .chatId( String.valueOf( update.getMessage().getChatId() ) )
                                    .build() )
                    );
                } else {
                    resultList.add( SendMessage.builder()
                            .text( String.valueOf( returnObject ) )
                            .replyMarkup( keyboard )
                            .chatId( String.valueOf( update.getMessage().getChatId() ) )
                            .build() );
                }
            }
            return resultList;
        };
    }
}
