package net.shafin.common.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author shafin
 * @since 11/21/17
 */
public class QueryTemplate {

    private final Connection connection;
    private boolean isConnected;

    public QueryTemplate(Connection connection) {
        this.connection = connection;
        if (this.connection != null) {
            this.isConnected = true;
        }
    }

    public PreparedStatement getPreparedStatement(String query) throws SQLException {
        return this.connection.prepareStatement(query);
    }

    public PreparedStatement getPreparedStatementForReadOnlyResult(String query) throws SQLException {
        return this.connection.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
    }

    public Statement getBatchedStatement(String... queries) throws SQLException {
        Statement stm = this.connection.createStatement();
        for (String query : queries) {
            stm.addBatch(query);
        }

        return stm;
    }

    public boolean queryForExistence(String query, Object... params) {
        connectionCheck();
        validateQuery(query);

        try (PreparedStatement ps = mapParamsIntoStatement(getPreparedStatement(query), params);
             ResultSet rs = ps.executeQuery()) {

            return rs != null && rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int executeQuery(String query, Object... params) {
        connectionCheck();
        validateQuery(query);

        try (PreparedStatement ps = getPreparedStatement(query)) {
            mapParamsIntoStatement(ps, params);

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int executeBatchUpdate(String query, List<Object[]> paramsList) {
        connectionCheck();
        validateQuery(query);

        int totalEffect = 0;

        try (PreparedStatement ps = getPreparedStatement(query)) {
            this.connection.setAutoCommit(false);
            final int batchSize = 1000;
            int count = 0;

            for (Object[] params : paramsList) {
                mapParamsIntoStatement(ps, params);
                ps.addBatch();

                if (++count % batchSize == 0) {
                    totalEffect += Arrays.stream(ps.executeBatch()).sum();
                    this.connection.commit();
                }
            }

            totalEffect += Arrays.stream(ps.executeBatch()).sum();

            this.connection.commit();
            this.connection.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalEffect;
    }

    public <T> List<T> retrieveData(String query, RowMapper<T> rowMapper, Object... params) {
        connectionCheck();
        validateQuery(query);

        List<T> list = new ArrayList<>();
        try (PreparedStatement ps = mapParamsIntoStatement(
                getPreparedStatementForReadOnlyResult(query), params);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rowMapper.mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public <T> T retrieveExplicitField(String query, String field, Class<T> fieldType, Object... params) {
        connectionCheck();
        validateQuery(query);

        try (PreparedStatement ps = mapParamsIntoStatement(
                getPreparedStatementForReadOnlyResult(query), params);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return getExplicitFieldValue(rs, field, fieldType);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public long countRows(String query, Object... params) {
        connectionCheck();
        validateQuery(query);

        query = query.replaceFirst("(?i)SELECT.*FROM", "SELECT count(*) as c FROM ");

        try (PreparedStatement ps = mapParamsIntoStatement(
                getPreparedStatementForReadOnlyResult(query), params);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                return rs.getLong("c");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private PreparedStatement mapParamsIntoStatement(PreparedStatement preparedStatement,
                                                     Object... params) throws SQLException {
        for (int i = 1; i <= params.length; i++) {
            Object param = params[i - 1];

            if (param instanceof String) {
                preparedStatement.setString(i, (String) param);
            } else if (param instanceof Integer) {
                preparedStatement.setInt(i, (Integer) param);
            } else if (param instanceof Long) {
                preparedStatement.setLong(i, (Long) param);
            } else if (param instanceof Float) {
                preparedStatement.setFloat(i, (Float) param);
            } else if (param instanceof Double) {
                preparedStatement.setDouble(i, (Double) param);
            } else if (param instanceof Date) {
                preparedStatement.setDate(i, new Date(((Date) param).getTime()));
            } else if (param instanceof Boolean) {
                preparedStatement.setBoolean(i, (Boolean) param);
            }
        }

        return preparedStatement;
    }

    private <T> T getExplicitFieldValue(ResultSet rs, String field, Class<T> type) throws SQLException {
        if (type.equals(String.class)) {
            return (T) rs.getString(field);
        } else if (type.equals(Integer.class)) {
            return (T) new Integer(rs.getInt(field));
        } else if (type.equals(Long.class)) {
            return (T) new Long(rs.getInt(field));
        } else if (type.equals(Float.class)) {
            return (T) new Float(rs.getLong(field));
        } else if (type.equals(Double.class)) {
            return (T) new Double(rs.getDouble(field));
        } else if (type.equals(Date.class)) {
            return (T) rs.getDate(field);
        } else if (type.equals(Boolean.class)) {
            return (T) new Boolean(rs.getBoolean(field));
        }

        return null;
    }

    private void connectionCheck() {
        if (!isConnected) {
            throw new IllegalStateException("Database Not Connected!");
        }
    }

    private void validateQuery(String query) {
        if (query == null || "".equals(query.trim())) {
            throw new IllegalArgumentException("Provided Query is Empty");
        }
    }
}
