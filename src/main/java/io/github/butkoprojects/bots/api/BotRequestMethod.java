package io.github.butkoprojects.bots.api;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Predicate;

public enum BotRequestMethod {

    MSG {
        @Override
        Predicate<Update> getPredicate() {
            return update -> update != null && update.hasMessage() && update.getMessage().hasText();
        }
    },
    CALLBACK {
        @Override
        Predicate<Update> getPredicate() {
            return update -> update != null && update.hasCallbackQuery() && update.getCallbackQuery().getData() != null;
        }
    };

    abstract Predicate<Update> getPredicate();
}
