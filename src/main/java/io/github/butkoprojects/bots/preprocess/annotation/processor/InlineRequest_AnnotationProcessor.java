package io.github.butkoprojects.bots.preprocess.annotation.processor;

import io.github.butkoprojects.bots.preprocess.annotation.InlineRequest;
import io.github.butkoprojects.bots.preprocess.controller.builder.ControllerBuilder;
import io.github.butkoprojects.bots.preprocess.controller.type.BotControllerTypeEnum;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
@Order( 9 )
public class InlineRequest_AnnotationProcessor
        extends BaseAnnotationProcessor
        implements AnnotationProcessor<InlineRequest> {

    @Override
    public void process( InlineRequest annotation, ControllerBuilder builder ) {
        builder.setPath( annotation.value() );
        builder.setControllerCouldBeExecuted( BotControllerTypeEnum.INLINE.updatePredicate );
        builder.setControllerType( BotControllerTypeEnum.INLINE.type );

        Consumer<Update> processFunction = update -> {
            if ( returnTypeIsList(builder.getMethod())) {
                processList(builder, update);
            } else {
                processSingle(builder, update);
            }
        };
        builder.addProcessConsumer( processFunction );
    }
}
