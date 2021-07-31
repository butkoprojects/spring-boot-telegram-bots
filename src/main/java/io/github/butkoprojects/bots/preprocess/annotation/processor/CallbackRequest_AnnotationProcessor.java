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
import java.util.function.Consumer;
import java.util.function.Function;

@Component
@Order(8)
public class CallbackRequest_AnnotationProcessor
        extends BaseAnnotationProcessor
        implements AnnotationProcessor<CallbackRequest> {

    @Override
    public void process(CallbackRequest annotation,
                        ControllerBuilder builder) {

        builder.setPath(annotation.value());
        builder.setControllerCouldBeExecuted(BotControllerTypeEnum.CALLBACK.updatePredicate);
        builder.setControllerType(BotControllerTypeEnum.CALLBACK.type);
        builder.setCallbackConfiguration(annotation);

        Consumer<Update> updateConsumer = update -> {
            if (builder.getInlineKeyboardMarkup() != null) {
                editMessage(builder, update);
            }
            if ( isReturnTypeIsString(builder.getMethod()) ) {
                processCallbackWithStringReturnType(builder, update);
            } else if ( returnTypeIsList(builder.getMethod()) ) {
                processList(builder, update);
            } else {
                processSingle(builder, update);
            }
        };
        builder.addProcessConsumer(updateConsumer);
    }

    private void editMessage(ControllerBuilder builder,
                             Update update) {
        if (builder.getMethod().getReturnType().equals(Void.TYPE)) {
            editMessageMarkup(builder, update, builder.getResultList());
        } else {
            editMessageTextAndMarkup(builder, update, builder.getResultList());
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
                                                     Update update) {
        MethodInvocationContext context = processMethodInvocation(builder, update);
        if (context.getResultObject() != null) {
            builder.getResultList().add(
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
