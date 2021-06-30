package io.github.butkoprojects.bots.preprocess.controller.builder;

import io.github.butkoprojects.bots.preprocess.controller.BotApiMethodController;
import io.github.butkoprojects.bots.preprocess.annotation.CallbackRequest;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class ControllerBuilder {
    private Object bean;
    private Method method;
    private Function<Update, List<BotApiMethod>> processFunction;
    private Predicate<Update> controllerShouldBeExecuted;
    private CallbackRequest callbackConfiguration;
    private ReplyKeyboardMarkup keyboardMarkup;
    private InlineKeyboardMarkup inlineKeyboardMarkup;
    private String path;
    private String controllerType;

    public abstract ControllerBuilder instance();

    public BotApiMethodController build() {
        return new BotApiMethodController(
                controllerShouldBeExecuted,
                processFunction );
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ReplyKeyboardMarkup getKeyboardMarkup() {
        return keyboardMarkup;
    }

    public ControllerBuilder setKeyboardMarkup(ReplyKeyboardMarkup keyboardMarkup) {
        this.keyboardMarkup = keyboardMarkup;
        keyboardMarkup.setResizeKeyboard(true);
        return this;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public ControllerBuilder setMethod(Method method) {
        this.method = method;
        method.setAccessible( true );
        return this;
    }

    public Function<Update, List<BotApiMethod>> getProcessFunction() {
        return processFunction;
    }

    public ControllerBuilder setProcessFunction(Function<Update, List<BotApiMethod>> processUpdate) {
        this.processFunction = processUpdate;
        return this;
    }

    public Predicate<Update> getControllerShouldBeExecuted() {
        return controllerShouldBeExecuted;
    }

    public ControllerBuilder setControllerCouldBeExecuted(Predicate<Update> controllerShouldBeExecuted) {
        this.controllerShouldBeExecuted = controllerShouldBeExecuted;
        return this;
    }

    public CallbackRequest getCallbackConfiguration() {
        return callbackConfiguration;
    }

    public ControllerBuilder setCallbackConfiguration( CallbackRequest callbackConfiguration ) {
        this.callbackConfiguration = callbackConfiguration;
        return this;
    }

    public InlineKeyboardMarkup getInlineKeyboardMarkup() {
        return inlineKeyboardMarkup;
    }

    public ControllerBuilder setInlineKeyboardMarkup( InlineKeyboardMarkup inlineKeyboardMarkup ) {
        this.inlineKeyboardMarkup = inlineKeyboardMarkup;
        return this;
    }

    public String getControllerType() {
        return controllerType;
    }

    public void setControllerType( String controllerType ) {
        this.controllerType = controllerType;
    }
}
