package io.github.butkoprojects.bots.handler.processor;

import io.github.butkoprojects.bots.util.TelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SendAnimationBotMethodProcessor implements BotMethodProcessor<SendAnimation> {

    @Override
    public boolean instanceOf( Object method ) {
        return method instanceof SendAnimation;
    }

    @Override
    public Message processMethod( SendAnimation sendAnimation,
                                  TelegramBot bot ) {
        Message result;
        try {
            String mediaName = sendAnimation.getAnimation().getMediaName();
            String fileId = bot.tmpStorage.get( mediaName );
            if ( fileId != null ) {
                sendAnimation.setAnimation( new InputFile( fileId ) );
            }
            result = bot.execute( sendAnimation );
            bot.tmpStorage.put( mediaName, result.getAnimation().getFileId() );
        } catch (TelegramApiException e) {
            throw new IllegalStateException( e );
        }
        return result;
    }
}
