package io.github.butkoprojects.bots.preprocess.factory.annotation;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.util.annotation.CallbackButton;
import io.github.butkoprojects.bots.util.annotation.CallbackButtonRow;
import io.github.butkoprojects.bots.util.annotation.CallbackKeyboard;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class CallbackKeyboardAnnotationProcessor implements AnnotationProcessor<CallbackKeyboard> {

    @Override
    public Class getAnnotationClass() {
        return CallbackKeyboard.class;
    }

    @Override
    public void process( CallbackKeyboard callbackKeyboard,
                         ControllerBuilder builder ) {
        if ( callbackKeyboard != null ) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyBoardRows = new ArrayList<>();

            for ( CallbackButtonRow row: callbackKeyboard.value() ) {
                List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
                for ( CallbackButton button: row.value() ) {
                    InlineKeyboardButton inlineButton = new InlineKeyboardButton();
                    inlineButton.setText( button.text() );
                    inlineButton.setCallbackData( button.call() + "|" + button.data() );
                    keyboardRow.add( inlineButton );
                }
                keyBoardRows.add( keyboardRow );
            }
            inlineKeyboardMarkup.setKeyboard( keyBoardRows );
            builder.setInlineKeyboardMarkup( inlineKeyboardMarkup );
        }
    }
}
