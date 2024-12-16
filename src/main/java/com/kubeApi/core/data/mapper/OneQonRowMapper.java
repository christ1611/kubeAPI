package com.kubeApi.core.data.mapper;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;


@Slf4j
public class OneQonRowMapper implements RowMapper<Map<String, Object>>
{
    //
    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Map<String, Object> mapOfColValues = createColumnMap(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
            Object obj = getColumnValue(rs, i);
            mapOfColValues.put(key, obj);
        }
        return mapOfColValues;
    }




    protected Map<String, Object> createColumnMap(int columnCount) {
        return new LinkedCaseInsensitiveMap<>(columnCount);
    }

    /**
     * Determine the key to use for the given column in the column Map.
     * @param columnName the column name as returned by the ResultSet
     * @return the column key to use
     * @see ResultSetMetaData#getColumnName
     */
    protected String getColumnKey(String columnName)
    {
        return JdbcUtils.convertUnderscoreNameToPropertyName(columnName);

        //return columnName;
    }

    /**
     * Retrieve a JDBC object value for the specified column.
     * <p>The default implementation uses the {@code getObject} method.
     * Additionally, this implementation includes a "hack" to get around Oracle
     * returning a non standard object for their TIMESTAMP datatype.
     * @param rs is the ResultSet holding the data
     * @param index is the column index
     * @return the Object returned
     * @see JdbcUtils#getResultSetValue
     */
    protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
        //return getResultSetValue(rs, index);

        Object obj = rs.getObject(index);
        String className = null;

        if (obj != null) {
            className = obj.getClass().getName();
        }


        //log.debug("mapper Value = ({}) {}", className, obj);


        if (StringUtils.indexOf(className, "BLOB") != -1) {
            Blob blob = (Blob) obj;
            obj = blob.getBytes(1, (int) blob.length());
            return obj;
            //return rs.getBinaryStream( index );
        }
        else if (StringUtils.indexOf(className, "CLOB") != -1) {
            Clob clob = (Clob) obj;
            obj = clob.getSubString(1, (int) clob.length());
            return obj;
        }
        else if (  StringUtils.equals(className, "oracle.sql.TIMESTAMP")
                || StringUtils.equals(className, "oracle.sql.TIMESTAMPTZ")
                )
        {
            return ZonedDateTime.of(rs.getTimestamp(index).toLocalDateTime(), ZoneId.systemDefault());
        }
        else if (StringUtils.startsWith(className, "java.sql.Timestamp")) {
            return rs.getDate(index).toLocalDate();
        }
        else if (className != null && StringUtils.startsWith( className,"oracle.sql.DATE"))
        {
                return rs.getDate(index).toLocalDate();
        }
        else if (obj instanceof Timestamp)
        {
            Timestamp ts = (Timestamp) obj ;
            LocalDateTime tm = ts.toLocalDateTime();


            if( tm.getHour() == 0 && tm.getMinute() == 0 && tm.getSecond() == 0 )
                return tm.toLocalDate();
            else
                return tm;

        }
        else if (obj instanceof Date) {
            return rs.getDate(index).toLocalDate();
        }
        else if (StringUtils.equals(className, "java.lang.String"))
        {
            return rs.getString(index);
        }


        String value = rs.getString(index);

        if(value == null) return null;


        if( StringUtils.isNumeric(value) )
        {
            if( value.charAt(0) == '0' && value.length() > 1 )
                return value;

            if( value.indexOf(".") == -1 )
            {
                return Long.parseLong( value );
            }
            else
            {
                return new BigDecimal( value );
            }
        }



        return obj;
    }

}
