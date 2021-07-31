package io.github.butkoprojects.bots.preprocess.annotation.processor;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.preprocess.annotation.KeyBoardButton;
import io.github.butkoprojects.bots.preprocess.annotation.KeyBoardRow;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@Order( 5 )
public class KeyBoardRow_AnnotationProcessor implements AnnotationProcessor<KeyBoardRow> {

    @Override
    public void process( KeyBoardRow rowAnnotation,
                         ControllerBuilder builder ) {
        if ( rowAnnotation != null ) {
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            final List<KeyboardRow> keyboardRows = new ArrayList<>();
            KeyboardRow keyboardRow = new KeyboardRow();
            for ( KeyBoardButton keyBoardButton :rowAnnotation.value() ) {
                KeyboardButton button = new KeyboardButton();
                button.setText( keyBoardButton.value() );
                button.setRequestContact( keyBoardButton.requestContact() );
                button.setRequestLocation( keyBoardButton.requestLocation() );
                keyboardRow.add( button );
            }
            keyboardRows.add( keyboardRow );
            keyboardMarkup.setKeyboard( keyboardRows );
            builder.setKeyboardMarkup( keyboardMarkup );
        }
    }
}
