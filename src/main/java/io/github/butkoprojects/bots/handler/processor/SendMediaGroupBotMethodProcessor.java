package io.github.butkoprojects.bots.handler.processor;

import io.github.butkoprojects.bots.util.TelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SendMediaGroupBotMethodProcessor implements BotMethodProcessor<SendMediaGroup> {

    @Override
    public boolean instanceOf( Object method ) {
        return false;
    }

    @Override
    public Message processMethod( SendMediaGroup method, TelegramBot bot ) {
        try {
            bot.execute( method );
        } catch ( TelegramApiException e ) {
            throw new IllegalStateException( e );
        }
        return null;
    }
}
