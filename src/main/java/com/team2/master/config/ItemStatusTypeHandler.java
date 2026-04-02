package com.team2.master.config;

import com.team2.master.command.domain.entity.enums.ItemStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(ItemStatus.class)
public class ItemStatusTypeHandler extends BaseTypeHandler<ItemStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ItemStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getDbValue());
    }

    @Override
    public ItemStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : ItemStatus.fromDbValue(value);
    }

    @Override
    public ItemStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : ItemStatus.fromDbValue(value);
    }

    @Override
    public ItemStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : ItemStatus.fromDbValue(value);
    }
}
