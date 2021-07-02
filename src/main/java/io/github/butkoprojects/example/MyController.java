package io.github.butkoprojects.example;

import io.github.butkoprojects.bots.preprocess.annotation.*;
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
