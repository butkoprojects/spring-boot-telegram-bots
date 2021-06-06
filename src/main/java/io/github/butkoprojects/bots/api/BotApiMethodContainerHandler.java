package io.github.butkoprojects.bots.api;

import io.github.butkoprojects.bots.api.method.controller.BotApiMethodController;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotApiMethodContainerHandler {

    BotApiMethodController determineController(Update update );

}
