package com.java.mod.typeHandler;

import com.java.mod.enums.Role;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * RoleTypeHandler 类是一个MyBatis类型的处理器，用于处理Role实体和数据库之间的转换
 * 实现了BaseTypeHandler接口，并指定了如何将Role实体转换为数据库中的整数值，以及如何反向转换
 */
@MappedTypes(Role.class)
@MappedJdbcTypes(JdbcType.TINYINT)
public class RoleTypeHandler extends BaseTypeHandler<Role> {

    /**
     * 设置非空参数值。
     * 当设置预编译SQL语句中的Role参数时被调用。
     *
     * @param ps       预编译SQL语句
     * @param i        参数索引
     * @param parameter Role实体
     * @param jdbcType 数据库类型
     * @throws SQLException 如果设置参数时发生错误
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Role parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCode());// 将Role实体的code属性设置到PreparedStatement中
    }

    /**
     * 从ResultSet中获取可为空的Role结果。
     * 当从ResultSet中读取Role实体时被调用。
     *
     * @param rs         结果集
     * @param columnName 列名
     * @return Role实体
     * @throws SQLException 如果读取结果集时发生错误
     */
    @Override
    public Role getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int code = rs.getInt(columnName); // 从ResultSet中获取整数型code
        return Role.fromCode(code);// 将整数转换为Role实体
    }

    /**
     * 从ResultSet中获取可为空的Role结果。
     * 当从ResultSet中读取Role实体时被调用。
     *
     * @param rs          结果集
     * @param columnIndex 列索引
     * @return Role实体
     * @throws SQLException 如果读取结果集时发生错误
     */
    @Override
    public Role getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int code = rs.getInt(columnIndex);
        return Role.fromCode(code);
    }

    /**
     * 从CallableStatement中获取可为空的Role结果。
     * 当从CallableStatement中读取Role实体时被调用。
     *
     * @param cs          可调用语句
     * @param columnIndex 列索引
     * @return Role实体
     * @throws SQLException 如果读取结果集时发生错误
     */
    @Override
    public Role getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int code = cs.getInt(columnIndex);// 从CallableStatement中获取整数型code
        return Role.fromCode(code);// 将整数转换为Role实体
    }
}