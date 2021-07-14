package io.github.butkoprojects.bots.handler.processor;

import io.github.butkoprojects.bots.util.TelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SendPhotoBotMethodProcessor implements BotMethodProcessor<SendPhoto> {

    @Override
    public boolean instanceOf( Object method ) {
        return method instanceof SendPhoto;
    }

    @Override
    public Message processMethod( SendPhoto sendPhoto, TelegramBot bot ) {
        Message result;
        try {
            String mediaName = sendPhoto.getPhoto().getMediaName();
            String fileId = bot.tmpStorage.get( mediaName );
            if ( fileId != null ) {
                sendPhoto.setPhoto( new InputFile( fileId ) );
            }
            result = bot.execute( sendPhoto );
            bot.tmpStorage.put( mediaName, result.getPhoto().get( 0 ).getFileId() );
        } catch (TelegramApiException e) {
            throw new IllegalStateException( e );
        }
        return result;
    }
}
