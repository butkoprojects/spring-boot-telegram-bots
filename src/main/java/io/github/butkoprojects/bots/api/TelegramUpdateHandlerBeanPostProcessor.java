package io.github.butkoprojects.bots.api;

import io.github.butkoprojects.bots.api.annotation.BotController;
import io.github.butkoprojects.bots.api.annotation.BotControllerCondition;
import io.github.butkoprojects.bots.api.annotation.BotRequestMapping;
import io.github.butkoprojects.bots.api.annotation.BotRequestMappingConditional;
import io.github.butkoprojects.bots.api.method.controller.BotApiMethodConditionController;
import io.github.butkoprojects.bots.api.impl.BotApiMethodContainerImpl;
import io.github.butkoprojects.bots.api.impl.DefaultBotRequestMappingCondition;
import io.github.butkoprojects.bots.api.method.controller.BotApiMethodController;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class TelegramUpdateHandlerBeanPostProcessor implements BeanPostProcessor {

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
    public Object postProcessAfterInitialization(Object bean, String beanName ) throws BeansException {
        if( !botControllerMap.containsKey( beanName ) ) return bean;

        Class original = botControllerMap.get( beanName );
        Arrays.stream( original.getDeclaredMethods() )
                .filter( method -> method.isAnnotationPresent( BotRequestMapping.class ) )
                .forEach( ( Method method ) -> generateController( bean, method ) );
        Arrays.stream( original.getDeclaredMethods() )
                .filter( method -> method.isAnnotationPresent( BotRequestMappingConditional.class ) )
                .forEach( ( Method method ) -> generateConditionalController( bean, method ) );
        return bean;
    }

    private void generateConditionalController(Object bean, Method method ) {
        BotRequestMappingConditional botRequestCondition = method.getAnnotation( BotRequestMappingConditional.class );
        Class conditionClass = botRequestCondition.value();
        BotRequestMappingCondition conditionObject = ( BotRequestMappingCondition ) conditionMap.getOrDefault( conditionClass, new DefaultBotRequestMappingCondition() );

        BotApiMethodConditionController controller = new BotApiMethodConditionController( bean, method, conditionObject );
        container.addConditionController( controller );
    }

    private void generateController(Object bean, Method method ) {
        BotController botController = bean.getClass().getAnnotation( BotController.class );
        BotRequestMapping botRequestMapping = method.getAnnotation( BotRequestMapping.class );

        String path = ( botController.value().length != 0 ? botController.value()[0] : "" )
                + ( botRequestMapping.value().length != 0 ? botRequestMapping.value()[0] : "" );

        BotApiMethodController controller = BotApiMethodController.builder()
                .setWorkingBean( bean )
                .setMethod( method )
                .setPredicate( botRequestMapping.method()[0].getPredicate() )
                .build();
        container.addBotController( path, controller );
    }
}
