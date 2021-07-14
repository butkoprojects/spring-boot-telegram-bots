package io.github.butkoprojects.bots.util;

import io.github.butkoprojects.bots.handler.BotRequestHandler;
import io.github.butkoprojects.bots.handler.processor.BotMethodProcessor;
import io.github.butkoprojects.bots.preprocess.controller.BotApiMethodController;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class TelegramBot extends TelegramLongPollingBot {

    public Map<String, String> tmpStorage = new HashMap<>();

    @Autowired
    private List<BotMethodProcessor> processors;

    @Autowired
    private BotRequestHandler handler;

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public void onUpdateReceived( Update update ) {
        BotApiMethodController controller = handler.determineController( update );
        executor.execute( () -> {
            List<Object> botApiMethods = controller.process( update );
            if ( botApiMethods != null && botApiMethods.isEmpty() ) {
                updateReceived( update );
            } else {
                Objects.requireNonNull( botApiMethods ).forEach( this::executeWithoutCheckedException );
            }
        } );
    }

    private void executeWithoutCheckedException( final Object method ) {
        processors.stream()
                .filter( processor -> processor.instanceOf( method ) )
                .findFirst()
                .ifPresent( processor -> processor.processMethod( method, this ) );
    }

    public void updateReceived( Update update ) {
        System.out.println( "Override updateReceived method for your telegram bot" );
    }
}
