package io.github.butkoprojects.bots.preprocess.annotation.processor;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.preprocess.annotation.MessageRequest;
import io.github.butkoprojects.bots.preprocess.controller.type.BotControllerTypeEnum;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
@Order( 7 )
public class MessageRequest_AnnotationProcessor
        extends BaseAnnotationProcessor
        implements AnnotationProcessor<MessageRequest> {

    @Override
    public void process( MessageRequest annotation, ControllerBuilder builder ) {
        builder.setPath( annotation.value() );
        builder.setControllerCouldBeExecuted( BotControllerTypeEnum.MESSAGE.updatePredicate );
        builder.setControllerType( BotControllerTypeEnum.MESSAGE.type );

        Consumer<Update> updateConsumer = update -> {
            if (isReturnTypeIsString(builder.getMethod())) {
                processNonBotApiReturnType(builder, update);
            } else if (returnTypeIsList(builder.getMethod())) {
                processList(builder, update);
            } else {
                processSingle(builder, update);
            }
        };
        builder.addProcessConsumer( updateConsumer );
    }

    private void processNonBotApiReturnType(ControllerBuilder builder, Update update ) {
            ReplyKeyboard keyboard =
                    builder.getKeyboardMarkup() != null ? builder.getKeyboardMarkup()
                            : builder.getInlineKeyboardMarkup() != null ? builder.getInlineKeyboardMarkup()
                            : null;
            Object returnObject = processMethodInvocation( builder, update ).getResultObject();
            if ( returnObject != null ) {
                if ( List.class.equals( returnObject.getClass() ) ) {
                    ( ( List ) returnObject ).forEach( obj ->
                            builder.getResultList().add( SendMessage.builder()
                                    .text( String.valueOf( obj ) )
                                    .replyMarkup( keyboard )
                                    .chatId( String.valueOf( update.getMessage().getChatId() ) )
                                    .build() )
                    );
                } else {
                    builder.getResultList().add( SendMessage.builder()
                            .text( String.valueOf( returnObject ) )
                            .replyMarkup( keyboard )
                            .chatId( String.valueOf( update.getMessage().getChatId() ) )
                            .build() );
                }
            }
    }
}
