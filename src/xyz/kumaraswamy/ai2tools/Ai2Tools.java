package xyz.kumaraswamy.ai2tools;

import android.util.Log;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.errors.IllegalArgumentError;
import gnu.expr.Language;
import gnu.kawa.functions.Apply;
import gnu.kawa.functions.ApplyToArgs;
import gnu.lists.LList;
import gnu.mapping.Environment;
import gnu.mapping.ProcedureN;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Symbol;

import java.lang.reflect.Field;

public class Ai2Tools extends AndroidNonvisibleComponent {

    private static final String TAG = "Ai2Tools";

    public Ai2Tools(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "Initializes")
    public void InitializeI() throws Exception {
        Class<?> clazz = Class.forName("kawa.standard.Scheme");
        Field field = clazz.getField("apply");

        Apply apply = (Apply) field.get(field);

        Field applyToArgsF = apply.getClass().getDeclaredField("applyToArgs");
        applyToArgsF.setAccessible(true);

        ApplyToArgs applyToArgs = (ApplyToArgs) applyToArgsF.get(apply);

        Field setApplyToArgs = apply.getClass().getDeclaredField("applyToArgs");
        setApplyToArgs.setAccessible(true);

        Language language = (Language) clazz.getField("instance").get(null);
        setApplyToArgs.set(apply, new ApplyToArgsI("apply-to-args", language, applyToArgs));

        Field env = form.getClass().getField("form$Mnenvironment");
        Environment environment = (Environment) env.get(form);

        Field envSetI = form.getClass().getField("form$Mnenvironment");
        envSetI.set(form, new Env(environment, this));
    }

    public void doEventCall(final String procedureName, Object[] arguments) throws Throwable {
        ProcedureN fun = lookupProcedureInForm(procedureName);
        if (fun == null) {
            throw new IllegalArgumentError("Procedure not found");
        }
        fun.applyN(arguments);
    }

    /**
     * This method is taken from Procedures
     * extension written by Ewpatton
     */

    private ProcedureN lookupProcedureInForm(String procedureName) {
        try {
            Field globalVarEnvironment = form.getClass().getField("global$Mnvars$Mnto$Mncreate");
            LList vars = (LList) globalVarEnvironment.get(form);
            Symbol procSym = new SimpleSymbol("p$" + procedureName);
            Object result = null;
            for (Object pair : vars) {
                if (!LList.Empty.equals(pair)) {
                    LList asPair = (LList) pair;
                    if (((Symbol) asPair.get(0)).getName().equals(procSym.getName())) {
                        result = asPair.get(1);
                        break;
                    }
                }
            }
            if (result instanceof ProcedureN) {
                // The def syntax wraps the function definition in an additional lambda, which we evaluate
                // here so that the return value of this is the lambda implementing the blocks logic.
                // See runtime.scm#665
                return (ProcedureN) ((ProcedureN) result).apply0();
            } else {
                Log.e(TAG, "Wanted a procedure, but got a " +
                        (result == null ? "null" : result.getClass().toString()));
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
