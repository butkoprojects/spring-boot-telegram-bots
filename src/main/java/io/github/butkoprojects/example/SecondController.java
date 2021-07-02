package io.github.butkoprojects.example;

import io.github.butkoprojects.bots.preprocess.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@BotController
public class SecondController {

    @MessageRequest( "/hola" )
    @CallbackButtonRow({
            @CallbackButton( text = "hola", call = "answer", data = "something"),
            @CallbackButton( text = "aloh", call = "answer2", data = "something")
    })
    @CallbackButtonRow({
            @CallbackButton( text = "time", call = "time", data = "something"),
            @CallbackButton( text = "skool", call = "more", data = "something")
    })
    public String callback() {
        return "hi hi hi hi hi hi";
    }

    @CallbackRequest( "answer" )
    @CallbackButtonRow({
            @CallbackButton( text = "changedWithText", call = "answer", data = "something"),
            @CallbackButton( text = "changedWithText2", call = "answer2", data = "something")
    })
    public String answerCallback() {
        return "text changed";
    }

    @CallbackRequest( "answer2" )
    @CallbackButtonRow({
            @CallbackButton( text = "$someText", call = "$answer", data = "something"),
            @CallbackButton( text = "changedWithoutText2", call = "answer2", data = "something")
    })
    public void answerCallback2( final Map<String, String> params ) {
        params.put("someText", "ebaaaaaaaaaaaaaaat");
        params.put("answer", "ebat2| smth");
    }

    @CallbackRequest( value = "ebat2", showAlert = true )
    public String ebat2() {
        return "ogogogogogo";
    }

    @CallbackRequest( value = "time" )
    @CallbackButton( text = "$time", call = "time", data = "something")
    @CallbackButton( text = "$time", call = "time", data = "something")
    public String showTime( final Map<String, String> params ) {
        params.put( "time", String.valueOf( System.currentTimeMillis() ) );
        return params.get( "time" );
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
