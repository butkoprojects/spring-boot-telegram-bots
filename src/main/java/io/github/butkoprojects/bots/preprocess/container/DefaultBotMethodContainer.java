package io.github.butkoprojects.bots.preprocess.container;

import io.github.butkoprojects.bots.util.exception.BotApiMethodContainerException;
import io.github.butkoprojects.bots.preprocess.controller.BotApiMethodController;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DefaultBotMethodContainer implements BotMethodContainer {

    private final Map<String, BotApiMethodController> controllerMap = new HashMap<>();

    @Override
    public void addBotController(String path, BotApiMethodController controller ) {
        if ( controllerMap.containsKey( path ) ) throw new BotApiMethodContainerException( "path " + path + " already exists" );
        controllerMap.put( path, controller );
    }

    @Override
    public BotApiMethodController getBotApiMethodController( String path ) {
        return controllerMap.get( path );
    }
}
