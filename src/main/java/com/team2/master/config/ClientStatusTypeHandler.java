package com.team2.master.config;

import com.team2.master.command.domain.entity.enums.ClientStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(ClientStatus.class)
public class ClientStatusTypeHandler extends BaseTypeHandler<ClientStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ClientStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getDbValue());
    }

    @Override
    public ClientStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : ClientStatus.fromDbValue(value);
    }

    @Override
    public ClientStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : ClientStatus.fromDbValue(value);
    }

    @Override
    public ClientStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : ClientStatus.fromDbValue(value);
    }
}
