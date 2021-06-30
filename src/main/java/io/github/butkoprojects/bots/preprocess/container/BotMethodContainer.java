package io.github.butkoprojects.bots.preprocess.container;

import io.github.butkoprojects.bots.preprocess.controller.BotApiMethodController;

public interface BotMethodContainer {

    void addBotController( String path, String controllerType, BotApiMethodController controller );

    BotApiMethodController getBotApiMethodController( String path, String controllerType );

}
