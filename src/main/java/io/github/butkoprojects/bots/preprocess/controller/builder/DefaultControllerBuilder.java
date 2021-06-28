package io.github.butkoprojects.bots.preprocess.controller.builder;

import org.springframework.stereotype.Component;

@Component
public class DefaultControllerBuilder extends ControllerBuilder {

    @Override
    public ControllerBuilder instance() {
        return new DefaultControllerBuilder();
    }
}
