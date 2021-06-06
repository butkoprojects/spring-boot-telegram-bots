package io.github.butkoprojects.bots.constructor.box.realization;

import io.github.butkoprojects.bots.constructor.box.BotRequestMappingCondition;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DefaultBotRequestMappingCondition implements BotRequestMappingCondition {

    @Override
    public boolean test(Update update) {
        return false;
    }
}
