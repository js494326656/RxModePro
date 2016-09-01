package com.landscape;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import rx.subjects.PublishSubject;

/**
 * Created by 1 on 2016/9/1.
 */
public class SilkBeanDriver<T> {

    T silkBean = null;

    public SilkBeanDriver asSilkBean(T srcBean) {
        // TODO: 2016/9/1 遍历

        return this;
    }

    private Object iteratorClone(Object srcBean) {
        Object destObj = null;
        try {
            destObj = srcBean.getClass().newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
        Field[] fields = srcBean.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object fieldObj = field.get(srcBean);
                boolean isPrimitive =
                        (field.getType().isPrimitive() ||
                                (String.class.isAssignableFrom(field.getType())) ||
                                (Number.class.isAssignableFrom(field.getType())) ||
                                (Boolean.class.isAssignableFrom(field.getType())) ||
                                (Character.class.isAssignableFrom(field.getType())));
                if (!isPrimitive) {
                    try {
                        // TODO: 2016/9/1 递归赋值
                        Class.forName(fieldObj.getClass().getName() + "$$Subcriber");




                        field.set(destObj, InheritUtils.cloneObject(fieldObj));
                    } catch (Exception cp) {
                        field.set(destObj, fieldObj);
                    }
                } else {
                    field.set(destObj, fieldObj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return destObj;
    }

    public void setTrigger(PublishSubject triggers) {
        ((BeanSupcriber) silkBean).setTrigger(triggers);
        List<BeanSupcriber> subSilkBeans = iteratorBean(silkBean);
        for (BeanSupcriber beanSupcriber : subSilkBeans) {
            beanSupcriber.setTrigger(triggers);
        }
    }

    public List<BeanSupcriber> iteratorBean(Object srcObj) {
        if (srcObj == null) {
            return new ArrayList<>();
        }
        List<BeanSupcriber> subSilkBeans = new ArrayList<>();
        Field[] fields = srcObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object fieldObj = field.get(srcObj);
                boolean isPrimitive =
                        (field.getType().isPrimitive() ||
                                (String.class.isAssignableFrom(field.getType())) ||
                                (Number.class.isAssignableFrom(field.getType())) ||
                                (Boolean.class.isAssignableFrom(field.getType())) ||
                                (Character.class.isAssignableFrom(field.getType())));
                if (!isPrimitive) {
                    if (fieldObj.getClass().getName().contains("$$Subcriber")) {
                        subSilkBeans.add((BeanSupcriber) fieldObj);
                    }
                    subSilkBeans.addAll(iteratorBean(field.get(srcObj)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return subSilkBeans;
    }

    public T getSilkBean() {
        return silkBean;
    }
}
