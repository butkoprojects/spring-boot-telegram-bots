package io.github.butkoprojects.bots.preprocess.controller.type;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class CallbackBotControllerType implements BotControllerType {

    @Override
    public String type() {
        return BotControllerTypeEnum.CALLBACK.type;
    }

    @Override
    public Predicate<Update> updatePredicate() {
        return BotControllerTypeEnum.CALLBACK.updatePredicate;
    }

    @Override
    public Function<Update, String> parseControllerPath() {
        return BotControllerTypeEnum.CALLBACK.parseControllerPath;
    }
}
