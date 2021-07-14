package io.github.butkoprojects.bots.handler.processor;

import io.github.butkoprojects.bots.util.TelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SendVoiceBotMethodProcessor implements BotMethodProcessor<SendVoice> {

    @Override
    public boolean instanceOf( Object method ) {
        return method instanceof SendVoice;
    }

    @Override
    public Message processMethod( SendVoice sendVoice, TelegramBot bot ) {
        Message result;
        try {
            String mediaName = sendVoice.getVoice().getMediaName();
            String fileId = bot.tmpStorage.get( mediaName );
            if ( fileId != null ) {
                sendVoice.setVoice( new InputFile( fileId ) );
            }
            result = bot.execute( sendVoice );
            bot.tmpStorage.put( mediaName, result.getVoice().getFileId() );
        } catch (TelegramApiException e) {
            throw new IllegalStateException( e );
        }
        return result;
    }
}
