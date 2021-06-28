package io.github.butkoprojects.bots.preprocess.factory.annotation;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.util.annotation.CallbackButton;
import io.github.butkoprojects.bots.util.annotation.CallbackButtonRow;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class CallbackButtonRow_AnnotationProcessor implements AnnotationProcessor<CallbackButtonRow> {

    @Override
    public Class getAnnotationClass() {
        return CallbackButtonRow.class;
    }

    @Override
    public void process( CallbackButtonRow callbackButtonRow,
                         ControllerBuilder builder ) {
        if ( callbackButtonRow != null ) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyBoardRows = new ArrayList<>();

            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
            for ( CallbackButton button: callbackButtonRow.value() ) {
                InlineKeyboardButton inlineButton = new InlineKeyboardButton();
                inlineButton.setText( button.text() );
                inlineButton.setCallbackData( button.call() + "|" + button.data() );
                keyboardRow.add( inlineButton );
            }

            keyBoardRows.add( keyboardRow );
            inlineKeyboardMarkup.setKeyboard( keyBoardRows );
            builder.setInlineKeyboardMarkup( inlineKeyboardMarkup );
        }
    }
}
