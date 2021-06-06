package io.github.butkoprojects.example;

import io.github.butkoprojects.bots.api.wrapper.TelegramLongPollingBotWrapper;
import org.springframework.stereotype.Component;

@Component
public class MyBot extends TelegramLongPollingBotWrapper {

    @Override
    public String getBotUsername() {
        return "@MotherPleaseCreateMeBot";
    }

    @Override
    public String getBotToken() {
        return "1136149470:AAEycB71ZvqlqgfWcwYOxtRdqE32rzLUKx4";
    }
}
