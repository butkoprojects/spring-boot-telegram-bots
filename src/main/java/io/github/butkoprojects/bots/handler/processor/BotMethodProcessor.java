package io.github.butkoprojects.bots.handler.processor;

import io.github.butkoprojects.bots.util.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotMethodProcessor<T> {

    boolean instanceOf( Object method );

    Message processMethod( T method, TelegramBot bot );

}
