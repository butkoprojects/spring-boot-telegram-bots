package io.github.butkoprojects.bots.preprocess.container;

import io.github.butkoprojects.bots.util.exception.BotApiMethodContainerException;
import io.github.butkoprojects.bots.preprocess.controller.BotApiMethodController;
import org.glassfish.grizzly.utils.Pair;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DefaultBotMethodContainer implements BotMethodContainer {

    private final Map<String, BotApiMethodController> controllerMap = new HashMap<>();

    @Override
    public void addBotController( String path,
                                  String controllerType,
                                  BotApiMethodController controller ) {
        final Pair<String, String> key = new Pair<>( path, controllerType );
        final String stringKey = key.toString();
        if ( controllerMap.containsKey( stringKey ) ) throw new BotApiMethodContainerException( "path " + path + " already exists" );
        controllerMap.put( stringKey, controller );
    }

    @Override
    public BotApiMethodController getBotApiMethodController( String path,
                                                             String controllerType ) {
        System.out.println();
        return controllerMap.get( new Pair<>( path, controllerType ).toString() );
    }
}
