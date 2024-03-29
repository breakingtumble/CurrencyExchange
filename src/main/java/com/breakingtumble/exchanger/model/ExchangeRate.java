package com.breakingtumble.exchanger.model;

import java.math.BigDecimal;

public class ExchangeRate {
    private int id;
    private Currencyy base;
    private Currencyy target;
    private BigDecimal rate;

    public ExchangeRate(int id, Currencyy base, Currencyy target, BigDecimal rate) {
        this.id = id;
        this.base = base;
        this.target = target;
        this.rate = rate;
    }

    public ExchangeRate(Currencyy base, Currencyy target, BigDecimal rate) {
        this.base = base;
        this.target = target;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public Currencyy getBase() {
        return base;
    }

    public Currencyy getTarget() {
        return target;
    }

}
