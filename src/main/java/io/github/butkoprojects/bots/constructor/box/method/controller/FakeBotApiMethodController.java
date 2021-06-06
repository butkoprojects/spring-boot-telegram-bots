package io.github.butkoprojects.bots.constructor.box.method.controller;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;

public class FakeBotApiMethodController extends BotApiMethodController {

    public FakeBotApiMethodController() {
        super();
    }

    @Override
    public List<BotApiMethod> process(Update update ) {
        SendMessage.SendMessageBuilder builder = SendMessage.builder();
        return Collections.singletonList( builder.text( "default" ).chatId( determineChatId( update ) ).build() );
    }

    private String determineChatId(Update update ) {
        return update.hasMessage()
                ? update.getMessage().getChatId().toString()
                : update.getCallbackQuery().getMessage().getChatId().toString();
    }
}
