package com.landscape.model;

import com.landscape.annotation.RxBean;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by landscape on 2016-08-16.
 */
public class RxBeanField {
    private VariableElement mFieldElement;

    public RxBeanField(Element element) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(
                String.format("Only fields can be annotated with @%s", RxBean.class.getSimpleName()));
        }

        mFieldElement = (VariableElement) element;
        RxBean bindView = mFieldElement.getAnnotation(RxBean.class);
    }

    public Name getFieldName() {
        return mFieldElement.getSimpleName();
    }

    public TypeMirror getFieldType() {
        return mFieldElement.asType();
    }
}
