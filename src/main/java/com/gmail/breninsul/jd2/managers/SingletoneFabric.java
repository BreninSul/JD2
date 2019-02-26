package com.gmail.breninsul.jd2.managers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class SingletoneFabric {
    private static final SingletoneFabric instance = new SingletoneFabric();

    private Map<Class, Object> mapOfSingletons = new HashMap<Class, Object>();

    protected SingletoneFabric() {
    }

    /**
     * @param classOf Put here class you want to create/use
     * @param <T>     - Our singletone
     * @return Singletone
     */
    public static final <T> T getInstance(Class<T> classOf) {
        if (!instance.mapOfSingletons.containsKey(classOf)) {
            synchronized (instance) {
                if (!instance.mapOfSingletons.containsKey(classOf)) {
                    T obj = null;
                    try {
                        Constructor<T> constructor = classOf.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        obj = constructor.newInstance();
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        ExeptionManager.log(e, classOf);
                    }
                   // obj =context.getBean(classOf);
                    instance.mapOfSingletons.put(classOf, obj);
                }
            }
        }
        return (T) instance.mapOfSingletons.get(classOf);
    }
}



