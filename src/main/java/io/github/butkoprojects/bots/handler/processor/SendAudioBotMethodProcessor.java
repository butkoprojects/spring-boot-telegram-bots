package io.github.butkoprojects.bots.handler.processor;

import io.github.butkoprojects.bots.util.TelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SendAudioBotMethodProcessor implements BotMethodProcessor<SendAudio> {

    @Override
    public boolean instanceOf(Object method) {
        return method instanceof SendAudio;
    }

    @Override
    public Message processMethod( SendAudio sendAudio, TelegramBot bot ) {
        Message result;
        try {
            String mediaName = sendAudio.getAudio().getMediaName();
            String fileId = bot.tmpStorage.get( mediaName );
            if ( fileId != null ) {
                sendAudio.setAudio( new InputFile( fileId ) );
            }
            result = bot.execute( sendAudio );
            bot.tmpStorage.put( mediaName, result.getAudio().getFileId() );
        } catch (TelegramApiException e) {
            throw new IllegalStateException( e );
        }
        return result;
    }
}
