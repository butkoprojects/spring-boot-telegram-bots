package io.github.butkoprojects.bots.constructor;

import io.github.butkoprojects.bots.constructor.box.BotRequestMappingCondition;
import io.github.butkoprojects.bots.constructor.box.annotation.BotControllerCondition;
import org.telegram.telegrambots.meta.api.objects.Update;

@BotControllerCondition
public class BotRequestMappingConditionImpl implements BotRequestMappingCondition {

    @Override
    public boolean test( Update update ) {
        return update.hasMessage() && update.getMessage().hasPhoto();
    }

}
