package io.github.butkoprojects.bots;

import io.github.butkoprojects.bots.preprocess.TelegramUpdateHandlerBeanPostProcessor;
import io.github.butkoprojects.bots.handler.DefaultBotRequestHandler;
import io.github.butkoprojects.bots.preprocess.container.DefaultBotMethodContainer;
import io.github.butkoprojects.bots.preprocess.controller.builder.DefaultControllerBuilder;
import io.github.butkoprojects.bots.preprocess.controller.type.CallbackBotControllerType;
import io.github.butkoprojects.bots.preprocess.controller.type.InlineBotControllerType;
import io.github.butkoprojects.bots.preprocess.controller.type.MessageBotControllerType;
import io.github.butkoprojects.bots.preprocess.factory.DefaultControllerFactory;
import io.github.butkoprojects.bots.preprocess.factory.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook.SetWebhookBuilder;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

/**
 * Auto configuration for telegram bots to automatically start bots that are configured as beans.
 */
@Configuration
@Slf4j
@ConditionalOnClass(TelegramBotsApi.class)
@RequiredArgsConstructor
@Import({
        TelegramProperties.class,
        TelegramApiFactory.class,
        SetWebhookBuilderFactory.class,
        DefaultBotMethodContainer.class,
        DefaultBotRequestHandler.class,
        DefaultControllerFactory.class,
        DefaultControllerBuilder.class,
        MessageBotControllerType.class,
        CallbackBotControllerType.class,
        InlineBotControllerType.class,
        MessageRequest_AnnotationProcessor.class,
        CallbackKeyboard_AnnotationProcessor.class,
        CallbackButtonRow_AnnotationProcessor.class,
        CallbackButton_AnnotationProcessor.class,
        Keyboard_AnnotationProcessor.class,
        KeyBoardRow_AnnotationProcessor.class,
        KeyBoardButton_AnnotationProcessor.class,
        CallbackRequest_AnnotationProcessor.class,
        InlineRequest_AnnotationProcessor.class,
        TelegramUpdateHandlerBeanPostProcessor.class
})
public class TelegramBotAutoConfiguration {

    private final List<BotSession> sessions = new ArrayList<>();
    private final List<LongPollingBot> pollingBots;
    private final List<TelegramWebhookBot> webHookBots;
    private final TelegramApiFactory apiFactory;
    private final SetWebhookBuilderFactory setWebhookFactory;

    /**
     * Throws.
     * @throws TelegramApiException throws.
     */
    @PostConstruct
    public void start() throws TelegramApiException {
        log.info("Starting auto config for telegram bots");
        TelegramBotsApi api = telegramBotsApi();
        pollingBots.forEach(bot -> {
            try {
                log.info("Registering polling bot: {}", bot.getBotUsername());
                sessions.add(api.registerBot(bot));
            } catch (TelegramApiException e) {
                log.error("Failed to register bot {} due to error", bot.getBotUsername(), e);
            }
        });
        webHookBots.forEach(bot -> {
            try {
                log.info("Registering web hook bot: {}", bot.getBotUsername());
                SetWebhookBuilder webhookBuilder = setWebhookFactory.create();
                if (bot instanceof CustomizableTelegramWebhookBot) {
                    ((CustomizableTelegramWebhookBot) bot).customizeWebHook(webhookBuilder);
                }
                api.registerBot(bot, webhookBuilder.build());
            } catch (TelegramApiException e) {
                log.error("Failed to register bot {} due to error", bot.getBotUsername(), e);
            }
        });
    }

    /**
     * Get API object depending on configured properties.
     * If no properties are configured, the API object won't support webhooks.
     *
     * @return api object
     * @throws TelegramApiException in case the creation of the API object fails
     */
    @Bean
    @ConditionalOnMissingBean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        return apiFactory.create();
    }

    /**
     *
     */
    @PreDestroy
    public void stop() {
        sessions.forEach(session -> {
            if (session != null) {
                session.stop();
            }
        });
    }

//    @Bean
//    public BotApiMethodContainer botApiMethodContainer() {
//        return new BotApiMethodContainerImpl();
//    }
//
//    @Bean
//    public BotApiMethodContainerHandler botApiMethodContainerHandler() {
//        return new BotApiMethodContainerHandlerImpl();
//    }
//
//    @Bean
//    public BotApiMethodControllerConverter botApiMethodControllerConverter() {
//        return new BotApiMethodControllerConverterImpl();
//    }
//
//    @Bean
//    public TelegramUpdateHandlerBeanPostProcessor telegramUpdateHandlerBeanPostProcessor() {
//        return new TelegramUpdateHandlerBeanPostProcessor();
//    }
}
