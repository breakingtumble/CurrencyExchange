package com.breakingtumble.exchanger.dao.impl;

import com.breakingtumble.exchanger.dao.ExchangeRateDAO;
import com.breakingtumble.exchanger.model.ExchangeRate;
import com.breakingtumble.exchanger.util.DatabaseConnection;
import com.breakingtumble.exchanger.util.ResultSetConverter;

import java.sql.*;
import java.util.List;

public class ExchangeRateDAOImpl implements ExchangeRateDAO {
    private static final String GET_ALL_RATES_QUERY = "SELECT e.id, e.rate,\n" +
            "c1.id AS base_currency_id, c1.code AS base_currency_code, c1.fullname AS base_currency_name, c1.sign as base_currency_sign,\n" +
            "c2.id AS target_currency_id, c2.code AS target_currency_code, c2.fullname AS target_currency_name, c2.sign as target_currency_sign\n" +
            "FROM exchange_rates e INNER JOIN currencies c1 on c1.id = e.base_currency_id INNER JOIN currencies c2 on c2.id = e.target_currency_id;\n";
    private static final String GET_RATE_BY_CODES = "SELECT e.id, e.rate,\n" +
            "c1.id AS base_currency_id, c1.code AS base_currency_code, c1.fullname AS base_currency_name, c1.sign as base_currency_sign,\n" +
            "c2.id AS target_currency_id, c2.code AS target_currency_code, c2.fullname AS target_currency_name, c2.sign as target_currency_sign\n" +
            "FROM ((exchange_rates e INNER JOIN currencies c1 on c1.id = e.base_currency_id) INNER JOIN currencies c2 on c2.id = e.target_currency_id) \n" +
            "WHERE base_currency_code == ? AND target_currency_code == ?;";
    private static final String CREATE_RATE = "INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)";
    private static final String UPDATE_RATE = "UPDATE exchange_rates SET rate = ? WHERE id = ?";

    @Override
    public List<ExchangeRate> getAll() throws SQLException {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ALL_RATES_QUERY);
            return ResultSetConverter.convertToExchangeRateList(resultSet);
        }
    }

    @Override
    public ExchangeRate findByBaseAndTargetCode(String base, String target) throws SQLException {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_RATE_BY_CODES);
            preparedStatement.setString(1, base);
            preparedStatement.setString(2, target);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return ResultSetConverter.convertToExchangeRateEntity(resultSet);
        }
    }

    @Override
    public ExchangeRate create(ExchangeRate exchangeRate) throws SQLException {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            int baseId = exchangeRate.getBase().getId();
            int targetId = exchangeRate.getTarget().getId();
            String baseCode = exchangeRate.getBase().getCode();
            String targetCode = exchangeRate.getTarget().getCode();
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_RATE);
            preparedStatement.setInt(1, baseId);
            preparedStatement.setInt(2, targetId);
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());
            preparedStatement.executeUpdate();
            return findByBaseAndTargetCode(baseCode, targetCode);
        }
    }

    @Override
    public ExchangeRate update(ExchangeRate exchangeRate, float rate) throws SQLException {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()){
            String baseCode = exchangeRate.getBase().getCode();
            String targetCode = exchangeRate.getTarget().getCode();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_RATE);
            preparedStatement.setFloat(1, rate);
            preparedStatement.setInt(2, exchangeRate.getId());
            preparedStatement.executeUpdate();
            return findByBaseAndTargetCode(baseCode, targetCode);
        }
    }
}
