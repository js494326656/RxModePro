package com.landscape.rxmodepro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.landscape.SilkBrite;
import com.landscape.SilkLog;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    RxTestBean testBean = null;
    SilkBrite<RxTestBean> brite = null;


    @Bind(R.id.tv_result)
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        brite = SilkBrite.create(Logger::i);
    }

    @OnClick(R.id.btn_get)
    void get(View view){
        //TODO
        testBean = brite.createQueryBean(mockData());
        brite.query()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showResult);
    }

    @OnClick(R.id.btn_test)
    void test(View view){
        //TODO
        if (testBean == null) {
            return;
        }
        testBean.setName("landscape");
    }

    public void showResult(RxTestBean info) {
        tvResult.setText(info.toString());
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
        return data;
    }

}
