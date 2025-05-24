package com.balanceeat.demo.domain.nutrition.entity;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(FoodCategory.class)
public class FoodCategoryTypeHandler extends BaseTypeHandler<FoodCategory> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, FoodCategory parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getLabel());
    }

    @Override
    public FoodCategory getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String label = rs.getString(columnName);
        return label == null ? null : FoodCategory.fromLabel(label);
    }

    @Override
    public FoodCategory getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String label = rs.getString(columnIndex);
        return label == null ? null : FoodCategory.fromLabel(label);
    }

    @Override
    public FoodCategory getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String label = cs.getString(columnIndex);
        return label == null ? null : FoodCategory.fromLabel(label);
    }
} 