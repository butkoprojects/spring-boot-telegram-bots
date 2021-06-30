package io.github.butkoprojects.bots.preprocess.annotation;

import java.lang.annotation.*;

@Target( {ElementType.METHOD, ElementType.ANNOTATION_TYPE} )
@Retention( RetentionPolicy.RUNTIME )
@Repeatable( KeyBoardRow.class )
public @interface KeyBoardButton {
    String value();

    boolean requestContact() default false;

    boolean requestLocation() default false;
}
