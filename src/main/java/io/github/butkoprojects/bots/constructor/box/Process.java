package io.github.butkoprojects.bots.constructor.box;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@FunctionalInterface
public interface Process {
    List<BotApiMethod> accept(Update update ) throws InvocationTargetException, IllegalAccessException;
}
