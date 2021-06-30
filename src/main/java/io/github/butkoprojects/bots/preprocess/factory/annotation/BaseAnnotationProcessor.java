package io.github.butkoprojects.bots.preprocess.factory.annotation;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

abstract class BaseAnnotationProcessor {

    boolean isReturnTypeIsBotApiMethod( Method method ) {
        return BotApiMethod.class.equals( method.getReturnType() );
    }

    boolean isReturnTypeIsString( Method method ) {
        return String.class.equals( method.getReturnType() );
    }

    boolean returnTypeIsList( Method method ) {
        return List.class.equals( method.getReturnType() );
    }

    Function<Update, List<BotApiMethod>> processSingle( ControllerBuilder builder ) {
        return update -> {
            BotApiMethod botApiMethod;
            try {
                botApiMethod = postProcessMethodInvocation( builder.getMethod().invoke( builder.getBean(), update), builder );
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
            return botApiMethod != null ? Collections.singletonList( botApiMethod ) : new ArrayList<>( 0 );
        };
    }

    Function<Update, List<BotApiMethod>> processList( Method method,
                                                      Object bean ) {
        return update -> {
            List<BotApiMethod> botApiMethods;
            try {
                botApiMethods = (List<BotApiMethod>) method.invoke( bean, update );
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(e);
            }
            return botApiMethods != null ? botApiMethods : new ArrayList<>( 0 );
        };
    }

    BotApiMethod postProcessMethodInvocation(Object result, ControllerBuilder builder) throws InvocationTargetException, IllegalAccessException {
        ReplyKeyboard keyboard = null;
        if ( builder.getKeyboardMarkup() != null ) {
            keyboard = builder.getKeyboardMarkup();
        }
        if ( builder.getInlineKeyboardMarkup() != null ) {
            keyboard = builder.getInlineKeyboardMarkup();
        }
        if ( keyboard != null ) {
            if (result instanceof SendMessage) {
                ((SendMessage) result).setReplyMarkup(keyboard);
            }
            if (result instanceof SendAnimation) {
                ((SendAnimation) result).setReplyMarkup(keyboard);
            }
            if (result instanceof SendContact) {
                ((SendContact) result).setReplyMarkup(keyboard);
            }
            if (result instanceof SendDice) {
                ((SendDice) result).setReplyMarkup(keyboard);
            }
            if (result instanceof SendDocument) {
                ((SendDocument) result).setReplyMarkup(keyboard);
            }
            if (result instanceof SendGame) {
                ((SendGame) result).setReplyMarkup(keyboard);
            }
            if (result instanceof SendLocation) {
                ((SendLocation) result).setReplyMarkup(keyboard);
            }
            if (result instanceof SendPhoto) {
                ((SendPhoto) result).setReplyMarkup(keyboard);
            }
            if (result instanceof SendSticker) {
                ((SendSticker) result).setReplyMarkup(keyboard);
            }
            if (result instanceof SendVenue) {
                ((SendVenue) result).setReplyMarkup(keyboard);
            }
            if (result instanceof SendVideo) {
                ((SendVideo) result).setReplyMarkup(keyboard);
            }
            if (result instanceof SendVideoNote) {
                ((SendVideoNote) result).setReplyMarkup(keyboard);
            }
            if (result instanceof SendVoice) {
                ((SendVoice) result).setReplyMarkup(keyboard);
            }
        }
        return ( BotApiMethod ) result;
    }
}
