package com.ichi2.libanki.hooks;

import com.ichi2.libanki.LaTeX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hooks {
    private Map<String, List<Hook>> hooks;
    
    public Hooks() {
        hooks = new HashMap<String, List<Hook>>();
        // Add default hooks
        new FuriganaFilters().install(this);
        new LaTeX().installHook(this);
    }
    
    /**
     * Add a function to hook. Ignore if already on hook.
     * @param hook The name of the hook.
     * @param func A class implements interface Hook and contains the function to add.
     */
    public void addHook(String hook, Hook func) {
        if (!hooks.containsKey(hook) || hooks.get(hook) == null) {
            hooks.put(hook, new ArrayList<Hook>());
        }
        boolean found = false;
        for (Hook h : hooks.get(hook)) {
            if (func.equals(h)) {
                found = true;
                break;
            }
        }
        if (!found) {
            hooks.get(hook).add(func);
        }
    }

    /**
     * Remove a function if is on hook.
     * @param hook The name of the hook.
     * @param func A class implements interface Hook and contains the function to remove.
     */
    public void remHook(String hook, Hook func) {
        if (hooks.containsKey(hook) && hooks.get(hook) != null) {
            for (Hook h : hooks.get(hook)) {
                if (func.equals(h)) {
                    hooks.get(hook).remove(h);
                    break;
                }
            }
        }
    }
    
    /**
     * Run all functions on hook.
     * @param hook The name of the hook.
     * @param args Variable arguments to be passed to the method runHook of each function on this hook. 
     */
    public void runHook(String hook, Object...args) {
        List<Hook> _hook = hooks.get(hook);
        if (_hook != null) {
            for (Hook func : _hook) {
                func.runHook(args);
            }
        }
    }
    
    /**
     * Apply all functions on hook to arg and return the result.
     * @param hook The name of the hook.
     * @param arg The input to the filter on hook.
     * @param args Variable arguments to be passed to the method runHook of each function on this hook. 
     */
    public Object runFilter(String hook, Object arg, Object...args) {
        List<Hook> _hook = hooks.get(hook);
        if (_hook != null) {
            for (Hook func : _hook) {
                arg = func.runFilter(arg, args);
            }
        }
        return arg;
    }
}

