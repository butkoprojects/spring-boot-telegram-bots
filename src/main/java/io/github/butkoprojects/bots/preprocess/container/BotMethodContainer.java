package io.github.butkoprojects.bots.preprocess.container;

import io.github.butkoprojects.bots.preprocess.controller.BotApiMethodController;

import java.util.List;

public interface BotMethodContainer {

    void addBotController(String path, BotApiMethodController controller );

    BotApiMethodController getBotApiMethodController( String path );

}
