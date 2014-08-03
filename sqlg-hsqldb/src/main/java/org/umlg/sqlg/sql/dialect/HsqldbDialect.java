package org.umlg.sqlg.sql.dialect;

import com.tinkerpop.gremlin.structure.Property;
import org.umlg.sqlg.structure.PropertyType;
import org.umlg.sqlg.structure.SqlGDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Date: 2014/07/16
 * Time: 3:09 PM
 */
public class HsqldbDialect implements SqlDialect {

    @Override
    public boolean supportsTransactionalSchema() {
        return false;
    }

    /**
     * Hsqldb sequences start with a 0
     *
     * @return
     */
    @Override
    public long sequenceInitialValue() {
        return 0l;
    }

    @Override
    public String getJdbcDriver() {
        return "org.hsqldb.jdbc.JDBCDriver";
    }

    @Override
    public void validateProperty(Object key, Object value) {
        if (value instanceof String) {
            return;
        }
        if (value instanceof Character) {
            return;
        }
        if (value instanceof Boolean) {
            return;
        }
        if (value instanceof Byte) {
            return;
        }
        if (value instanceof Short) {
            return;
        }
        if (value instanceof Integer) {
            return;
        }
        if (value instanceof Long) {
            return;
        }
        if (value instanceof Double) {
            return;
        }
        if (value instanceof byte[]) {
            return;
        }
        if (value instanceof boolean[]) {
            return;
        }
        if (value instanceof char[]) {
            return;
        }
        if (value instanceof short[]) {
            return;
        }
        if (value instanceof int[]) {
            return;
        }
        if (value instanceof long[]) {
            return;
        }
        if (value instanceof double[]) {
            return;
        }
        if (value instanceof String[]) {
            return;
        }
        if (value instanceof Character[]) {
            return;
        }
        if (value instanceof Boolean[]) {
            return;
        }
        if (value instanceof Byte[]) {
            return;
        }
        if (value instanceof Short[]) {
            return;
        }
        if (value instanceof Integer[]) {
            return;
        }
        if (value instanceof Long[]) {
            return;
        }
        if (value instanceof Double[]) {
            return;
        }
        throw Property.Exceptions.dataTypeOfPropertyValueNotSupported(value);
    }

    @Override
    public String getColumnEscapeKey() {
        return "\"";
    }

    @Override
    public String getPrimaryKeyType() {
        return "BIGINT NOT NULL PRIMARY KEY";
    }

    @Override
    public String getAutoIncrementPrimaryKeyConstruct() {
        return "BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY";
    }

    @Override
    public String propertyTypeToSqlDefinition(PropertyType propertyType) {
        switch (propertyType) {
            case BOOLEAN:
                return "BOOLEAN";
            case BYTE:
                return "TINYINT";
            case SHORT:
                return "SMALLINT";
            case INTEGER:
                return "INTEGER";
            case LONG:
                return "BIGINT";
            case FLOAT:
                return "REAL";
            case DOUBLE:
                return "DOUBLE";
            case STRING:
                return "LONGVARCHAR";
            case BYTE_ARRAY:
                return "LONGVARBINARY";
            case BOOLEAN_ARRAY:
                return "BOOLEAN ARRAY DEFAULT ARRAY[]";
            case SHORT_ARRAY:
                return "SMALLINT ARRAY DEFAULT ARRAY[]";
            case INTEGER_ARRAY:
                return "INTEGER ARRAY DEFAULT ARRAY[]";
            case LONG_ARRAY:
                return "BIGINT ARRAY DEFAULT ARRAY[]";
            case FLOAT_ARRAY:
                return "REAL ARRAY DEFAULT ARRAY[]";
            case DOUBLE_ARRAY:
                return "DOUBLE ARRAY DEFAULT ARRAY[]";
            case STRING_ARRAY:
                return "LONGVARCHAR ARRAY DEFAULT ARRAY[]";
            default:
                throw new IllegalStateException("Unknown propertyType " + propertyType.name());
        }
    }

    @Override
    public int propertyTypeToJavaSqlType(PropertyType propertyType) {
        switch (propertyType) {
            case BOOLEAN:
                return Types.BOOLEAN;
            case BYTE:
                return Types.TINYINT;
            case SHORT:
                return Types.SMALLINT;
            case INTEGER:
                return Types.INTEGER;
            case LONG:
                return Types.BIGINT;
            case FLOAT:
                return Types.REAL;
            case DOUBLE:
                return Types.DOUBLE;
            case STRING:
                return Types.CLOB;
            case BOOLEAN_ARRAY:
                return Types.ARRAY;
            case SHORT_ARRAY:
                return Types.ARRAY;
            case INTEGER_ARRAY:
                return Types.ARRAY;
            case LONG_ARRAY:
                return Types.ARRAY;
            case FLOAT_ARRAY:
                return Types.ARRAY;
            case DOUBLE_ARRAY:
                return Types.ARRAY;
            case STRING_ARRAY:
                return Types.ARRAY;
            default:
                throw new IllegalStateException("Unknown propertyType " + propertyType.name());
        }
    }

    @Override
    public PropertyType sqlTypeToPropertyType(int sqlType, String typeName) {
        switch (sqlType) {
            case Types.BIT:
                return PropertyType.BOOLEAN;
            case Types.SMALLINT:
                return PropertyType.SHORT;
            case Types.INTEGER:
                return PropertyType.INTEGER;
            case Types.BIGINT:
                return PropertyType.LONG;
            case Types.REAL:
                return PropertyType.FLOAT;
            case Types.DOUBLE:
                return PropertyType.DOUBLE;
            case Types.VARCHAR:
                return PropertyType.STRING;
            case Types.ARRAY:
                switch (typeName) {
                    case "BOOLEAN ARRAY":
                        return PropertyType.BOOLEAN_ARRAY;
                    case "SMALLINT ARRAY":
                        return PropertyType.SHORT_ARRAY;
                    case "INTEGER ARRAY":
                        return PropertyType.INTEGER_ARRAY;
                    case "BIGINT ARRAY":
                        return PropertyType.LONG_ARRAY;
                    case "DOUBLE ARRAY":
                        return PropertyType.DOUBLE_ARRAY;
                    default:
                        if (typeName.contains("VARCHAR") && typeName.contains("ARRAY")) {
                            return PropertyType.STRING_ARRAY;
                        } else {
                            throw new RuntimeException(String.format("Array type not supported sqlType = %s and typeName = %s", new String[]{String.valueOf(sqlType), typeName}));
                        }
                }
            default:
                throw new IllegalStateException("Unknown sqlType " + sqlType);
        }
    }

    @Override
    public String getForeignKeyTypeDefinition() {
        return "BIGINT NOT NULL";
    }

    @Override
    public boolean supportsFloatValues() {
        return false;
    }

    @Override
    public boolean supportsFloatArrayValues() {
        return false;
    }

    @Override
    public String getArrayDriverType(PropertyType propertyType) {
        switch (propertyType) {
            case BOOLEAN_ARRAY:
                return "BOOLEAN";
            case SHORT_ARRAY:
                return "SMALLINT";
            case INTEGER_ARRAY:
                return "INTEGER";
            case LONG_ARRAY:
                return "BIGINT";
            case DOUBLE_ARRAY:
                return "DOUBLE";
            case STRING_ARRAY:
                return "VARCHAR";
            default:
                throw new IllegalStateException("propertyType " + propertyType.name() + " unknown!");
        }
    }

    @Override
    public String createTableStatement() {
        return "CREATE TABLE ";
    }

    @Override
    public void prepareDB(Connection conn) {
        StringBuilder sql = new StringBuilder("SET DATABASE TRANSACTION CONTROL MVCC;");
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql.toString())) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
