package io.github.butkoprojects.bots.api.realization;

import io.github.butkoprojects.bots.api.exception.BotApiMethodContainerException;
import io.github.butkoprojects.bots.api.BotApiMethodContainer;
import io.github.butkoprojects.bots.api.method.controller.BotApiMethodConditionController;
import io.github.butkoprojects.bots.api.method.controller.BotApiMethodController;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotApiMethodContainerImpl implements BotApiMethodContainer {

    private final Map<String, BotApiMethodController> controllerMap = new HashMap<>();

    private final List<BotApiMethodConditionController> controllerList = new ArrayList<>();

    @Override
    public List<BotApiMethodConditionController> getAllConditionControllers() {
        return controllerList;
    }

    @Override
    public void addConditionController(BotApiMethodConditionController controller) {
        controllerList.add( controller );
    }

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
