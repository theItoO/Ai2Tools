package xyz.kumaraswamy.ai2tools;

import android.util.Log;
import gnu.mapping.Environment;
import gnu.mapping.Location;
import gnu.mapping.LocationEnumeration;
import gnu.mapping.NamedLocation;
import gnu.mapping.Symbol;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Env extends Environment {

    private final Environment environment;
    private final Ai2Tools ai2Tools;
    private static final String TAG = "Ai2Tools";

    public Env(Environment environment, Ai2Tools ai2Tools) {
        this.environment = environment;
        this.ai2Tools = ai2Tools;
    }

    @Override
    public Symbol getSymbol(String name) {
        Symbol symbol = defaultNamespace().getSymbol(name);
        Log.d("EventsI", "symbol called: " + symbol);
        return symbol;
    }

    @Override
    public NamedLocation lookup(Symbol symbol, Object o, int i) {
        return environment.lookup(symbol, o, i);
    }

    public Object get(Symbol key, Object property, Object defaultValue) {
        Location loc = lookup(key, property);
        Object object = loc == null ? defaultValue : loc.get(defaultValue);

        Log.d(TAG, "get() " + key + " result " + object);
        String name = key.getName();
        if (name.startsWith("any$")) {
            String removeI = name.substring(4);
            Log.d(TAG, "removedI " + removeI);

            String eventName = removeI.substring(removeI.indexOf('$') + 1);
            Log.d(TAG, "eventName: " + eventName);

            return new ApplyMethod(eventName, ai2Tools);
        }

        return environment.get(key, property, defaultValue);
    }

    @Override
    public boolean isBound(Symbol key, Object property) {
        Log.d(TAG, "isBoundI: " + key);
        return key.getName().startsWith("any$") || environment.isBound(key, property);
    }

    @Override
    public NamedLocation getLocation(Symbol symbol, Object o, int i, boolean b) {
        return environment.getLocation(symbol, o, i, b);
    }

    @Override
    public void define(Symbol symbol, Object o, Object o1) {
        environment.define(symbol, o, o1);
    }

    @Override
    public LocationEnumeration enumerateLocations() {
        return environment.enumerateLocations();
    }

    @Override
    public LocationEnumeration enumerateAllLocations() {
        return environment.enumerateAllLocations();
    }

    @Override
    protected boolean hasMoreElements(LocationEnumeration locationEnumeration) {
        try {
            Method method =
                    environment.getClass().getMethod(
                            "hasMoreElements", LocationEnumeration.class);
            method.setAccessible(true);
            return (boolean) method.invoke(environment, locationEnumeration);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public NamedLocation addLocation(Symbol symbol, Object o, Location location) {
        return environment.addLocation(symbol, o, location);
    }
}
