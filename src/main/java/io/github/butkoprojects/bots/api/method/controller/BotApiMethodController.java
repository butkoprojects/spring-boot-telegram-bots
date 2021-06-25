package io.github.butkoprojects.bots.api.method.controller;

import io.github.butkoprojects.bots.api.Process;
import io.github.butkoprojects.bots.api.annotation.CallbackRequest;
import io.github.butkoprojects.bots.api.annotation.KeyBoardButton;
import io.github.butkoprojects.bots.api.annotation.KeyBoardRow;
import io.github.butkoprojects.bots.api.annotation.Keyboard;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

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
    private CallbackRequest callbackConfiguration;

    public BotApiMethodController() {}

    public BotApiMethodController(Object bean,
                                  Method method,
                                  Predicate<Update> predicate,
                                  Process process,
                                  CallbackRequest callbackConfiguration) {
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
        private CallbackRequest callbackConfiguration;
        private ReplyKeyboardMarkup keyboardMarkup;
        private boolean needSession;

        public BotApiMethodControllerBuilder setKeyBoardButton( KeyBoardButton keyBoardButton ) {
            if ( keyBoardButton != null ) {
                keyboardMarkup = new ReplyKeyboardMarkup();
                final List<KeyboardRow> keyboardRows = new ArrayList<>();
                KeyboardRow keyboardRow = new KeyboardRow();

                KeyboardButton button = new KeyboardButton();
                button.setText( keyBoardButton.value() );
                button.setRequestContact( keyBoardButton.requestContact() );
                button.setRequestLocation( keyBoardButton.requestLocation() );
                keyboardRow.add( button );

                keyboardRows.add( keyboardRow );
                keyboardMarkup.setKeyboard( keyboardRows );
            }
            return this;
        }

        public BotApiMethodControllerBuilder setKeyBoardRow( KeyBoardRow rowAnnotation ) {
            if ( rowAnnotation != null ) {
                keyboardMarkup = new ReplyKeyboardMarkup();
                final List<KeyboardRow> keyboardRows = new ArrayList<>();
                    KeyboardRow keyboardRow = new KeyboardRow();
                    for ( KeyBoardButton keyBoardButton :rowAnnotation.value() ) {
                        KeyboardButton button = new KeyboardButton();
                        button.setText( keyBoardButton.value() );
                        button.setRequestContact( keyBoardButton.requestContact() );
                        button.setRequestLocation( keyBoardButton.requestLocation() );
                        keyboardRow.add( button );
                    }
                    keyboardRows.add( keyboardRow );
                keyboardMarkup.setKeyboard( keyboardRows );
            }
            return this;
        }

        public BotApiMethodControllerBuilder setKeyBoard( Keyboard keyBoard ) {
            if ( keyBoard != null ) {
                keyboardMarkup = new ReplyKeyboardMarkup();
                final List<KeyboardRow> keyboardRows = new ArrayList<>();
                for ( KeyBoardRow keyboardRow : keyBoard.value() ) {
                    KeyboardRow row = new KeyboardRow();
                    for ( KeyBoardButton keyBoardButton : keyboardRow.value() ) {
                        KeyboardButton button = new KeyboardButton();
                        button.setText( keyBoardButton.value() );
                        button.setRequestContact( keyBoardButton.requestContact() );
                        button.setRequestLocation( keyBoardButton.requestLocation() );
                        row.add( button );
                    }
                    keyboardRows.add( row );
                }
                keyboardMarkup.setKeyboard( keyboardRows );
            }
            return this;
        }

        public BotApiMethodControllerBuilder setWorkingBean( Object bean ) {
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

        public BotApiMethodControllerBuilder callbackRequest( CallbackRequest callbackRequest ) {
            callbackConfiguration = callbackRequest;
            processUpdate = isReturnTypeIsString() ?
                    this::processCallbackWithStringReturnType :
                    typeListReturnDetect() ?
                            this::processList :
                            this::processSingle;
            return this;
        }

        public BotApiMethodControllerBuilder messageRequest() {
            processUpdate = isReturnTypeIsString() ?
                    this::processNonBotApiReturnType :
                    typeListReturnDetect() ?
                            this::processList :
                            this::processSingle;
            return this;
        }

        public BotApiMethodController build() {
            return new BotApiMethodController(
                    bean,
                    method,
                    controllerShouldBeExecuted,
                    processUpdate,
                    callbackConfiguration );
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
            BotApiMethod botApiMethod = postProcessMethodInvocation( method.invoke( bean, update ) );
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
                                    .replyMarkup( keyboardMarkup )
                                    .chatId( String.valueOf( update.getMessage().getChatId() ) )
                                    .build() )
                    );
                } else {
                    resultList.add( SendMessage.builder()
                            .text( String.valueOf( returnObject ) )
                            .replyMarkup( keyboardMarkup )
                            .chatId( String.valueOf( update.getMessage().getChatId() ) )
                            .build() );
                }
            }
            return resultList;
        }

        private BotApiMethod postProcessMethodInvocation( Object result ) throws InvocationTargetException, IllegalAccessException {
            if ( result instanceof SendMessage ) { ( ( SendMessage ) result ).setReplyMarkup( keyboardMarkup ); }
            if ( result instanceof SendAnimation) { ( ( SendAnimation ) result ).setReplyMarkup( keyboardMarkup ); }
            if ( result instanceof SendContact ) { ( ( SendContact ) result ).setReplyMarkup( keyboardMarkup ); }
            if ( result instanceof SendDice ) { ( ( SendDice ) result ).setReplyMarkup( keyboardMarkup ); }
            if ( result instanceof SendDocument ) { ( ( SendDocument ) result ).setReplyMarkup( keyboardMarkup ); }
            if ( result instanceof SendGame ) { ( ( SendGame ) result ).setReplyMarkup( keyboardMarkup ); }
            if ( result instanceof SendLocation ) { ( ( SendLocation ) result ).setReplyMarkup( keyboardMarkup ); }
            if ( result instanceof SendPhoto ) { ( ( SendPhoto ) result ).setReplyMarkup( keyboardMarkup ); }
            if ( result instanceof SendSticker ) { ( ( SendSticker ) result ).setReplyMarkup( keyboardMarkup ); }
            if ( result instanceof SendVenue ) { ( ( SendVenue ) result ).setReplyMarkup( keyboardMarkup ); }
            if ( result instanceof SendVideo ) { ( ( SendVideo ) result ).setReplyMarkup( keyboardMarkup ); }
            if ( result instanceof SendVideoNote ) { ( ( SendVideoNote ) result ).setReplyMarkup( keyboardMarkup ); }
            if ( result instanceof SendVoice ) { ( ( SendVoice ) result ).setReplyMarkup( keyboardMarkup ); }
            return ( BotApiMethod ) result;
        }
    }
}
