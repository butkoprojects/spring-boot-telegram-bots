package io.github.butkoprojects.bots.preprocess.factory.annotation;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.preprocess.annotation.CallbackButton;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@Order( 3 )
public class CallbackButton_AnnotationProcessor implements AnnotationProcessor<CallbackButton> {

    @Override
    public Class<CallbackButton> getAnnotationClass() {
        return CallbackButton.class;
    }

    @Override
    public void process( CallbackButton callbackButton,
                         ControllerBuilder builder ) {
        if ( callbackButton != null ) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyBoardRows = new ArrayList<>();
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

            InlineKeyboardButton inlineButton = new InlineKeyboardButton();
            inlineButton.setText( callbackButton.text() );
            inlineButton.setCallbackData( callbackButton.call() + "|" + callbackButton.data() );
            keyboardRow.add( inlineButton );

            keyBoardRows.add( keyboardRow );
            inlineKeyboardMarkup.setKeyboard( keyBoardRows );
            builder.setInlineKeyboardMarkup( inlineKeyboardMarkup );
        }
    }
}
