package io.github.butkoprojects.bots.preprocess;

import io.github.butkoprojects.bots.preprocess.factory.ControllerFactory;
import io.github.butkoprojects.bots.util.annotation.BotController;
import io.github.butkoprojects.bots.util.annotation.CallbackRequest;
import io.github.butkoprojects.bots.util.annotation.MessageRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TelegramUpdateHandlerBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private ControllerFactory factory;

    private final List<Class<? extends Annotation>> controller_annotations = Arrays.asList(
            MessageRequest.class, CallbackRequest.class
    );

    private final Map<String, Class> botControllerMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName ) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if ( beanClass.isAnnotationPresent( BotController.class ) ) {
            botControllerMap.put( beanName, beanClass );
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization( Object bean, String beanName ) throws BeansException {
        if ( !botControllerMap.containsKey( beanName ) ) return bean;

        Class original = botControllerMap.get( beanName );
        Arrays.stream( original.getDeclaredMethods() )
                .filter( method -> controller_annotations.stream().anyMatch( method::isAnnotationPresent ) )
                .forEach( ( Method method ) -> factory.generateController( bean, method ) );
        return bean;
    }
}
