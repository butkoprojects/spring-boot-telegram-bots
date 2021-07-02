package io.github.butkoprojects.bots.preprocess.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( {ElementType.METHOD, ElementType.ANNOTATION_TYPE} )
@Retention( RetentionPolicy.RUNTIME )
public @interface InlineRequest {

    String value() default "default";

    int cacheTime() default 0;
}
