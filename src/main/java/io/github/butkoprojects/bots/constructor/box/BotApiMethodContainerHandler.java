package io.github.butkoprojects.bots.constructor.box;

import io.github.butkoprojects.bots.constructor.box.method.controller.BotApiMethodController;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotApiMethodContainerHandler {

    BotApiMethodController determineController(Update update );

}
