package io.github.butkoprojects.bots.api.method.controller;

import io.github.butkoprojects.bots.api.BotRequestMappingCondition;
import io.github.butkoprojects.bots.api.Process;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BotApiMethodConditionController {

    private Object bean;
    private Method method;
    private Process processUpdate;
    private BotRequestMappingCondition condition;

    public BotApiMethodConditionController(Object bean, Method method, BotRequestMappingCondition condition ) {
        this.bean = bean;
        this.method = method;
        this.condition = condition;

        processUpdate = typeListReturnDetect() ? this::processList : this::processSingle;
    }

    public List<BotApiMethod> process( Update update ) {
        try {
            return processUpdate.accept( update );
        } catch ( IllegalAccessException | InvocationTargetException e ) {
            throw new IllegalArgumentException( e.getMessage() );
        }
    }

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }

    public boolean test(Update update ) {
        return condition.test( update );
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
