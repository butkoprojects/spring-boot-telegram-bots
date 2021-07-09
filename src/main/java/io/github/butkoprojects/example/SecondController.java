package io.github.butkoprojects.example;

import io.github.butkoprojects.bots.preprocess.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@BotController
public class SecondController {

    @MessageRequest( "/hola" )
    @CallbackButtonRow({
            @CallbackButton( name = "hola", call = "answer", data = "something"),
            @CallbackButton( name = "aloh", call = "answer2", data = "something")
    })
    @CallbackButtonRow({
            @CallbackButton( name = "time", call = "time", data = "something"),
            @CallbackButton( name = "skool", call = "more", data = "something")
    })
    public String callback() {
        return "hi hi hi hi hi hi";
    }

    @CallbackRequest( "answer" )
    @CallbackButtonRow({
            @CallbackButton( name = "changedWithText", call = "answer", data = "something"),
            @CallbackButton( name = "changedWithText2", call = "answer2", data = "something")
    })
    public String answerCallback() {
        return "text changed";
    }

    @CallbackRequest( "answer2" )
    @CallbackButtonRow({
            @CallbackButton( name = "$someText", call = "$answer", data = "something"),
            @CallbackButton( name = "changedWithoutText2", call = "answer2", data = "something")
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
    @CallbackButton( name = "$time", call = "time", data = "something")
    @CallbackButton( name = "$time", call = "time", data = "something")
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

    @MessageRequest( "button1" )
    @KeyBoardButton( "new button" )
    public String button1() {
        return "response on button1";
    }

    @MessageRequest( "/home" )
    @KeyBoardRow({ @KeyBoardButton("button1"), @KeyBoardButton("button1") })
    public String home( final Update update )
    {
        return "Home";
    }

}
