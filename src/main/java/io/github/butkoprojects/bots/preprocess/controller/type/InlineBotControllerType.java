package io.github.butkoprojects.bots.preprocess.controller.type;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class InlineBotControllerType implements BotControllerType {

    @Override
    public String type() {
        return BotControllerTypeEnum.INLINE.type;
    }

    @Override
    public Predicate<Update> updatePredicate() {
        return BotControllerTypeEnum.INLINE.updatePredicate;
    }

    @Override
    public Function<Update, String> parseControllerPath() {
        return BotControllerTypeEnum.INLINE.parseControllerPath;
    }
}
