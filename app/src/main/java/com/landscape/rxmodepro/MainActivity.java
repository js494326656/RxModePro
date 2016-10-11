package com.landscape.rxmodepro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.landscape.SilkBrite;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    RxTestBean testBean = null;
    SilkBrite<RxTestBean> brite = null;


    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.tv_rx_node)
    TextView tvRxNode;
    @Bind(R.id.tv_rx_child_node)
    TextView tvRxChildNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        brite = SilkBrite.create();
    }

    @OnClick(R.id.btn_get)
    void get(View view) {
        //TODO
        if (testBean == null) {
            testBean = brite.asSilkBean(mockData());
            brite.asModeObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::showResult);
            // 监听RxTestBean的nick
            brite.asNodeObservable("nick")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(o1 -> {
                        String name = String.valueOf(o1);
                        if (TextUtils.isEmpty(name)) {
                            return "unknown user";
                        }
                        return name;
                    })
                    .subscribe(this::showRxNode);
            // 监听RxTestBean的rxBean的nick
            brite.asNodeObservable("rxBean::nick")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(o1 -> {
                        String name = String.valueOf(o1);
                        if (TextUtils.isEmpty(name)) {
                            return "unknown user";
                        }
                        return name;
                    })
                    .subscribe(this::showChildNode);

        } else {
            brite.updateBean(mockData());
        }
    }

    @OnClick(R.id.btn_test)
    void test(View view) {
        //TODO
        if (testBean == null) {
            return;
        }
        testBean.setNick("adfadf");
        testBean.getRxBean().setNick("Cat");
        testBean.setNum(99);
        testBean.setClose(false);
    }

    public void showResult(RxTestBean info) {
        tvResult.setText(info.toString());
    }

    public void showRxNode(String value) {
        tvRxNode.setText(value);
    }

    public void showChildNode(String value) {
        tvRxChildNode.setText(value);
    }

    private RxTestBean mockData() {
        RxTestBean data = new RxTestBean();
        data.setAmount(10f);
        data.setClose(true);
        data.setFirstName('h');
        data.setId(10);
        data.setName("hello");
        data.setNick("world");
        data.setNum(100);
        data.setScore(10000l);
        data.setPrice(-1f);
        data.setTotal(99f);
        // child object
        RxTest2 rxBean2 = new RxTest2();
        rxBean2.setAge(15);
        rxBean2.setNick("Tom");
        rxBean2.setNumber(110);
        data.setRxBean(rxBean2);
        return data;
    }

}
