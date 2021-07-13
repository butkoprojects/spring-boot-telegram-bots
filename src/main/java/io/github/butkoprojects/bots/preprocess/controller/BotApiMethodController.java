package io.github.butkoprojects.bots.preprocess.controller;

import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class BotApiMethodController {

    private Function<Update, List<Object>> processUpdate;
    private Predicate<Update> successUpdate;

    public BotApiMethodController() {}

    public BotApiMethodController( Predicate<Update> predicate,
                                   Function<Update, List<Object>> process ) {
        this.successUpdate = predicate;
        this.processUpdate = process;
    }

    public List<Object> process( Update update ) {
        return successUpdate.test( update ) ? processUpdate.apply( update ) : null;
    }
}
