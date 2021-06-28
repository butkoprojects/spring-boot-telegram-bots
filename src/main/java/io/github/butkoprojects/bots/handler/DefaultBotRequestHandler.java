package io.github.butkoprojects.bots.handler;

import io.github.butkoprojects.bots.preprocess.controller.FakeBotApiMethodController;
import io.github.butkoprojects.bots.preprocess.container.BotMethodContainer;
import io.github.butkoprojects.bots.preprocess.controller.BotApiMethodController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DefaultBotRequestHandler implements BotRequestHandler {

    @Autowired
    private BotMethodContainer container;

    public BotApiMethodController determineController( Update update ) {
        String path;
        BotApiMethodController controller = null;

            if (update.hasMessage() && update.getMessage().hasText()) {
                path = update.getMessage().getText().split(" ")[0].trim();
                controller = container.getBotApiMethodController(path);
                if (controller == null) controller = container.getBotApiMethodController("");

            } else if (update.hasCallbackQuery()) {
                path = update.getCallbackQuery().getData().split("\\|")[0].trim();
                controller = container.getBotApiMethodController(path);
            }

        return controller != null ? controller : new FakeBotApiMethodController();
    }
}
