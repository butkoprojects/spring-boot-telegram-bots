package io.github.butkoprojects.bots.api.annotation;

import io.github.butkoprojects.bots.api.BotRequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( {ElementType.METHOD} )
@Retention( RetentionPolicy.RUNTIME )
public @interface BotRequestMapping {

    String[] value() default {};

    BotRequestMethod[] method() default { BotRequestMethod.MSG };

}
