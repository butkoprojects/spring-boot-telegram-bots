package io.github.butkoprojects.bots.util.session;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotSessionImpl implements BotSession {

    private static final Map<String, String> storage = new HashMap<>();

    @Override
    public void addToSession( String key, String value ) {
        storage.put( key, value );
    }

    @Override
    public String getFromSession(String key) {
        return storage.get( key );
    }
}
