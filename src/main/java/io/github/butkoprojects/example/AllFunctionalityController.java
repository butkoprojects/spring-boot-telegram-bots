package io.github.butkoprojects.example;

import io.github.butkoprojects.bots.preprocess.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@BotController
public class AllFunctionalityController {

    @MessageRequest( "/test1" )
    public SendMessage test1( final Update update ) {
        return SendMessage.builder()
                .chatId(String.valueOf(update.getMessage().getChatId()))
                .text("tests ability to use 'raw' SendMessage logic")
                .build();
    }

    @MessageRequest( "/test2" )
    public String test2( final Update update ) {
        return "tests ability to return String";
    }

    @MessageRequest( "/test3" )
    public String test3() {
        return "tests ability to have empty params for method";
    }

    @MessageRequest( "/test4" )
    @KeyBoardButton( "test4" )
    public SendMessage test4( final Update update ) {
        return SendMessage.builder()
                .chatId(String.valueOf(update.getMessage().getChatId()))
                .text("tests ability to use 'raw' SendMessage logic with keyboard annotation")
                .build();
    }

    @MessageRequest( "/test5" )
    @KeyBoardButton( "test5" )
    public String test5() {
        return "tests ability to add keyboard and return string";
    }

    @MessageRequest( "/test6" )
    @CallbackButton( call = "test7", name = "test7" )
    @CallbackButton( call = "test8", name = "test8" )
    @CallbackButton( call = "test9", name = "test9" )
    @CallbackButton( call = "test10", name = "test10" )
    public String test6() {
        return "tests callback buttons";
    }

    @CallbackRequest( "test7" )
    public String test7() {
        return "tests ability to answer callback";
    }

    @CallbackRequest( "test8" )
    @CallbackButton( call = "test7", name = "test7edited" )
    @CallbackButton( call = "test8", name = "test8edited" )
    @CallbackButton( call = "test9", name = "test9edited" )
    @CallbackButton( call = "test10", name = "test10edited" )
    public String test8() {
        return "ability to edit callback keyboard";
    }

    @CallbackRequest( "test9" )
    @CallbackButton( call = "test7", name = "$test7" )
    @CallbackButton( call = "test8", name = "$test8" )
    @CallbackButton( call = "test9", name = "$test9" )
    @CallbackButton( call = "test10", name = "$test10" )
    public String test9( Map<String, String> params ) {
        params.put( "test7", "TEST7" );
        params.put( "test8", "TEST8" );
        params.put( "test9", "TEST9" );
        params.put( "test10", "TEST10" );
        return "test ability to put params into callback buttons";
    }

    @CallbackRequest( "test10" )
    @CallbackButton( call = "test9", name = "$msg" )
    public void test10( Map<String, String> params ) {
        params.put( "msg", "void method" );
    }
}
