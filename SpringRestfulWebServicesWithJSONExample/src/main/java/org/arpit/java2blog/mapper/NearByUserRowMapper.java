package org.arpit.java2blog.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.arpit.java2blog.model.db.NearByUserDBModel;
import org.springframework.jdbc.core.RowMapper;

public class NearByUserRowMapper implements RowMapper<NearByUserDBModel>{

	@Override
	public NearByUserDBModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		NearByUserDBModel model = new NearByUserDBModel();
		model.setUserId(rs.getLong("user_id"));
		model.setDeviceToken(rs.getString("device_token"));
		return model;
	}
}
