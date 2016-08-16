package com.landscape.complier;

import com.squareup.javapoet.ClassName;

/**
 * Created by brucezz on 2016-08-04.
 * Github: https://github.com/brucezz
 * Email: im.brucezz@gmail.com
 */
public class TypeUtil {
    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");
    public static final ClassName ANDROID_ON_CLICK_LISTENER = ClassName.get("android.view", "View", "OnClickListener");
    public static final ClassName SUBCRIBER = ClassName.get("com.landscape", "BeanSupcriber");
    public static final ClassName PROVIDER = ClassName.get("me.brucezz.viewfinder.provider", "Provider");
}
