package io.github.butkoprojects.example;

import io.github.butkoprojects.bots.api.BotRequestMethod;
import io.github.butkoprojects.bots.api.annotation.BotController;
import io.github.butkoprojects.bots.api.annotation.BotRequestMapping;
import io.github.butkoprojects.bots.api.annotation.CallbackConfiguration;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@BotController
public class MyController {

    @BotRequestMapping( value = "/start", method = BotRequestMethod.MSG )
    public SendMessage start( final Update update )
    {
        return SendMessage.builder()
                .chatId(String.valueOf(update.getMessage().getChatId()))
                .text("Start message.")
                .build();
    }

    @BotRequestMapping( "/home" )
    public String home( final Update update )
    {
        return "Home";
    }

    @BotRequestMapping( "/buttons" )
    public SendMessage buttons( final Update update )
    {
        KeyboardButton button = new KeyboardButton();
        button.setText("button1");

        KeyboardRow row = new KeyboardRow();
        row.add(button);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(row);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard( keyboardRows );

        return SendMessage.builder()
                .replyMarkup( markup )
                .chatId(String.valueOf(update.getMessage().getChatId()))
                .text("Start message.")
                .build();
    }

    @BotRequestMapping( "/inline" )
    public SendMessage inline( final Update update )
    {
        InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyBoardRows = new ArrayList<>();

        InlineKeyboardButton moreButton = new InlineKeyboardButton();
        moreButton.setText( "text" );
        moreButton.setCallbackData( "more| some data" );

        InlineKeyboardButton moreButton2 = new InlineKeyboardButton();
        moreButton2.setText( "text2" );
        moreButton2.setCallbackData( "more2| some data" );

        keyBoardRows.add( Arrays.asList( moreButton, moreButton2 ) );
        replyMarkup.setKeyboard( keyBoardRows );

        return SendMessage.builder()
                .replyMarkup( replyMarkup )
                .chatId(String.valueOf(update.getMessage().getChatId()))
                .text("Inline.")
                .build();
    }

    @BotRequestMapping( value = "more", method = BotRequestMethod.CALLBACK )
    public List<BotApiMethod> answerCallback( final Update update ) {
        return Arrays.asList(
                SendMessage.builder()
                        .chatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()))
                        .text("Answer callback")
                        .build(),
                AnswerCallbackQuery.builder()
                        .text( "Success!" )
                        .callbackQueryId( update.getCallbackQuery().getId() )
                        .build()
        );
    }

    @BotRequestMapping( value = "more2", method = BotRequestMethod.CALLBACK )
    @CallbackConfiguration( showAlert = true, cacheTime = 0 )
    public String answerCallbackString( final Update update ) {
        return "That is amazing!";
    }
}
