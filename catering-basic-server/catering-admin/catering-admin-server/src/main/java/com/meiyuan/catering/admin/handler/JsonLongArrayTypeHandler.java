package com.meiyuan.catering.admin.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author admin
 */
public class JsonLongArrayTypeHandler extends BaseTypeHandler<Long[]> {
	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Long[] parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, toJson(parameter));
	}

	@Override
	public Long[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return this.toObject(rs.getString(columnName));
	}

	@Override
	public Long[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return this.toObject(rs.getString(columnIndex));
	}

	@Override
	public Long[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return this.toObject(cs.getString(columnIndex));
	}

	public String toJson(Long[] params) {
		try {
			return MAPPER.writeValueAsString(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "[]";
	}

	public Long[] toObject(String content) {
		if (content != null && !content.isEmpty()) {
			try {
				return (Long[]) MAPPER.readValue(content, Long[].class);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			return null;
		}
	}
}
