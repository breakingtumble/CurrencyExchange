package com.breakingtumble.exchanger.service;

import com.breakingtumble.exchanger.model.Currencyy;

import java.sql.SQLException;
import java.util.List;

public interface CurrencyService {
    List<Currencyy> getAllCurrencies() throws SQLException;

    Currencyy createCurrency(Currencyy currencyy) throws SQLException;

    Currencyy getCurrencyByCode(String code) throws SQLException;
}
