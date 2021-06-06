package io.github.butkoprojects.bots.constructor.box.method.controller.converter;

import io.github.butkoprojects.bots.constructor.box.method.controller.BotApiMethodConditionController;
import io.github.butkoprojects.bots.constructor.box.method.controller.BotApiMethodController;

public interface BotApiMethodControllerConverter {

    BotApiMethodController convert( BotApiMethodConditionController conditionController );

}
