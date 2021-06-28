package io.github.butkoprojects.bots.util.annotation;

import java.lang.annotation.*;

@Target( {ElementType.METHOD, ElementType.ANNOTATION_TYPE} )
@Retention( RetentionPolicy.RUNTIME )
@Repeatable( CallbackKeyboard.class )
public @interface CallbackButtonRow {
    CallbackButton[] value();
}
