package com.breakingtumble.exchanger.model;

public class ExchangeRate {
    private int id;
    private Currencyy base;
    private Currencyy target;
    private float rate;

    public ExchangeRate(int id, Currencyy base, Currencyy target, float rate) {
        this.id = id;
        this.base = base;
        this.target = target;
        this.rate = rate;
    }

    public ExchangeRate(Currencyy base, Currencyy target, float rate) {
        this.base = base;
        this.target = target;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public float getRate() {
        return rate;
    }

    public Currencyy getBase() {
        return base;
    }

    public Currencyy getTarget() {
        return target;
    }

}
