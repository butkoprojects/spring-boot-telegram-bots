package io.github.butkoprojects.bots.preprocess.annotation.processor;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.preprocess.annotation.CallbackButton;
import io.github.butkoprojects.bots.preprocess.annotation.CallbackButtonRow;
import io.github.butkoprojects.bots.preprocess.annotation.CallbackKeyboard;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@Order( 1 )
public class CallbackKeyboard_AnnotationProcessor implements AnnotationProcessor<CallbackKeyboard> {

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
                    inlineButton.setText( button.name() );
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
