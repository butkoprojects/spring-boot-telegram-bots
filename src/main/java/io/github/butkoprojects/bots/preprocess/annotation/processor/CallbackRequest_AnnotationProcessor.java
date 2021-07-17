package io.github.butkoprojects.bots.preprocess.annotation.processor;

import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.preprocess.annotation.CallbackRequest;
import io.github.butkoprojects.bots.preprocess.controller.type.BotControllerTypeEnum;
import io.github.butkoprojects.bots.preprocess.factory.MethodInvocationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;
import java.util.function.Function;

@Component
@Order(8)
public class CallbackRequest_AnnotationProcessor
        extends BaseAnnotationProcessor
        implements AnnotationProcessor<CallbackRequest> {

    @Override
    public Class<CallbackRequest> getAnnotationClass() {
        return CallbackRequest.class;
    }

    @Override
    public void process(CallbackRequest annotation,
                        ControllerBuilder builder) {

        builder.setPath(annotation.value());
        builder.setControllerCouldBeExecuted(BotControllerTypeEnum.CALLBACK.updatePredicate);
        builder.setControllerType(BotControllerTypeEnum.CALLBACK.type);
        builder.setCallbackConfiguration(annotation);

        Function<Update, List<Object>> processFunction = update -> {
            List<Object> resultList = new ArrayList<>();
            if (builder.getInlineKeyboardMarkup() != null) {
                editMessage(builder, update, resultList);
            }
            if ( isReturnTypeIsString(builder.getMethod()) ) {
                processCallbackWithStringReturnType(builder, update, resultList);
            } else if ( returnTypeIsList(builder.getMethod()) ) {
                processList(builder, update, resultList);
            } else {
                processSingle(builder, update, resultList);
            }
            return resultList;
        };
        builder.setProcessFunction(processFunction);
    }

    private void editMessage(ControllerBuilder builder,
                             Update update,
                             List<Object> resultList) {
        if (builder.getMethod().getReturnType().equals(Void.TYPE)) {
            editMessageMarkup(builder, update, resultList);
        } else {
            editMessageTextAndMarkup(builder, update, resultList);
        }
    }

    private void editMessageMarkup(ControllerBuilder builder,
                                   Update update,
                                   List<Object> resultList) {
        MethodInvocationContext context = processMethodInvocation(builder, update);
        builder.updateInlineKeyboardMarkupWithValues(context.getParams());

        EditMessageReplyMarkup.EditMessageReplyMarkupBuilder editMessageReplyMarkupBuilder =
                EditMessageReplyMarkup.builder()
                        .inlineMessageId(update.getCallbackQuery().getInlineMessageId())
                        .chatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()))
                        .messageId(update.getCallbackQuery().getMessage().getMessageId());
        if (builder.getInlineKeyboardMarkup() != null) {
            editMessageReplyMarkupBuilder.replyMarkup(builder.getInlineKeyboardMarkup());
        }
        resultList.add(editMessageReplyMarkupBuilder.build());
    }

    private void editMessageTextAndMarkup(ControllerBuilder builder,
                                          Update update,
                                          List<Object> resultList) {
        MethodInvocationContext context = processMethodInvocation(builder, update);
        builder.updateInlineKeyboardMarkupWithValues(context.getParams());

        if (context.getResultObject() instanceof String) {
            EditMessageText.EditMessageTextBuilder editMessageTextBuilder =
                    EditMessageText.builder()
                            .text(String.valueOf(context.getResultObject()))
                            .chatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()))
                            .messageId(update.getCallbackQuery().getMessage().getMessageId());
            if (builder.getInlineKeyboardMarkup() != null) {
                editMessageTextBuilder.replyMarkup(builder.getInlineKeyboardMarkup());
            }
            resultList.add(editMessageTextBuilder.build());
        }
    }

    private void processCallbackWithStringReturnType(ControllerBuilder builder,
                                                     Update update,
                                                     List<Object> resultList) {
        MethodInvocationContext context = processMethodInvocation(builder, update);
        if (context.getResultObject() != null) {
            resultList.add(
                    AnswerCallbackQuery.builder()
                            .callbackQueryId(update.getCallbackQuery().getId())
                            .text(String.valueOf(context.getResultObject()))
                            .showAlert(builder.getCallbackConfiguration().showAlert())
                            .cacheTime(builder.getCallbackConfiguration().cacheTime())
                            .build()
            );
        }
    }
}
