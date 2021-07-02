package io.github.butkoprojects.bots.handler;

import io.github.butkoprojects.bots.preprocess.controller.type.BotControllerType;
import io.github.butkoprojects.bots.preprocess.controller.type.BotControllerTypeEnum;
import io.github.butkoprojects.bots.preprocess.controller.FakeBotApiMethodController;
import io.github.butkoprojects.bots.preprocess.container.BotMethodContainer;
import io.github.butkoprojects.bots.preprocess.controller.BotApiMethodController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class DefaultBotRequestHandler implements BotRequestHandler {

    @Autowired
    private BotMethodContainer container;

    @Autowired
    private List<BotControllerType> types;

    public BotApiMethodController determineController( Update update ) {
        return types.stream()
                .filter( type -> type.updatePredicate().test( update ) )
                .findFirst()
                .map( type -> container.getBotApiMethodController( type.parseControllerPath().apply( update ), type.type() ) )
                .orElse( new FakeBotApiMethodController() );
    }
}
