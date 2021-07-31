package io.github.butkoprojects.bots.preprocess.annotation.processor;

import io.github.butkoprojects.bots.preprocess.annotation.EditLastMessage;
import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Consumer;

@Component
@Order( 1 )
public class EditLastMessage_AnnotationProcessor implements AnnotationProcessor<EditLastMessage> {

    @Override
    public void process( EditLastMessage annotation, ControllerBuilder builder ) {
        Consumer<Update> updateConsumer = update -> {
            builder.getResultList().add(
                    DeleteMessage.builder()
                        .chatId( String.valueOf( update.getCallbackQuery().getMessage().getChatId() ) )
                        .messageId( update.getCallbackQuery().getMessage().getMessageId() )
                        .build()
            );
        };
        builder.addProcessConsumer( updateConsumer );
    }
}
