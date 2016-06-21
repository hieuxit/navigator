package io.fruitful.navigator.internal;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by hieuxit on 6/1/16.
 */

public class NavigatorManager {
    private static final String TAG = NavigatorManager.class.getSimpleName();
    private static final String HAS_NAVIGATOR_BINDER = "$HasNavigatorBinder";
    private static final String BIND_METHOD_NAME = "bind";
    private static final String ANDROID_PREFIX = "android.";
    private static final String JAVA_PREFIX = "java.";

    public static void emitBindHasNavigator(Object target, NavigatorOwnerKind kind) {

        Class<?> targetClass = target.getClass();
        Class<?> binderClass = findHasNavigatorBinder(targetClass);
        if (binderClass == null) {
            // if class does not have @HasNavigator do nothing
            return;
        }
        try {
            Method hasNavigatorMethod = binderClass.getMethod(BIND_METHOD_NAME, targetClass, NavigatorOwnerKind.class);
            if (hasNavigatorMethod != null) {
                hasNavigatorMethod.invoke(null, target, kind);
            }
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("apt navigator error. Check " + targetClass.getName() + "$HasNavigatorBinder???");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Class<?> findHasNavigatorBinder(Class<?> cls) {
        String clsName = cls.getName();
        Class<?> binder;
        if (clsName.startsWith(ANDROID_PREFIX) || clsName.startsWith(JAVA_PREFIX)) {
            Log.d(TAG, "MISS: Reached framework class. Abandoning search.");
            return null;
        }
        try {
            binder = Class.forName(getHasNavigatorBinderClassName(clsName));
            Log.d(TAG, "HIT: Loaded @HasNavigator binder class.");
        } catch (ClassNotFoundException e) {
            Log.d(TAG, "Not found. Trying superclass " + cls.getSuperclass().getName());
            binder = findHasNavigatorBinder(cls.getSuperclass());
        }
        return binder;
    }

    private static String getHasNavigatorBinderClassName(String clsName) {
        return clsName + HAS_NAVIGATOR_BINDER;
    }
}
