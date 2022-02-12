package xyz.kumaraswamy.ai2tools;

import android.util.Log;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.util.YailList;
import gnu.expr.Language;
import gnu.expr.ModuleMethod;
import gnu.kawa.functions.ApplyToArgs;

import java.util.Arrays;

public class ApplyToArgsI extends gnu.kawa.functions.ApplyToArgs {

    private static final String TAG = "Ai2Tools";
    private final ApplyToArgs apply;

    public ApplyToArgsI(String name, Language language, ApplyToArgs apply) {
        super(name, language);
        this.apply = apply;
    }

    @Override
    public Object applyN(Object[] args) throws Throwable {
        Log.d(TAG, "applyN: " + Arrays.toString(args));
        if (args.length == 1) {
            return apply.applyN(args);
        }

        if (args[0] instanceof ModuleMethod) {
            ModuleMethod applyMethodI = (ModuleMethod) args[0];

            if (applyMethodI instanceof ApplyMethod) {
                ApplyMethod applyMethod = (ApplyMethod) applyMethodI;
                Component component = (Component) args[1];

                YailList eventArgs = YailList.makeList(
                        Arrays.asList(args)
                                .subList(3, args.length));
                Object[] arguments = {component, applyMethod.getEventName(), eventArgs};

                Log.d(TAG, "Event raised of name '" + applyMethod.getEventName() +
                        "' of component " + component + " and args " + arguments[2]);

                applyMethod.getAi2Tools().doEventCall("event", arguments);
            }
        }

        return apply.applyN(args);
    }
}
