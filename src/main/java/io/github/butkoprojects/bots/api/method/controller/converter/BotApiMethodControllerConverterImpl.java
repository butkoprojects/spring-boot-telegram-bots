package io.github.butkoprojects.bots.api.method.controller.converter;

import io.github.butkoprojects.bots.api.method.controller.BotApiMethodConditionController;
import io.github.butkoprojects.bots.api.method.controller.BotApiMethodController;
import org.springframework.stereotype.Component;

@Component
public class BotApiMethodControllerConverterImpl implements BotApiMethodControllerConverter {

    @Override
    public BotApiMethodController convert( BotApiMethodConditionController conditionController) {
        return new BotApiMethodController( conditionController.getBean(), conditionController.getMethod(), update -> true );
    }
}
