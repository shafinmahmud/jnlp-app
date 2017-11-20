package net.shafin.common.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author shafin
 * @since 5/21/17
 */
public interface RowMapper<T> {

    T mapRow(ResultSet resultSet) throws SQLException;
}
