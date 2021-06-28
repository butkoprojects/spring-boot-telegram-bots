package io.github.butkoprojects.bots.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( {ElementType.METHOD, ElementType.ANNOTATION_TYPE} )
@Retention( RetentionPolicy.RUNTIME )
public @interface CallbackRequest {

    String value() default "";

    boolean showAlert() default false;

    int cacheTime() default 0;
}
