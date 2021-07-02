package io.github.butkoprojects.bots.preprocess.controller.type;

import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.function.Function;
import java.util.function.Predicate;

public enum BotControllerTypeEnum {

    MESSAGE( "message",
            update -> update != null && update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText() != null,
            update -> { return update.getMessage().getText().split(" ")[0].trim(); }
    ),
    CALLBACK( "callback",
            update -> update != null && update.hasCallbackQuery() && update.getCallbackQuery().getData() != null,
            update -> { return update.getCallbackQuery().getData().split("\\|")[0].trim(); }
    ),
    INLINE(
            "inline",
            update -> update != null && update.hasInlineQuery() && update.getInlineQuery() != null && update.getInlineQuery().getQuery() != null,
            update -> "default"
    );

    public final String type;
    public final Predicate<Update> updatePredicate;
    public final Function<Update, String> parseControllerPath;

    BotControllerTypeEnum( String aType,
                           Predicate<Update> aUpdatePredicate,
                           Function<Update, String> aParseControllerPath ) {
        type = aType;
        updatePredicate = aUpdatePredicate;
        parseControllerPath = aParseControllerPath;
    }
}
