package io.github.butkoprojects.bots.constructor.box.method.controller;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import io.github.butkoprojects.bots.constructor.box.Process;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class BotApiMethodController {

    private Object bean;
    private Method method;
    private Process processUpdate;
    private Predicate<Update> successUpdate;

    public BotApiMethodController() {}

    public BotApiMethodController(Object bean, Method method, Predicate<Update> predicate ) {
        this.bean = bean;
        this.successUpdate = predicate;
        this.method = method;
        this.method.setAccessible( true );

        processUpdate = typeListReturnDetect() ? this::processList : this::processSingle;
    }

    public List<BotApiMethod> process(Update update ) {
        try {
            return successUpdate.test( update ) ? processUpdate.accept( update ) : null;
        } catch ( IllegalAccessException | InvocationTargetException e ) {
            throw new IllegalArgumentException( e.getMessage() );
        }
    }

    boolean typeListReturnDetect() {
        return List.class.equals( method.getReturnType() );
    }

    private List<BotApiMethod> processSingle(Update update ) throws InvocationTargetException, IllegalAccessException {
        BotApiMethod botApiMethod = (BotApiMethod) method.invoke( bean, update );
        return botApiMethod != null ? Collections.singletonList( botApiMethod ) : new ArrayList<>( 0 );
    }

    private List<BotApiMethod> processList(Update update ) throws InvocationTargetException, IllegalAccessException {
        List<BotApiMethod> botApiMethods = (List<BotApiMethod>) method.invoke( bean, update );
        return botApiMethods != null ? botApiMethods : new ArrayList<>( 0 );
    }
}
