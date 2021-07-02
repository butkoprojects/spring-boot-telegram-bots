package io.github.butkoprojects.bots.preprocess.factory;

import java.util.HashMap;
import java.util.Map;

public class MethodInvocationContext {

    private final Map<String, String> params = new HashMap<>();

    private Object resultObject;

    public Map<String, String> getParams() {
        return params;
    }

    public Object getResultObject() {
        return resultObject;
    }

    public void setResultObject( Object resultObject ) {
        this.resultObject = resultObject;
    }
}
