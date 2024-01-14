package com.breakingtumble.exchanger.dto;

import com.breakingtumble.exchanger.model.Currencyy;

public class ExchangeRateDTO {
    private Currencyy baseCurrency;
    private Currencyy targetCurrency;
    private float rate;
    private float amount;
    private float convertedAmount;

    public ExchangeRateDTO(Currencyy baseCurrency, Currencyy targetCurrency, float rate, float amount, float convertedAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public Currencyy getBaseCurrency() {
        return baseCurrency;
    }

    public Currencyy getTargetCurrency() {
        return targetCurrency;
    }

    public float getRate() {
        return rate;
    }

    public float getAmount() {
        return amount;
    }

    public float getConvertedAmount() {
        return convertedAmount;
    }
}
