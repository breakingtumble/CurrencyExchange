package com.breakingtumble.exchanger.dao;

import com.breakingtumble.exchanger.model.ExchangeRate;

import java.sql.SQLException;
import java.util.List;

public interface ExchangeRateDAO {
    List<ExchangeRate> getAll() throws SQLException;

    ExchangeRate update(ExchangeRate exchangeRate, float rate) throws SQLException;

    ExchangeRate findByBaseAndTargetCode(String base, String target) throws SQLException;

    ExchangeRate create(ExchangeRate exchangeRate) throws SQLException;
}
