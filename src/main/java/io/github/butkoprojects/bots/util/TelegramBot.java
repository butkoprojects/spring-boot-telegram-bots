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
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private BotRequestHandler handler;

    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public void onUpdateReceived( Update update ) {
        BotApiMethodController controller = handler.determineController( update );
        executor.execute( () -> {
            List<BotApiMethod> botApiMethods = controller.process( update );
            if ( botApiMethods != null && botApiMethods.isEmpty() ) {
                updateReceived( update );
            } else {
                Objects.requireNonNull( botApiMethods ).forEach( this::executeWithoutCheckedException );
            }
        } );
    }

    private Message executeWithoutCheckedException( final BotApiMethod method ) {
        try {
            Object result = execute( method );
            if ( result instanceof Message ) {
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
