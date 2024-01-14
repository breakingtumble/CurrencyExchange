package com.breakingtumble.exchanger.dao.impl;

import com.breakingtumble.exchanger.model.Currencyy;
import com.breakingtumble.exchanger.util.DatabaseConnection;
import com.breakingtumble.exchanger.util.ResultSetConverter;
import com.breakingtumble.exchanger.dao.CurrencyDAO;

import java.sql.*;
import java.util.List;

public class CurrencyDAOImpl implements CurrencyDAO {
    private static final String GET_ALL_QUERY = "SELECT * FROM currencies";
    private static final String INSERT_QUERY = "INSERT INTO currencies (code, fullname, sign) " +
            "VALUES (?, ?, ?)";
    private static final String GET_CURRENCY_BY_CODE = "SELECT * FROM currencies WHERE code = ?";

    @Override
    public List<Currencyy> getAll() throws SQLException {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ALL_QUERY);
            return ResultSetConverter.convertToCurrencyList(resultSet);
        }
    }

    @Override
    public Currencyy getByCode(String s) throws SQLException {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_CURRENCY_BY_CODE);
            preparedStatement.setString(1, s);
            ResultSet resultSet = preparedStatement.executeQuery();
            /*
            *   Checking if resulted set contains any values, if it doesn't return null value,
            * we must handle it higher in hierarchy*/
            if (!resultSet.next()) {
                return null;
            }
            /*
            *   Convert the first occurrence of the set to entity if it's found
            * */
            return ResultSetConverter.convertToCurrencyEntity(resultSet);
        }
    }

    @Override
    public Currencyy create(Currencyy currencyy) throws SQLException {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
            preparedStatement.setString(1, currencyy.getCode());
            preparedStatement.setString(2, currencyy.getName());
            preparedStatement.setString(3, currencyy.getSign());
            preparedStatement.executeUpdate();
            return getByCode(currencyy.getCode());
        }
    }
}
