package com.landscape.rxmodepro;

import com.landscape.annotation.RxBean;

import java.lang.reflect.Field;

/**
 * Created by 1 on 2016/8/17.
 */
@RxBean
public class RxTestBean {

    private int num;
    private int id;
    private boolean isClose;
    private long score;
    private String nick;
    private String name;
    private char firstName;
    private float price;
    private float amount;
    private double total;
    private RxTest2 rxBean;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getFirstName() {
        return firstName;
    }

    public void setFirstName(char firstName) {
        this.firstName = firstName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public RxTest2 getRxBean() {
        return rxBean;
    }

    public void setRxBean(RxTest2 rxBean) {
        this.rxBean = rxBean;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("num:" + num + "\n");
        buffer.append("id:" + id + "\n");
        buffer.append("isClose:" + isClose + "\n");
        buffer.append("score:" + score + "\n");
        buffer.append("nick:" + nick + "\n");
        buffer.append("name:" + name + "\n");
        buffer.append("firstName:" + firstName + "\n");
        buffer.append("price:" + price + "\n");
        buffer.append("amount:" + amount + "\n");
        buffer.append("total:" + total + "\n");
        buffer.append("rxBean:" + rxBean.toString());
        return buffer.toString();
    }
}
