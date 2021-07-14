package io.github.butkoprojects.bots.handler.processor;

import io.github.butkoprojects.bots.util.TelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SendDocumentBotMethodProcessor implements BotMethodProcessor<SendDocument> {

    @Override
    public boolean instanceOf(Object method) {
        return method instanceof SendDocument;
    }

    @Override
    public Message processMethod( SendDocument sendDocument, TelegramBot bot ) {
        Message result;
        try {
            String mediaName = sendDocument.getDocument().getMediaName();
            String fileId = bot.tmpStorage.get( mediaName );
            if ( fileId != null ) {
                sendDocument.setDocument( new InputFile( fileId ) );
            }
            result = bot.execute( sendDocument );
            bot.tmpStorage.put( mediaName, result.getDocument().getFileId() );
        } catch (TelegramApiException e) {
            throw new IllegalStateException( e );
        }
        return result;
    }
}
