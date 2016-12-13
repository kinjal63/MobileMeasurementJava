package org.arpit.java2blog.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class DataUsageMapper implements RowMapper {

	public Aggregate mapRow(ResultSet rs, int arg1) throws SQLException {
		Aggregate aggregate = new Aggregate();
		aggregate.setUserId(rs.getLong("userId"));
		aggregate.setTotalMobileRx(rs.getLong("totalMobileRx"));
		aggregate.setTotalMobileTx(rs.getLong("totalMobileTx"));
		aggregate.setTotalWifiRx(rs.getLong("totalWifiRx"));
		aggregate.setTotalWifiTx(rs.getLong("totalWifiTx"));
		return aggregate;
	}

}
