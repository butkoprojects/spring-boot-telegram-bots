package io.github.butkoprojects.bots.util;

import io.github.butkoprojects.bots.handler.BotRequestHandler;
import io.github.butkoprojects.bots.preprocess.controller.BotApiMethodController;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class TelegramBot extends TelegramLongPollingBot {

    private Map<String, String> tmpStorage = new HashMap<>();

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
        try {
            if ( method instanceof BotApiMethod ) {
                execute( ( BotApiMethod<?> ) method );
            } else if ( method instanceof SendPhoto ) {
                SendPhoto sendPhoto = (SendPhoto) method;
                String mediaName = sendPhoto.getPhoto().getMediaName();
                String fileId = tmpStorage.get( mediaName );
                if ( fileId != null ) {
                    sendPhoto.setPhoto( new InputFile( fileId ) );
                }
                Message result = execute( sendPhoto );
                tmpStorage.put( mediaName, result.getPhoto().get( 0 ).getFileId() );
            } else if ( method instanceof SendVoice) {
                execute( ( SendVoice ) method );
            }
        } catch ( TelegramApiException e ) {
            throw new IllegalStateException( e );
        }
    }

    public void updateReceived( Update update ) {
        System.out.println( "Override updateReceived method for your telegram bot" );
    }
}
