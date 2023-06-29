package com.astune.mcenter.object;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class Hook {
    private int stateId;

    private Object[] parameters;

    private Method method;

    private Object parent;

    public Hook(Method method, Object parent, int stateId, Object... parameters){
        this.method = method;
        this.parameters = parameters;
        this.parent = parent;
        this.stateId = stateId;
    }

    public Hook(Method method, Object parent, int stateId){
        this.method = method;
        this.parameters = null;
        this.parent = parent;
        this.stateId = stateId;
    }

    public int getStateId() {
        return stateId;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public Method getMethod() {
        return method;
    }

    public Object getParent() {
        return parent;
    }
}
