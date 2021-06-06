package io.github.butkoprojects.bots.api;

import io.github.butkoprojects.bots.api.annotation.BotControllerCondition;
import org.telegram.telegrambots.meta.api.objects.Update;

@BotControllerCondition
public class BotRequestMappingConditionImpl implements BotRequestMappingCondition {

    @Override
    public boolean test( Update update ) {
        return update.hasMessage() && update.getMessage().hasPhoto();
    }

}
