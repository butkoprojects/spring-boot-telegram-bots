package io.github.butkoprojects.bots.util.session;

/**
 * Interface that defines bot session interface.
 */
public interface BotSession {

    void addToSession( String key, String value );

    String getFromSession( String key );
    
}
