package io.github.butkoprojects.bots.preprocess.controller.type;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Function;
import java.util.function.Predicate;

public interface BotControllerType {

    String type();

    Predicate<Update> updatePredicate();

    Function<Update, String> parseControllerPath();

}
