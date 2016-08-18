package com.landscape;

import java.lang.reflect.Field;

/**
 * Created by 1 on 2016/8/17.
 */
public class InheritUtils<T> {

    public static <T> InheritUtils<T> create() {
        return new InheritUtils<>();
    }

    public T cloneObject(T object) throws Exception {
        T result = null;
        Class<?> finderClass = null;
        finderClass = Class.forName(object.getClass().getName() + "$$Subcriber");
        result = (T) finderClass.newInstance();
        Field[] srcFields = object.getClass().getDeclaredFields();
        for (int i = 0; i < srcFields.length; i++) {
            Field field = srcFields[i];
            field.setAccessible(true);
            Object val = field.get(object);
            field.set(result,val);
        }
        return result;
    }


}
