package io.github.butkoprojects.bots.preprocess.factory.annotation;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.util.annotation.KeyBoardButton;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@Order( 6 )
public class KeyBoardButton_AnnotationProcessor implements AnnotationProcessor<KeyBoardButton> {

    @Override
    public Class<KeyBoardButton> getAnnotationClass() {
        return KeyBoardButton.class;
    }

    @Override
    public void process( KeyBoardButton keyBoardButton,
                         ControllerBuilder builder ) {
        if ( keyBoardButton != null ) {
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            keyboardMarkup.setResizeKeyboard( true );
            final List<KeyboardRow> keyboardRows = new ArrayList<>();
            KeyboardRow keyboardRow = new KeyboardRow();

            KeyboardButton button = new KeyboardButton();
            button.setText( keyBoardButton.value() );
            button.setRequestContact( keyBoardButton.requestContact() );
            button.setRequestLocation( keyBoardButton.requestLocation() );
            keyboardRow.add( button );

            keyboardRows.add( keyboardRow );
            keyboardMarkup.setKeyboard( keyboardRows );
            builder.setKeyboardMarkup( keyboardMarkup );
        }
    }
}
