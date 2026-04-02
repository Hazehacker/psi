package com.zeroone.star.storemanagement.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Unix 时间戳（秒）与 LocalDateTime 的转换器
 */
@MappedTypes(LocalDateTime.class)
public class UnixTimestampTypeHandler extends BaseTypeHandler<LocalDateTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        // LocalDateTime -> Unix 时间戳（秒）
        long timestamp = parameter.atZone(ZoneId.systemDefault()).toEpochSecond();
        ps.setLong(i, timestamp);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        long timestamp = rs.getLong(columnName);
        return rs.wasNull() ? null : toLocalDateTime(timestamp);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        long timestamp = rs.getLong(columnIndex);
        return rs.wasNull() ? null : toLocalDateTime(timestamp);
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        long timestamp = cs.getLong(columnIndex);
        return cs.wasNull() ? null : toLocalDateTime(timestamp);
    }

    /**
     * 时间戳转 LocalDateTime
     */
    private LocalDateTime toLocalDateTime(long timestamp) {
        if (timestamp == 0) {
            return null;
        }
        return LocalDateTime.ofInstant(
            Instant.ofEpochSecond(timestamp), 
            ZoneId.systemDefault()
        );
    }
}