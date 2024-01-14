package com.breakingtumble.exchanger.dto;

import com.breakingtumble.exchanger.model.Currencyy;

import java.math.BigDecimal;

public class ExchangeRateDto {
    private int id;
    private Currencyy base;
    private Currencyy target;
    private BigDecimal rate;

    public ExchangeRateDto(int id, Currencyy base, Currencyy target, BigDecimal rate) {
        this.id = id;
        this.base = base;
        this.target = target;
        this.rate = rate;
    }

    public ExchangeRateDto(Currencyy base, Currencyy target, BigDecimal rate) {
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
