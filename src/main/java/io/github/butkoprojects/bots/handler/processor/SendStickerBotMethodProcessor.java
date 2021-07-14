package io.github.butkoprojects.bots.handler.processor;

import io.github.butkoprojects.bots.util.TelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SendStickerBotMethodProcessor implements BotMethodProcessor<SendSticker> {

    @Override
    public boolean instanceOf( Object method ) {
        return method instanceof SendSticker;
    }

    @Override
    public Message processMethod( SendSticker sendSticker, TelegramBot bot ) {
        try {
            String mediaName = sendSticker.getSticker().getMediaName();
            String fileId = bot.tmpStorage.get( mediaName );
            if ( fileId != null ) {
                sendSticker.setSticker( new InputFile( fileId ) );
            }
            Message message = bot.execute( sendSticker );
            bot.tmpStorage.put( mediaName, message.getSticker().getFileId() );
            return message;
        } catch (TelegramApiException e) {
            throw new IllegalStateException( e );
        }
    }
}
