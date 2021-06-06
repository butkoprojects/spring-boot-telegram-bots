package io.github.butkoprojects.bots.constructor.box;

import io.github.butkoprojects.bots.constructor.box.method.controller.BotApiMethodConditionController;
import io.github.butkoprojects.bots.constructor.box.method.controller.BotApiMethodController;

import java.util.List;

public interface BotApiMethodContainer {

    List<BotApiMethodConditionController> getAllConditionControllers();

    void addConditionController( BotApiMethodConditionController controller );

    void addBotController(String path, BotApiMethodController controller );

    BotApiMethodController getBotApiMethodController( String path );

}
