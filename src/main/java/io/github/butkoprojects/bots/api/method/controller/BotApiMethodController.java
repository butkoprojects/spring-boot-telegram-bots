package io.github.butkoprojects.bots.api.method.controller;

import io.github.butkoprojects.bots.api.BotRequestMethod;
import io.github.butkoprojects.bots.api.Process;
import io.github.butkoprojects.bots.api.annotation.BotRequestMapping;
import io.github.butkoprojects.bots.api.annotation.CallbackConfiguration;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

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
    private CallbackConfiguration callbackConfiguration;

    public BotApiMethodController() {}

    public BotApiMethodController(Object bean,
                                  Method method,
                                  Predicate<Update> predicate,
                                  Process process,
                                  CallbackConfiguration callbackConfiguration) {
        this.bean = bean;
        this.successUpdate = predicate;
        this.method = method;
        this.method.setAccessible( true );
        this.processUpdate = process;
        this.callbackConfiguration = callbackConfiguration;
    }

    public List<BotApiMethod> process( Update update ) {
        try {
            return successUpdate.test( update ) ? processUpdate.accept( update ) : null;
        } catch ( IllegalAccessException | InvocationTargetException e ) {
            throw new IllegalArgumentException( e.getMessage() );
        }
    }

    public static BotApiMethodControllerBuilder builder() {
        return new BotApiMethodControllerBuilder();
    }

    public static class BotApiMethodControllerBuilder {
        private Object bean;
        private Method method;
        private Process processUpdate;
        private Predicate<Update> controllerShouldBeExecuted;
        private CallbackConfiguration callbackConfiguration;

        public BotApiMethodControllerBuilder setWorkingBean(Object bean ) {
            this.bean = bean;
            return this;
        }

        public BotApiMethodControllerBuilder setMethod( Method method ) {
            this.method = method;
            this.method.setAccessible( true );
            return this;
        }

        /**
         * Predicate defines should or not method controller be executed.
         */
        public BotApiMethodControllerBuilder setPredicate( Predicate<Update> predicate ) {
            this.controllerShouldBeExecuted = predicate;
            return this;
        }

        public BotApiMethodController build() {
            BotRequestMethod requestMethod = getRequestMappingMethod();
            if ( requestMethod == BotRequestMethod.CALLBACK ) {
                updateCallbackConfiguration();
                processUpdate = isReturnTypeIsString() ?
                        this::processCallbackWithStringReturnType :
                        typeListReturnDetect() ?
                                this::processList :
                                this::processSingle;
            }
            if ( requestMethod == BotRequestMethod.MSG ) {
                processUpdate = isReturnTypeIsString() ?
                        this::processNonBotApiReturnType :
                        typeListReturnDetect() ?
                                this::processList :
                                this::processSingle;
            }
            return new BotApiMethodController( bean, method, controllerShouldBeExecuted, processUpdate, callbackConfiguration );
        }

        private void updateCallbackConfiguration() {
            callbackConfiguration = method.getAnnotation( CallbackConfiguration.class );
        }

        private BotRequestMethod getRequestMappingMethod() {
            BotRequestMapping annotation = method.getAnnotation( BotRequestMapping.class );
            return annotation.method()[0];
        }

        private boolean isReturnTypeIsString() {
            return String.class.equals( method.getReturnType() );
        }

        private boolean typeListReturnDetect() {
            return List.class.equals( method.getReturnType() );
        }

        private List<BotApiMethod> processCallbackWithStringReturnType(Update update ) throws InvocationTargetException, IllegalAccessException {
            Object returnObject = method.invoke( bean, update );
            List<BotApiMethod> resultList = new ArrayList<>();
            if ( returnObject != null ) {
                resultList.add(
                        AnswerCallbackQuery.builder()
                                .callbackQueryId( update.getCallbackQuery().getId() )
                                .text( String.valueOf( returnObject ) )
                                .showAlert( callbackConfiguration.showAlert() )
                                .cacheTime( callbackConfiguration.cacheTime() )
                                .build()
                );
            }
            return resultList;
        }

        private List<BotApiMethod> processSingle(Update update ) throws InvocationTargetException, IllegalAccessException {
            BotApiMethod botApiMethod = (BotApiMethod) method.invoke( bean, update );
            return botApiMethod != null ? Collections.singletonList( botApiMethod ) : new ArrayList<>( 0 );
        }

        private List<BotApiMethod> processList(Update update ) throws InvocationTargetException, IllegalAccessException {
            List<BotApiMethod> botApiMethods = (List<BotApiMethod>) method.invoke( bean, update );
            return botApiMethods != null ? botApiMethods : new ArrayList<>( 0 );
        }

        private List<BotApiMethod> processNonBotApiReturnType( Update update ) throws InvocationTargetException, IllegalAccessException {
            Object returnObject = method.invoke( bean, update );
            List<BotApiMethod> resultList = new ArrayList<>();
            if ( returnObject != null ) {
                if ( List.class.equals( returnObject.getClass() ) ) {
                    ( ( List ) returnObject ).forEach( obj ->
                            resultList.add( SendMessage.builder()
                                    .text( String.valueOf( obj ) )
                                    .chatId( String.valueOf( update.getMessage().getChatId() ) )
                                    .build() )
                    );
                } else {
                    resultList.add( SendMessage.builder()
                            .text( String.valueOf( returnObject ) )
                            .chatId( String.valueOf( update.getMessage().getChatId() ) )
                            .build() );
                }
            }
            return resultList;
        }
    }
}
