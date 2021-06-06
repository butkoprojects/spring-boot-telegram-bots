package io.github.butkoprojects.bots.constructor.box;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotRequestMappingCondition {

    /**
     * Do not throw exception from this method, just return true or false
     * @return true or false
     */
    boolean test( Update update );

}
