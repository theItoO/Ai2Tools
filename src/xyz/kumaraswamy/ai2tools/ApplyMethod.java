package xyz.kumaraswamy.ai2tools;

import gnu.expr.ModuleMethod;

public class ApplyMethod extends ModuleMethod {

    private final String eventName;
    private final Ai2Tools ai2Tools;

    public ApplyMethod(String eventName, Ai2Tools ai2Tools) {
        super(null, -1, "", 0);
        this.eventName = eventName;
        this.ai2Tools = ai2Tools;
    }

    public Ai2Tools getAi2Tools() {
        return ai2Tools;
    }

    public String getEventName() {
        return eventName;
    }

    @Override
    public Object apply0() {
        return null;
    }

    @Override
    public Object apply1(Object arg1) {
        return null;
    }

    @Override
    public Object apply2(Object arg1, Object arg2) {
        return null;
    }

    @Override
    public Object apply3(Object arg1, Object arg2, Object arg3) {
        return null;
    }

    @Override
    public Object apply4(Object arg1, Object arg2, Object arg3, Object arg4) {
        return null;
    }

    @Override
    public Object applyN(Object[] args) {
        return null;
    }
}
