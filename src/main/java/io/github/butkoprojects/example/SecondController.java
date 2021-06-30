package io.github.butkoprojects.example;

import io.github.butkoprojects.bots.preprocess.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@BotController
public class SecondController {

    @MessageRequest( "/hola" )
    @CallbackButtonRow({
            @CallbackButton( text = "hola", call = "more", data = "something"),
            @CallbackButton( text = "aloh", call = "more", data = "something")
    })
    @CallbackButtonRow({
            @CallbackButton( text = "loks", call = "more", data = "something"),
            @CallbackButton( text = "skool", call = "more", data = "something")
    })
    public String callback( final Update update ) {
        return "hi hi hi hi hi hi";
    }

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

}
