package com.breakingtumble.exchanger.dto;

import com.breakingtumble.exchanger.model.Currencyy;

import java.math.BigDecimal;

public class ExchangeRateResultDto {
    private Currencyy baseCurrency;
    private Currencyy targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;

    public ExchangeRateResultDto(Currencyy baseCurrency, Currencyy targetCurrency, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
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

    public BigDecimal getRate() {
        return rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }
}
