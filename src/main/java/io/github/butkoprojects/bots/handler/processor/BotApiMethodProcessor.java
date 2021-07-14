package io.github.butkoprojects.bots.handler.processor;

import io.github.butkoprojects.bots.util.TelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class BotApiMethodProcessor implements BotMethodProcessor {

    @Override
    public boolean instanceOf( Object method ) {
        return method instanceof BotApiMethod;
    }

    @Override
    public Message processMethod( Object method,
                                  TelegramBot bot ) {
        try {
            bot.execute( ( BotApiMethod ) method );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}
