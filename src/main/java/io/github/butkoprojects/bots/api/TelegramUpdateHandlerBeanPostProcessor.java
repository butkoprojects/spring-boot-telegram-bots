package io.github.butkoprojects.bots.api;

import io.github.butkoprojects.bots.api.annotation.*;
import io.github.butkoprojects.bots.api.method.controller.BotApiMethodConditionController;
import io.github.butkoprojects.bots.api.impl.BotApiMethodContainerImpl;
import io.github.butkoprojects.bots.api.impl.DefaultBotRequestMappingCondition;
import io.github.butkoprojects.bots.api.method.controller.BotApiMethodController;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class TelegramUpdateHandlerBeanPostProcessor implements BeanPostProcessor {

    private final List<Class<? extends Annotation>> controller_annotations = Arrays.asList(
            MessageRequest.class, CallbackRequest.class
    );

    @Autowired
    private BotApiMethodContainerImpl container;

    private final Map<String, Class> botControllerMap = new HashMap<>();

    private final Map<Class, Object> conditionMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName ) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if ( beanClass.isAnnotationPresent( BotController.class ) ) {
            botControllerMap.put( beanName, beanClass );
        } else if ( beanClass.isAnnotationPresent( BotControllerCondition.class ) ) {
            conditionMap.put( beanClass, bean );
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization( Object bean, String beanName ) throws BeansException {
        if ( !botControllerMap.containsKey( beanName ) ) return bean;

        Class original = botControllerMap.get( beanName );
        Arrays.stream( original.getDeclaredMethods() )
                .filter( method -> controller_annotations.stream().anyMatch( method::isAnnotationPresent ) )
                .forEach( ( Method method ) -> generateController( bean, method ) );
        Arrays.stream( original.getDeclaredMethods() )
                .filter( method -> method.isAnnotationPresent( BotRequestMappingConditional.class ) )
                .forEach( ( Method method ) -> generateConditionalController( bean, method ) );
        return bean;
    }

    private void generateConditionalController( Object bean, Method method ) {
        BotRequestMappingConditional botRequestCondition = method.getAnnotation( BotRequestMappingConditional.class );
        Class conditionClass = botRequestCondition.value();
        BotRequestMappingCondition conditionObject = ( BotRequestMappingCondition ) conditionMap.getOrDefault( conditionClass, new DefaultBotRequestMappingCondition() );

        BotApiMethodConditionController controller = new BotApiMethodConditionController( bean, method, conditionObject );
        container.addConditionController( controller );
    }

    private void generateController( Object bean, Method method ) {
        BotController botController = bean.getClass().getAnnotation( BotController.class );

        if ( method.isAnnotationPresent( MessageRequest.class ) ) {
            createMessageController( bean, method, botController );
        }
        if ( method.isAnnotationPresent( CallbackRequest.class ) ) {
            createCallbackController( bean, method, botController );
        }
    }

    private void createMessageController( Object bean, Method method, BotController botController ) {
        MessageRequest messageRequest = method.getAnnotation( MessageRequest.class );

        String path = ( botController.value().length != 0 ? botController.value()[0] : "" )
                + messageRequest.value();

        Predicate<Update> updatePredicate = update -> update != null && update.hasMessage() && update.getMessage().hasText();

        BotApiMethodController controller = BotApiMethodController.builder()
                .setWorkingBean( bean )
                .setMethod( method )
                .setPredicate( updatePredicate )
                .messageRequest()
                .build();
        container.addBotController( path, controller );
    }

    private void createCallbackController( Object bean, Method method, BotController botController ) {
        CallbackRequest callbackRequest = method.getAnnotation( CallbackRequest.class );

        String path = ( botController.value().length != 0 ? botController.value()[0] : "" )
                + callbackRequest.value();

        Predicate<Update> updatePredicate = update ->
                update != null &&
                update.hasCallbackQuery() &&
                update.getCallbackQuery().getData() != null;

        BotApiMethodController controller = BotApiMethodController.builder()
                .setWorkingBean( bean )
                .setMethod( method )
                .setPredicate( updatePredicate )
                .callbackRequest( callbackRequest )
                .build();
        container.addBotController( path, controller );
    }
}
