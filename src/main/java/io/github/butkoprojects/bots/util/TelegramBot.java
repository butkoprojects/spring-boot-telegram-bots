package io.github.butkoprojects.bots.util;

import io.github.butkoprojects.bots.handler.BotRequestHandler;
import io.github.butkoprojects.bots.preprocess.controller.BotApiMethodController;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public abstract class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private BotRequestHandler handler;

    @Override
    public void onUpdateReceived( Update update ) {
        BotApiMethodController controller = handler.determineController( update );
        List<BotApiMethod> botApiMethods = controller.process( update );
        if ( botApiMethods != null && botApiMethods.isEmpty() ) {
            updateReceived( update );
        } else {
            botApiMethods.forEach( this::executeWithoutCheckedException );
        }
    }

    private Message executeWithoutCheckedException(final BotApiMethod method ) {
        try {
            Object result = execute( method );
            if ( result instanceof Message) {
                return (Message) result;
            } else return null;
        } catch ( TelegramApiException e ) {
            throw new IllegalStateException( e );
        }
    }

    public void updateReceived( Update update ) {
        System.out.println( "Override updateReceived method for your telegram bot" );
    }
}
