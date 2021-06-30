package io.github.butkoprojects.bots.preprocess.controller;

import io.github.butkoprojects.bots.preprocess.annotation.*;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class BotApiMethodController {

    private Function<Update, List<BotApiMethod>> processUpdate;
    private Predicate<Update> successUpdate;

    public BotApiMethodController() {}

    public BotApiMethodController(Predicate<Update> predicate,
                                  Function<Update, List<BotApiMethod>> process) {
        this.successUpdate = predicate;
        this.processUpdate = process;
    }

    public List<BotApiMethod> process( Update update ) {
        return successUpdate.test( update ) ? processUpdate.apply( update ) : null;
    }

    public static BotApiMethodControllerBuilder builder() {
        return new BotApiMethodControllerBuilder();
    }

    public static class BotApiMethodControllerBuilder {
        private Object bean;
        private Method method;
        private Function<Update, List<BotApiMethod>> processUpdate;
        private Predicate<Update> controllerShouldBeExecuted;
        private CallbackRequest callbackConfiguration;
        private ReplyKeyboardMarkup keyboardMarkup;
        private InlineKeyboardMarkup inlineKeyboardMarkup;
        private boolean needSession;

        public BotApiMethodControllerBuilder setCallbackKeyboard( CallbackKeyboard callbackKeyboard ) {
            if ( callbackKeyboard != null ) {
                inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> keyBoardRows = new ArrayList<>();

                for ( CallbackButtonRow row: callbackKeyboard.value() ) {
                    List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
                    for ( CallbackButton button: row.value() ) {
                        InlineKeyboardButton inlineButton = new InlineKeyboardButton();
                        inlineButton.setText( button.text() );
                        inlineButton.setCallbackData( button.call() + "|" + button.data() );
                        keyboardRow.add( inlineButton );
                    }
                    keyBoardRows.add( keyboardRow );
                }
                inlineKeyboardMarkup.setKeyboard( keyBoardRows );
            }
            return this;
        }

        public BotApiMethodControllerBuilder setCallbackButtonRow( CallbackButtonRow callbackButtonRow ) {
            if ( callbackButtonRow != null ) {
                inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> keyBoardRows = new ArrayList<>();

                List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
                for ( CallbackButton button: callbackButtonRow.value() ) {
                    InlineKeyboardButton inlineButton = new InlineKeyboardButton();
                    inlineButton.setText( button.text() );
                    inlineButton.setCallbackData( button.call() + "|" + button.data() );
                    keyboardRow.add( inlineButton );
                }

                keyBoardRows.add( keyboardRow );
                inlineKeyboardMarkup.setKeyboard( keyBoardRows );
            }
            return this;
        }

        public BotApiMethodControllerBuilder setCallbackButton( CallbackButton callbackButton ) {
            if ( callbackButton != null ) {
                inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> keyBoardRows = new ArrayList<>();
                List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

                InlineKeyboardButton inlineButton = new InlineKeyboardButton();
                inlineButton.setText( callbackButton.text() );
                inlineButton.setCallbackData( callbackButton.call() + "|" + callbackButton.data() );
                keyboardRow.add( inlineButton );

                keyBoardRows.add( keyboardRow );
                inlineKeyboardMarkup.setKeyboard( keyBoardRows );
            }
            return this;
        }

        public BotApiMethodControllerBuilder setKeyBoardButton( KeyBoardButton keyBoardButton ) {
            if ( keyBoardButton != null ) {
                keyboardMarkup = new ReplyKeyboardMarkup();
                keyboardMarkup.setResizeKeyboard( true );
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
                    returnTypeIsList() ?
                            this::processList :
                            this::processSingle;
            return this;
        }

        public BotApiMethodControllerBuilder messageRequest() {
            processUpdate = isReturnTypeIsString() ?
                    this::processNonBotApiReturnType :
                    returnTypeIsList() ?
                            this::processList :
                            this::processSingle;
            return this;
        }

        public BotApiMethodController build() {
            if ( keyboardMarkup != null ) {
                keyboardMarkup.setResizeKeyboard(true);
            }
            return new BotApiMethodController(
                    controllerShouldBeExecuted,
                    processUpdate );
        }

        private boolean isReturnTypeIsString() {
            return String.class.equals( method.getReturnType() );
        }

        private boolean returnTypeIsList() {
            return List.class.equals( method.getReturnType() );
        }

        private List<BotApiMethod> processCallbackWithStringReturnType(Update update ) {
            Object returnObject;
            try {
                returnObject = method.invoke( bean, update );
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(e);
            }
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

        private List<BotApiMethod> processSingle(Update update ) {
            BotApiMethod botApiMethod = null;
            try {
                botApiMethod = postProcessMethodInvocation( method.invoke( bean, update ) );
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
            return botApiMethod != null ? Collections.singletonList( botApiMethod ) : new ArrayList<>( 0 );
        }

        private List<BotApiMethod> processList( Update update ) {
            List<BotApiMethod> botApiMethods = null;
            try {
                botApiMethods = (List<BotApiMethod>) method.invoke( bean, update );
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(e);
            }
            return botApiMethods != null ? botApiMethods : new ArrayList<>( 0 );
        }

        private List<BotApiMethod> processNonBotApiReturnType( Update update ) {
            ReplyKeyboard keyboard = keyboardMarkup != null
                    ? keyboardMarkup
                    : inlineKeyboardMarkup != null
                        ? inlineKeyboardMarkup
                        : null;
            Object returnObject = null;
            try {
                returnObject = method.invoke( bean, update );
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(e);
            }
            List<BotApiMethod> resultList = new ArrayList<>();
            if ( returnObject != null ) {
                if ( List.class.equals( returnObject.getClass() ) ) {
                    ( ( List ) returnObject ).forEach( obj ->
                            resultList.add( SendMessage.builder()
                                    .text( String.valueOf( obj ) )
                                    .replyMarkup( keyboard )
                                    .chatId( String.valueOf( update.getMessage().getChatId() ) )
                                    .build() )
                    );
                } else {
                    resultList.add( SendMessage.builder()
                            .text( String.valueOf( returnObject ) )
                            .replyMarkup( keyboard )
                            .chatId( String.valueOf( update.getMessage().getChatId() ) )
                            .build() );
                }
            }
            return resultList;
        }

        private BotApiMethod postProcessMethodInvocation( Object result ) throws InvocationTargetException, IllegalAccessException {
            ReplyKeyboard keyboard = null;
            if ( keyboardMarkup != null ) {
                keyboard = keyboardMarkup;
            }
            if ( inlineKeyboardMarkup != null ) {
                keyboard = inlineKeyboardMarkup;
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
}
