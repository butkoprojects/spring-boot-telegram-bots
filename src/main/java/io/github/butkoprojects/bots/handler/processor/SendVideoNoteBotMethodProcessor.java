package io.github.butkoprojects.bots.handler.processor;

import io.github.butkoprojects.bots.util.TelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendVideoNote;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SendVideoNoteBotMethodProcessor implements BotMethodProcessor<SendVideoNote> {

    @Override
    public boolean instanceOf(Object method) {
        return method instanceof SendVideoNote;
    }

    @Override
    public Message processMethod( SendVideoNote method, TelegramBot bot ) {
        try {
            bot.execute( method );
            return null;
        } catch (TelegramApiException e) {
            throw new IllegalStateException( e );
        }
    }
}
