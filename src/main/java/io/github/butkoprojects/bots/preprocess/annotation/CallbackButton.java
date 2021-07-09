package io.github.butkoprojects.bots.preprocess.annotation;

import java.lang.annotation.*;

@Target( {ElementType.METHOD, ElementType.ANNOTATION_TYPE} )
@Retention( RetentionPolicy.RUNTIME )
@Repeatable( CallbackButtonRow.class )
public @interface CallbackButton {

    String name() default "";

    String call() default "";

    String data() default "";

}
