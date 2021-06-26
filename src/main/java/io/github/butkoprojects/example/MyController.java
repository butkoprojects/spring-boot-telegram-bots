package io.github.butkoprojects.example;

import io.github.butkoprojects.bots.api.annotation.*;
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

    @MessageRequest( value = "/start" )
    @KeyBoardRow({ @KeyBoardButton("button1"), @KeyBoardButton("button1") })
    @KeyBoardRow({ @KeyBoardButton("button2"), @KeyBoardButton("button2") })
    public SendMessage start2( final Update update )
    {
        return SendMessage.builder()
                .chatId(String.valueOf(update.getMessage().getChatId()))
                .text("Start message.")
                .build();
    }

    @MessageRequest( "/home" )
    @KeyBoardButton( value = "eto home bitch", requestContact = true )
    public String home( final Update update )
    {
        return "Home";
    }

    @MessageRequest( "/buttons" )
    public SendMessage buttons( final Update update ) {
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

    @MessageRequest( "/start2" )
    public SendMessage start( final Update update ) {
        InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyBoardRows = new ArrayList<>();

        InlineKeyboardButton moreButton = new InlineKeyboardButton();
        moreButton.setText( "text" );
        moreButton.setCallbackData( "more| some data" );

        keyBoardRows.add( Arrays.asList( moreButton ) );
        replyMarkup.setKeyboard( keyBoardRows );

        return SendMessage.builder()
                .replyMarkup( replyMarkup )
                .chatId(String.valueOf(update.getMessage().getChatId()))
                .text("Start message.")
                .build();
    }

    @CallbackRequest( value = "more", showAlert = false )
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

    @CallbackRequest( value = "more2", showAlert = true )
    public String answerCallbackString( final Update update ) {
        return "That is amazing!";
    }

    @MessageRequest( value = "/keyboard" )
    @KeyBoardButton("single")
    @KeyBoardButton("not single")
    public String keyboard( final Update update ) {
        return "keyboard test";
    }
}
