package io.github.butkoprojects.bots.handler;

import io.github.butkoprojects.bots.preprocess.controller.BotApiMethodController;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotRequestHandler {

    BotApiMethodController determineController(Update update );

}
