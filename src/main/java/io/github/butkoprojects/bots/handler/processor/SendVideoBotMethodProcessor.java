package io.github.butkoprojects.bots.handler.processor;

import io.github.butkoprojects.bots.util.TelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SendVideoBotMethodProcessor implements BotMethodProcessor<SendVideo> {

    @Override
    public boolean instanceOf( Object method ) {
        return method instanceof SendVideo;
    }

    @Override
    public Message processMethod( SendVideo sendVideo, TelegramBot bot ) {
        Message result;
        try {
            String mediaName = sendVideo.getVideo().getMediaName();
            String fileId = bot.tmpStorage.get( mediaName );
            if ( fileId != null ) {
                sendVideo.setVideo( new InputFile( fileId ) );
            }
            result = bot.execute( sendVideo );
            bot.tmpStorage.put( mediaName, result.getVideo().getFileId() );
        } catch (TelegramApiException e) {
            throw new IllegalStateException( e );
        }
        return result;
    }
}
