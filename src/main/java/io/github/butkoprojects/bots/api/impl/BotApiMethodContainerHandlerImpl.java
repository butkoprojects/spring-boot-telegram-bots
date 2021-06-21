package io.github.butkoprojects.bots.api.impl;

import io.github.butkoprojects.bots.api.method.controller.BotApiMethodConditionController;
import io.github.butkoprojects.bots.api.method.controller.FakeBotApiMethodController;
import io.github.butkoprojects.bots.api.BotApiMethodContainer;
import io.github.butkoprojects.bots.api.BotApiMethodContainerHandler;
import io.github.butkoprojects.bots.api.method.controller.BotApiMethodController;
import io.github.butkoprojects.bots.api.method.controller.converter.BotApiMethodControllerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotApiMethodContainerHandlerImpl implements BotApiMethodContainerHandler {

    @Autowired
    private BotApiMethodContainer container;

    @Autowired
    private BotApiMethodControllerConverter converter;

    public BotApiMethodController determineController( Update update ) {
        String path;
        BotApiMethodController controller = null;
        BotApiMethodConditionController conditionController = container.getAllConditionControllers()
                .stream().filter( it -> it.test( update ) ).findFirst().orElse( null );

        if ( conditionController == null ) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                path = update.getMessage().getText().split(" ")[0].trim();
                controller = container.getBotApiMethodController(path);
                if (controller == null) controller = container.getBotApiMethodController("");

            } else if (update.hasCallbackQuery()) {
                path = update.getCallbackQuery().getData().split("\\|")[0].trim();
                controller = container.getBotApiMethodController(path);
            }
        }

        return controller != null
                ? controller
                : conditionController != null
                    ? converter.convert( conditionController )
                    : new FakeBotApiMethodController();
    }
}
