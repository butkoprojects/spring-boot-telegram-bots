package io.github.butkoprojects.bots.constructor.box.annotation;

import io.github.butkoprojects.bots.constructor.box.BotRequestMappingCondition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( {ElementType.METHOD} )
@Retention( RetentionPolicy.RUNTIME )
public @interface BotRequestMappingConditional {

    Class<? extends BotRequestMappingCondition> value();

}
