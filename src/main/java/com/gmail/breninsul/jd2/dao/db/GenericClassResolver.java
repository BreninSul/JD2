package com.gmail.breninsul.jd2.dao.db;

import com.gmail.breninsul.jd2.managers.MainProps;
import org.springframework.core.GenericTypeResolver;

import java.lang.reflect.ParameterizedType;

public class GenericClassResolver<EntityType> {

    public Class getEntityGenericClass() {
        try {
            if ("Spring".equals(MainProps.get("GenericTypeResolver"))) {
                return getGenericClassSpring(0);
            } else return getGenericClassReflectionAPI(0);
        } catch (NullPointerException e) {
            throw new NullPointerException("No generic type have been set for Entity");
        }
    }


    /**
     * Set "GenericTypeResolver=ReflectionAPI" in main.properties to use this variant
     * Reflection is not good, but seems to be best way for this case
     *
     * @return class of generic
     */
    private Class getGenericClassReflectionAPI(int index) throws NullPointerException {
        Class clazz = null;
        try {
            ParameterizedType actaualParameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
            clazz = (Class) actaualParameterizedType.getActualTypeArguments()[index];
        } catch (NullPointerException e) {
            throw new NullPointerException("No generic type have been set");
        }
        return clazz;
    }

    /**
     * Set "GenericTypeResolver=Spring" in main.properties to use this variant
     * Do not recomended for use, 10-30 times slower then getGenericClassReflectionAPI()
     *
     * @return class of generic
     */
    private Class getGenericClassSpring(int index) throws NullPointerException {
        Class clazz = null;
        try {
            clazz = (Class) GenericTypeResolver.resolveTypeArguments(getClass(), GenericClassResolver.class)[index];
        } catch (NullPointerException e) {
            throw new NullPointerException("No generic type have been set");
        }
        return clazz;
    }
}
