package io.github.butkoprojects.bots.api;

/**
 * Interface that defines bot session interface.
 */
public interface BotSession {

    void addToSession( String key, String value );

    String getFromSession( String key );
    
}
