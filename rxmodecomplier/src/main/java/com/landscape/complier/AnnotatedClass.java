package com.landscape.complier;

import com.landscape.model.RxBeanType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

import rx.subjects.PublishSubject;

/**
 * Created by brucezz on 2016-07-27.
 * Github: https://github.com/brucezz
 * Email: im.brucezz@gmail.com
 */
public class AnnotatedClass {

    public TypeElement mClassElement;
    public RxBeanType mType;
    public Elements mElementUtils;

    public AnnotatedClass(TypeElement classElement, Elements elementUtils) {
        this.mClassElement = classElement;
        this.mElementUtils = elementUtils;
    }

    public String getFullClassName() {
        return mClassElement.getQualifiedName().toString();
    }

    public void addClass(RxBeanType type) {
        mType = type;
    }

    public JavaFile generateFinder() {
        List<MethodSpec> injectMethods = new ArrayList<>();
        Method[] methods = null;
        MethodSpec constructor = null;
        try {
            Class<?> cls = Class.forName(mClassElement.getSimpleName().toString());
            methods = cls.getMethods();
            // constructor
            constructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(cls, "parent")
                    .addStatement("this.$N = $N", "greeting", "greeting")
                    .build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        // method inject(final T host, Object source, Provider provider)
        MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder("sendTrigger")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mClassElement.asType()), "bean")
                .addStatement("if(triggers != null){triggers.onNext(bean);}");


        // add sendtrigger()
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (method.getName().startsWith("set")) {
                List<VariableElement> params = (List<VariableElement>) mType.getMethodElement().get(i).getParameters();
                if (params.size() > 0) {
                    injectMethods.add(
                            MethodSpec.methodBuilder(method.getName())
                                    .addModifiers(Modifier.PUBLIC)
                                    .addAnnotation(Override.class)
                                    .returns(TypeName.VOID)
                                    .addParameter(TypeName.get(params.get(0).asType()), params.get(0).getSimpleName().toString())
                                    .addStatement("super.$N($N)", method.getName(),params.get(0).getSimpleName().toString())
                                    .addStatement("sendTrigger(this)")
                                    .build());
                }
            }
        }



//        if (mMethods.size() > 0) {
//            injectMethodBuilder.addStatement("$T listener", TypeUtil.ANDROID_ON_CLICK_LISTENER);
//        }
//        for (OnClickMethod method : mMethods) {
//            // declare OnClickListener anonymous class
//            TypeSpec listener = TypeSpec.anonymousClassBuilder("")
//                .addSuperinterface(TypeUtil.ANDROID_ON_CLICK_LISTENER)
//                .addMethod(MethodSpec.methodBuilder("onClick")
//                    .addAnnotation(Override.class)
//                    .addModifiers(Modifier.PUBLIC)
//                    .returns(TypeName.VOID)
//                    .addParameter(TypeUtil.ANDROID_VIEW, "view")
//                    .addStatement("host.$N()", method.getMethodName())
//                    .build())
//                .build();
//            injectMethodBuilder.addStatement("listener = $L ", listener);
//            for (int id : method.ids) {
//                // set listeners
//                injectMethodBuilder.addStatement("provider.findView(source, $L).setOnClickListener(listener)", id);
//            }
//        }
        // generate whole class
        TypeSpec.Builder finderClassBuilder = TypeSpec.classBuilder(mClassElement.getSimpleName() + "$$Subcriber")
                .superclass(TypeName.get(mClassElement.asType()))
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.SUBCRIBER, TypeName.get(mClassElement.asType())))
                .addMethod(injectMethodBuilder.build())
                .addField(PublishSubject.class,"triggers");

        if (constructor != null) {
            finderClassBuilder.addMethod(constructor);
        }

        for (MethodSpec injectMethodSpec : injectMethods) {
            finderClassBuilder.addMethod(injectMethodSpec);
        }

        TypeSpec finderClass = finderClassBuilder.build();

        String packageName = mElementUtils.getPackageOf(mClassElement).getQualifiedName().toString();

        return JavaFile.builder(packageName, finderClass).build();
    }
}
