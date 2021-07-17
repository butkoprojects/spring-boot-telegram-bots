package io.github.butkoprojects.bots.preprocess.annotation.processor;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.preprocess.factory.MethodInvocationContext;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

abstract class BaseAnnotationProcessor {

    protected MethodInvocationContext processMethodInvocation(ControllerBuilder builder, Update update) {
        final MethodInvocationContext context = new MethodInvocationContext();
        final List<Object> arguments = new ArrayList<>();
        Arrays.stream( builder.getMethod().getParameters() ).forEach(param -> {
                    if ( param.getType().equals( Update.class ) ) {
                        arguments.add( update );
                    }
                    if ( param.getType().equals( Map.class ) ) {
                        arguments.add( context.getParams() );
                    }
                }
        );
        try {
            context.setResultObject( builder.getMethod().invoke( builder.getBean(), arguments.toArray() ) );
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
        return context;
    }

    boolean isMarkupIsInline( ReplyKeyboard keyboard ) {
        return keyboard instanceof InlineKeyboardMarkup;
    }

    boolean isMarkupIsKeyboardMarkup( ReplyKeyboard keyboard ) {
        return keyboard instanceof InlineKeyboardMarkup;
    }

    boolean isReturnTypeIsBotApiMethod( Method method ) {
        return BotApiMethod.class.equals( method.getReturnType() );
    }

    boolean isReturnTypeIsString( Method method ) {
        return String.class.equals( method.getReturnType() );
    }

    boolean returnTypeIsList( Method method ) {
        return List.class.equals( method.getReturnType() );
    }

    Function<Update, List<Object>> processSingle( ControllerBuilder builder ) {
        return update -> {
            Object botApiMethod = postProcessMethodInvocation( processMethodInvocation( builder, update ).getResultObject(), builder );
            return botApiMethod != null ? Collections.singletonList( botApiMethod ) : new ArrayList<>( 0 );
        };
    }

    Function<Update, List<Object>> processList( ControllerBuilder builder ) {
        return update -> {
            List<Object> botApiMethods = (List<Object>) processMethodInvocation( builder, update ).getResultObject();
            return botApiMethods != null ? botApiMethods : new ArrayList<>( 0 );
        };
    }

    void processList( ControllerBuilder builder, Update update, List<Object> resultList ) {
        List<Object> botApiMethods = (List<Object>) processMethodInvocation( builder, update ).getResultObject();
        resultList.addAll( botApiMethods != null ? botApiMethods : new ArrayList<>( 0 ) );
    }

    void processSingle( ControllerBuilder builder, Update update, List<Object> resultList ) {
        Object botApiMethod = postProcessMethodInvocation( processMethodInvocation( builder, update ).getResultObject(), builder );
        resultList.addAll( botApiMethod != null ? Collections.singletonList( botApiMethod ) : new ArrayList<>( 0 ) );
    }

    Object postProcessMethodInvocation(Object result, ControllerBuilder builder) {
        ReplyKeyboard keyboard = null;
        if ( builder.getKeyboardMarkup() != null ) {
            keyboard = builder.getKeyboardMarkup();
        }
        if ( builder.getInlineKeyboardMarkup() != null ) {
            keyboard = builder.getInlineKeyboardMarkup();
        }
        if ( keyboard != null && result instanceof BotApiMethod ) {
            if (result instanceof SendMessage) {
                ((SendMessage) result).setReplyMarkup(keyboard);
            }
            if (result instanceof SendContact) {
                ((SendContact) result).setReplyMarkup(keyboard);
            }
            if (result instanceof SendDice) {
                ((SendDice) result).setReplyMarkup(keyboard);
            }
            if (result instanceof SendGame) {
                ((SendGame) result).setReplyMarkup(keyboard);
            }
            if (result instanceof SendLocation) {
                ((SendLocation) result).setReplyMarkup(keyboard);
            }
            if (result instanceof SendVenue) {
                ((SendVenue) result).setReplyMarkup(keyboard);
            }
        }
        return result;
    }
}
