package io.github.butkoprojects.example;

import io.github.butkoprojects.bots.api.annotation.BotController;
import io.github.butkoprojects.bots.api.annotation.CallbackRequest;
import io.github.butkoprojects.bots.api.annotation.MessageRequest;
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
    public SendMessage start( final Update update )
    {
        return SendMessage.builder()
                .chatId(String.valueOf(update.getMessage().getChatId()))
                .text("Start message.")
                .build();
    }

    @MessageRequest( "/home" )
    public String home( final Update update )
    {
        return "Home";
    }

    @MessageRequest( "/buttons" )
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

    @MessageRequest( "/inline" )
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

    @MessageRequest( value = "chat" )
    public String chatId( final Update update ) {
        return String.valueOf( update.getMessage().getChatId() );
    }

    @MessageRequest( value = "/textTest" )
    public String textTest( final Update update ) {
        return "ogo!";
    }

    @MessageRequest( value = "/gavno" )
    public String boolshit( final Update update ) {
        return "gavno";
    }
}
