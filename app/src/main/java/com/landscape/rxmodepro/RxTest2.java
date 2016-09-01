package com.landscape.rxmodepro;

import com.landscape.annotation.RxBean;

/**
 * Created by 1 on 2016/9/1.
 */
@RxBean
public class RxTest2 {
    private String nick;
    private int age;
    private int number;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
