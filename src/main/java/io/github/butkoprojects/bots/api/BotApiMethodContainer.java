package io.github.butkoprojects.bots.api;

import io.github.butkoprojects.bots.api.method.controller.BotApiMethodConditionController;
import io.github.butkoprojects.bots.api.method.controller.BotApiMethodController;

import java.util.List;

public interface BotApiMethodContainer {

    List<BotApiMethodConditionController> getAllConditionControllers();

    void addConditionController( BotApiMethodConditionController controller );

    void addBotController(String path, BotApiMethodController controller );

    BotApiMethodController getBotApiMethodController( String path );

}
