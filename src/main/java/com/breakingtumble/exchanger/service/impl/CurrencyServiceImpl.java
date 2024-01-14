package com.breakingtumble.exchanger.service.impl;

import com.breakingtumble.exchanger.model.Currencyy;
import com.breakingtumble.exchanger.service.CurrencyService;
import com.breakingtumble.exchanger.dao.CurrencyDAO;
import com.breakingtumble.exchanger.dao.impl.CurrencyDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class CurrencyServiceImpl implements CurrencyService {
    private static CurrencyServiceImpl instance;
    private final CurrencyDAO currencyDAO;

    private CurrencyServiceImpl() {
        currencyDAO = new CurrencyDAOImpl();
    }

    public static synchronized CurrencyServiceImpl getService() {
        if (instance == null) {
            instance = new CurrencyServiceImpl();
        }
        return instance;
    }

    @Override
    public List<Currencyy> getAllCurrencies() throws SQLException {
        return currencyDAO.getAll();
    }

    @Override
    public Currencyy createCurrency(Currencyy currencyy) throws SQLException {
        return currencyDAO.create(currencyy);
    }

    @Override
    public Currencyy getCurrencyByCode(String code) throws SQLException {
        return currencyDAO.getByCode(code);
    }
}
