package com.landscape;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by 1 on 2016/8/15.
 */
public class SilkBrite<T> {

    T destBean = null;

    private final SilkLog logger;

    @CheckResult @NonNull
    public static SilkBrite create(){
        return new SilkBrite(message -> {});
    }

    @CheckResult @NonNull
    public static SilkBrite create(SilkLog log) {
        return new SilkBrite(log);
    }

    private SilkBrite(SilkLog log) {
        logger = log;
    }

    private final PublishSubject<T> triggers = PublishSubject.create();

    private final Scheduler scheduler = Schedulers.io();


    public T createQueryBean(T srcBean) {
        destBean = null;
        try {
            InheritUtils<T> inheritUtils = InheritUtils.create();
            destBean = inheritUtils.cloneObject(srcBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return destBean;
    }

    public void updateBean(T srcBean) {
        if (destBean.getClass().isAssignableFrom(srcBean.getClass())) {
            throw new IllegalArgumentException("必须传入相同类型");
        }
        InheritUtils<T> inheritUtils = InheritUtils.create();
        try {
            inheritUtils.cpObject(srcBean, destBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Observable<T> query() {
        final Observable<T> queryObservable = triggers //
                .startWith(destBean) //
                .subscribeOn(scheduler)
                .onBackpressureLatest() // Guard against uncontrollable frequency of scheduler executions.
                .doOnSubscribe(new Action0() {
                    @Override public void call() {
                        if (destBean == null) {
                            throw new IllegalStateException(
                                    "Cannot subscribe to observable for the bean is null.");
                        }
                        ((BeanSupcriber)destBean).setTrigger(triggers);
                    }
                });
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                queryObservable.unsafeSubscribe(subscriber);
            }
        });

    }


}
