package io.github.butkoprojects.bots.api.annotation;

import java.lang.annotation.*;

@Target( {ElementType.METHOD, ElementType.ANNOTATION_TYPE} )
@Retention( RetentionPolicy.RUNTIME )
@Repeatable( Keyboard.class )
public @interface KeyBoardRow {
    KeyBoardButton[] value();
}
