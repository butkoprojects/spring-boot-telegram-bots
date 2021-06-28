package io.github.butkoprojects.example;

import io.github.butkoprojects.bots.util.TelegramBot;
import org.springframework.stereotype.Component;

@Component
public class MyBot extends TelegramBot {

    @Override
    public String getBotUsername() {
        return "@MotherPleaseCreateMeBot";
    }

    @Override
    public String getBotToken() {
        return "1136149470:AAEycB71ZvqlqgfWcwYOxtRdqE32rzLUKx4";
    }
}
