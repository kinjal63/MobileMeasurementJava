package org.arpit.java2blog.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class GameDataList implements RowMapper {

	public GameDataModel mapRow(ResultSet rs, int arg1) throws SQLException {
		GameDataModel gameDataModel = new GameDataModel();
		gameDataModel.setGameId(rs.getLong("gameId"));
		gameDataModel.setGameImagePath(rs.getString("gameImagePath"));
		gameDataModel.setGamePackageName(rs.getString("gamePackageName"));
		gameDataModel.setGameName(rs.getString("gameName"));
		return gameDataModel;
	}

}
