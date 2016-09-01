package com.landscape;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by 1 on 2016/8/17.
 */
public class InheritUtils {
    public static  <T> T cloneObject(Object object) throws Exception {
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

    public static  <T> void cpObject(T srcBean,T destBean) throws Exception {
        boolean hasChanged = false;
        Field[] srcFields = srcBean.getClass().getDeclaredFields();
        for (int i = 0; i < srcFields.length; i++) {
            Field field = srcFields[i];
            field.setAccessible(true);
            Object val = field.get(srcBean);
            Object destVal = field.get(destBean);
            if (val.hashCode() != destVal.hashCode() && !val.equals(destVal)) {
                hasChanged = true;
                field.set(destBean,val);
            }
        }
        if (hasChanged) {
            Method sendMethod = destBean.getClass().getMethod("sendTrigger",new Class[]{srcBean.getClass()});
            sendMethod.invoke(destBean, destBean);
        }
    }

}
