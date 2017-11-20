package net.shafin.common.db;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shafin
 * @since 5/21/17
 */
public abstract class GenericDao<T extends Serializable> {

    private String tableName;

    public DataSource dataSource;
    public QueryTemplate template;

    public GenericDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.template = new QueryTemplate(dataSource.getConnection());

        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];

        this.tableName = clazz.getAnnotation(Table.class).name();
    }

    public boolean isExist(long id) {
        String query = "SELECT id FROM " + tableName + " WHERE id = ?";
        return this.template.queryForExistence(query, new Object[]{id});
    }

    public boolean isExist(String whereField, Object fieldValue) {
        String query = "SELECT * FROM " + tableName + " WHERE " + whereField + " = ?";
        return this.template.queryForExistence(query, new Object[]{fieldValue});
    }

    public T find(long id, RowMapper<T> mapper) {
        String query = "SELECT * FROM " + tableName + " WHERE id = ?";
        List<T> list = this.template.retrieveData(query, mapper, new Object[]{id});

        return getFirstItem(list);
    }

    public T find(String whereField, Object fieldValue, RowMapper<T> mapper) {
        String query = "SELECT * FROM " + tableName + " WHERE " + whereField + " = ?";
        List<T> list = this.template.retrieveData(query, mapper, new Object[]{fieldValue});

        return getFirstItem(list);
    }

    public T find(String whereField1, Object fieldValue1,
                  String whereField2, Object fieldValue2, RowMapper<T> mapper) {
        String query = "SELECT * FROM " + tableName + " WHERE " + whereField1 + " = ? and " + whereField2 + " = ?";
        List<T> list = this.template.retrieveData(query, mapper, new Object[]{fieldValue1, fieldValue2});

        return getFirstItem(list);
    }

    public List<T> findAll(RowMapper<T> mapper) {
        String query = "SELECT * FROM " + tableName;
        return this.template.retrieveData(query, mapper);
    }

    public List<T> findAll(String whereField, Object fieldValue, RowMapper<T> mapper) {
        String query = "SELECT * FROM " + tableName + " WHERE " + whereField + " = ?";
        return this.template.retrieveData(query, mapper, new Object[]{fieldValue});
    }

    public List<T> findAllBy(String whereField1, Object fieldValue1,
                             String whereField2, Object fieldValue2, RowMapper<T> mapper) {
        String query = "SELECT * FROM " + tableName + " WHERE " + whereField1 + " = ? and " + whereField2 + " = ?";
        return this.template.retrieveData(query, mapper, new Object[]{fieldValue1, fieldValue2});
    }

    public List<T> findAll(int page, int size, RowMapper<T> mapper) {
        String query = "SELECT * FROM " + tableName + " LIMIT ? OFFSET ?";
        return this.template.retrieveData(query, mapper, new Object[]{size, (page - 1) * size});
    }

    public List<T> findAll(String whereField, Object fieldValue, int page, int size, RowMapper<T> mapper) {
        String query = "SELECT * FROM " + tableName + " WHERE " + whereField + "=? LIMIT = ? and OFFSET = ?";
        return this.template.retrieveData(query, mapper, new Object[]{fieldValue, size, (page - 1) * size});
    }

    public Long countRowsAll() {
        String query = "SELECT count(id) FROM " + tableName;
        return this.template.countRows(query);
    }

    public Long countRowsAll(String whereField, Object fieldValue) {
        String query = "SELECT count(id) FROM " + tableName + " WHERE " + whereField + "=?";
        return this.template.countRows(query, new Object[]{fieldValue});
    }

    public int save(String[] fields, Object[] values) {
        String query = "INSERT INTO " + tableName + " " + formatArray(fields) + " VALUES " + formatArray(values);
        return this.template.executeQuery(query, values);
    }

    public int update(String field, Object value) {
        String query = "UPDATE " + tableName + " SET " + field + " = ?";
        return this.template.executeQuery(query, new Object[]{value});
    }

    public int update(String field, Object value, String whereField, Object whereValue) {
        String query = "UPDATE " + tableName + " SET " + field + " = ? WHERE " + whereField + " = ?";
        return this.template.executeQuery(query, new Object[]{value, whereValue});
    }

    public int update(String[] fields, Object[] values, String whereField, Object whereValue) {
        String query = "UPDATE " + tableName + " SET " + formatArray(fields) + " VALUES "
                + formatArray(values) + " WHERE " + whereField + " = ?";

        return this.template.executeQuery(query, mergeArrays(values, whereValue));
    }

    public int delete(long entityId) {
        String query = "DELETE FROM " + tableName + " WHERE id = ?";
        return this.template.executeQuery(query, new Object[]{entityId});
    }

    public int delete(String whereField, Object fieldValue) {
        String query = "DELETE FROM " + tableName + " WHERE " + whereField + " = ?";
        return this.template.executeQuery(query, new Object[]{fieldValue});
    }

    private String formatArray(String[] array) {
        return (array != null && array.length > 0) ?
                "(" + String.join(", ", array) + ")" : "()";
    }

    private Object[] mergeArrays(Object[] array1, Object... array2) {
        List<Object> params = new LinkedList<>();
        params.add(array1);
        params.add(array2);

        return params.toArray();
    }

    private String formatArray(Object[] array) {
        if (array != null && array.length > 0) {
            String[] params = new String[array.length];
            for (int i = 0; i < params.length; i++) {
                params[i] = "?";
            }

            return formatArray(params);
        }

        return "()";
    }

    private T getFirstItem(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }
}
