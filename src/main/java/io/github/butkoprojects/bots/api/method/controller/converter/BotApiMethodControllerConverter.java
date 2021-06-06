package io.github.butkoprojects.bots.api.method.controller.converter;

import io.github.butkoprojects.bots.api.method.controller.BotApiMethodConditionController;
import io.github.butkoprojects.bots.api.method.controller.BotApiMethodController;

public interface BotApiMethodControllerConverter {

    BotApiMethodController convert( BotApiMethodConditionController conditionController );

}
