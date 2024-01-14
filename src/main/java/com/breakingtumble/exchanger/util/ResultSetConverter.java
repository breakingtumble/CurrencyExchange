package com.breakingtumble.exchanger.util;

import com.breakingtumble.exchanger.model.Currencyy;
import com.breakingtumble.exchanger.model.ExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetConverter {
    public static List<Currencyy> convertToCurrencyList(ResultSet rs) {
        List<Currencyy> currencyList = new ArrayList<>();
        try {
            while (rs.next()) {
                currencyList.add(convertToCurrencyEntity(rs));
            }
            return currencyList;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public static Currencyy convertToCurrencyEntity(ResultSet resultSet) {
        try {
            return new Currencyy(resultSet.getInt("id"),
                    resultSet.getString("fullname"),
                    resultSet.getString("code"),
                    resultSet.getString("sign"));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static List<ExchangeRate> convertToExchangeRateList(ResultSet resultSet) {
        List<ExchangeRate> exchangeRateList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                exchangeRateList.add(convertToExchangeRateEntity(resultSet));
            }
            return exchangeRateList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ExchangeRate convertToExchangeRateEntity(ResultSet resultSet) {
        try {
            Currencyy baseCurrency = new Currencyy(
                    resultSet.getInt("base_currency_id"),
                    resultSet.getString("base_currency_name"),
                    resultSet.getString("base_currency_code"),
                    resultSet.getString("base_currency_sign")
            );
            Currencyy targetCurrency = new Currencyy(
                    resultSet.getInt("target_currency_id"),
                    resultSet.getString("target_currency_name"),
                    resultSet.getString("target_currency_code"),
                    resultSet.getString("target_currency_sign")
            );
            return new ExchangeRate(
                    resultSet.getInt("id"),
                    baseCurrency,
                    targetCurrency,
                    resultSet.getBigDecimal("rate")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
