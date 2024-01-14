package com.breakingtumble.exchanger.dao;

import com.breakingtumble.exchanger.model.Currencyy;

import java.sql.SQLException;
import java.util.List;

public interface CurrencyDAO {
    List<Currencyy> getAll() throws SQLException;

    Currencyy create(Currencyy currencyy) throws SQLException;

    Currencyy getByCode(String s) throws SQLException;
}
