package io.github.butkoprojects.bots.preprocess.factory.annotation;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.util.annotation.KeyBoardButton;
import io.github.butkoprojects.bots.util.annotation.KeyBoardRow;
import io.github.butkoprojects.bots.util.annotation.Keyboard;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@Order( 4 )
public class Keyboard_AnnotationProcessor implements AnnotationProcessor<Keyboard> {

    @Override
    public Class<Keyboard> getAnnotationClass() {
        return Keyboard.class;
    }

    @Override
    public void process( Keyboard keyBoard,
                         ControllerBuilder builder ) {
        if ( keyBoard != null ) {
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            final List<KeyboardRow> keyboardRows = new ArrayList<>();
            for ( KeyBoardRow keyboardRow : keyBoard.value() ) {
                KeyboardRow row = new KeyboardRow();
                for ( KeyBoardButton keyBoardButton : keyboardRow.value() ) {
                    KeyboardButton button = new KeyboardButton();
                    button.setText( keyBoardButton.value() );
                    button.setRequestContact( keyBoardButton.requestContact() );
                    button.setRequestLocation( keyBoardButton.requestLocation() );
                    row.add( button );
                }
                keyboardRows.add( row );
            }
            keyboardMarkup.setKeyboard( keyboardRows );
            builder.setKeyboardMarkup( keyboardMarkup );
        }
    }
}
