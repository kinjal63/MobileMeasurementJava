package org.arpit.java2blog.dao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.arpit.java2blog.mapper.NearByUserRowMapper;
import org.arpit.java2blog.model.Aggregate;
import org.arpit.java2blog.model.AppData;
import org.arpit.java2blog.model.CallDuration;
import org.arpit.java2blog.model.DataUsageMapper;
import org.arpit.java2blog.model.GameData;
import org.arpit.java2blog.model.GameDataModel;
import org.arpit.java2blog.model.User;
import org.arpit.java2blog.model.UserAvailablity;
import org.arpit.java2blog.model.UserConnectionInfo;
import org.arpit.java2blog.model.UserDataUsage;
import org.arpit.java2blog.model.UserGameResponse;
import org.arpit.java2blog.model.UserInput;
import org.arpit.java2blog.model.UserMapper;
import org.arpit.java2blog.model.UserRSSI;
import org.arpit.java2blog.model.db.NearByUserDBModel;
import org.arpit.java2blog.util.NotificationUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class UserDaoImpl implements UserDao {

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	private Cloudinary cloudinary;

	private MailSender mailSender;
	private SimpleMailMessage templateMessage;

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setTemplateMessage(SimpleMailMessage templateMessage) {
		this.templateMessage = templateMessage;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);

		cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "dmffogznf", "api_key", "593785351395324",
				"api_secret", "NhoJNHlIl0yhNzlcB1XAmcS7_Ak"));
	}

	public int insert(User customer) {
		String imagePath = "";
		if (!customer.getFile().isEmpty()) {
			try {
				String filePath = "D:/Images/" + customer.getFirstname() + "-uploaded";
				File fileToUpload = new File(filePath);
				byte[] bytes = customer.getFile().getBytes();
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fileToUpload));
				stream.write(bytes);
				stream.close();

				Map uploadResult = cloudinary.uploader().upload(filePath, ObjectUtils.emptyMap());
				imagePath = (String) uploadResult.get("secure_url");
				fileToUpload.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String sql = "INSERT INTO USER "
				+ "(firstname, lastname, email, password, age, image_path, device_token, created_date) VALUES (?, ?, ?, ?, ?, ?, ?, now())";
		Connection conn = null;

		int userId = 0;

		try {
			conn = (Connection) dataSource.getConnection();
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, customer.getFirstname());
			ps.setString(2, customer.getLastname());
			ps.setString(3, customer.getEmail());
			ps.setString(4, customer.getPassword());
			ps.setInt(5, customer.getAge());
			ps.setString(6, imagePath);
			ps.setString(7, customer.getDeviceToken());
			ps.executeUpdate();
			ps.close();

			String sql1 = "select MAX(id) as id from user";
			PreparedStatement ps1 = (PreparedStatement) conn.prepareStatement(sql1);
			ResultSet rs = ps1.executeQuery();

			if (rs.next()) {
				userId = rs.getInt("id");
			}

			sendEmail();

			return userId;

		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	private void sendEmail() {
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		msg.setTo("kinjalpatel63@gmail.com");
		msg.setText("Your code is 11234.");
		try {
			this.mailSender.send(msg);
		} catch (MailException ex) {
			// simply log it and go on...
			System.err.println(ex.getMessage());
		}
	}

	@Override
	public void sendConnectionInvite(UserConnectionInfo userConnectionInfo) {
		String whereIn = "(";
		int size = userConnectionInfo.getRemoteUserIds().size();

		for (int i = 0; i < size; i++) {
			if (i != size - 1) {
				whereIn += userConnectionInfo.getRemoteUserIds().get(i) + ",";
			} else {
				whereIn += userConnectionInfo.getRemoteUserIds().get(i) + ")";
			}
		}

		String idQuery = "select device_token from user ua where ua.id in " + whereIn;

		List<String> deviceTokens = (List<String>) jdbcTemplateObject.query(idQuery, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		});
		if( userConnectionInfo.getWifiDeviceAddress() != null ) {
			NotificationUtil.sendWifiInvitation(userConnectionInfo.getUserId(), 
					userConnectionInfo.getWifiDeviceAddress(), deviceTokens);	
		}
		else {
			NotificationUtil.sendBluetoothInvitation(userConnectionInfo.getUserId(), deviceTokens);
		}
	}

	@Override
	public void sendRemoteUserInput(UserInput input) {
		String idQuery = "select device_token from user ua where ua.id = " + input.getToUserId();

		String deviceToken = (String) jdbcTemplateObject.queryForObject(idQuery, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		});
		NotificationUtil.sendBluetoothAddress(input.getUserId(), deviceToken, input.getBluetoothAddress());
	}

	public User findByEmail(String email) {
		String sql = "select * from user where email = ?";
		User user = (User) jdbcTemplateObject.queryForObject(sql, new Object[] { email }, new UserMapper());

		return user;

	}

	public void saveRssi(UserRSSI userRssi) {
		String sql = "insert into rssi (userId, deviceId, rssi, latitude, longitude, operator_name, timestamp) values (?, ?, ?, ?, ?, ?, now())";
		jdbcTemplateObject.update(sql, new Object[] { userRssi.getUserId(), userRssi.getDeviceId(), userRssi.getRssi(),
				userRssi.getLatitude(), userRssi.getLongitude(), userRssi.getOperatorName() });
	}

	@Override
	public void saveDataUsage(UserDataUsage dataUsage) {
		String sql = "insert into data_usage (userId, country, deviceId, mobileTx, mobileRx, wifiTx, wifiRx, longitude, latitude, operator_name, timestamp) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())";
		jdbcTemplateObject.update(sql,
				new Object[] { dataUsage.getUserId(), dataUsage.getCountry(), dataUsage.getDeviceId(),
						dataUsage.getMobileTx(), dataUsage.getMobileRx(), dataUsage.getWifiTx(), dataUsage.getWifiRx(),
						dataUsage.getLongitude(), dataUsage.getLatitude(), dataUsage.getOperatorName() });

	}

	@Override
	public void aggregateCallDuration(CallDuration callDuration) {
		// Aggregate agr = aggregateRssiAndDataUsage();
		String sql = "insert into call_logs (userId, country, total_mobileRx, "
				+ "total_mobileTx, total_wifiRx, total_wifiTx, timestamp) (select userId, country, sum(mobileRx) as totalMobileRx, sum(mobileTx) as totalMobileTx, "
				+ "sum(wifiRx) as totalWifiRx, sum(wifiTx) as totalWifiTx, now() from data_usage group by userId, country)";

		System.out.println(sql);
		jdbcTemplateObject.update(sql, new Object[] {});
	}

	public Aggregate aggregateRssiAndDataUsage() {
		String sql = "select userId, country, sum(mobileRx) as totalMobileRx, sum(mobileTx) as totalMobileTx, "
				+ "sum(wifiRx) as totalWifiRx, sum(wifiTx) as totalWifiTx "
				+ "from data_usage group by userId, country";
		Aggregate user = (Aggregate) jdbcTemplateObject.queryForObject(sql, null, new DataUsageMapper());
		return user;
	}

	@Override
	public void saveAppData(AppData appData) {
		String sql = "insert into game_profile (user_id, game_id) ";
		String sql1 = "(select " + appData.getUserId() + ", id from game_library where game_package_name in (";
		String whereIn = "";
		for (int j = 0; j < appData.getAppDetail().length; j++) {
			if (j != appData.getAppDetail().length - 1) {
				whereIn += "'" + appData.getAppDetail()[j].getAppPackageName() + "',";
			} else {
				whereIn += "'" + appData.getAppDetail()[j].getAppPackageName() + "'))";
			}
		}
		sql1 += whereIn;
		sql += sql1;
		
		String insertAvailability = "insert into user_availability (user_id, latitude, "
				+ "longitude, availability, os_type, updated_at) values(?, ?, ?, 0, 1, now())";
		
		System.out.println("SQL statement->" + sql1);
		jdbcTemplateObject.update(sql, new Object[]{});
		jdbcTemplateObject.update(insertAvailability, new Object[] {appData.getUserId(), appData.getLatitude(), 
				appData.getLongitude()});
	}

	public void addGameInfo(GameData gameData) {
		String imagePath = getImagePath(gameData);

		String sql = "insert into game_library (game_name, game_publisher_name,"
				+ "game_package_name, game_studio_name, game_image_path, age_rating,"
				+ "supporting_os, network_type, min_players, max_players, created_date) values (" + "'"
				+ gameData.getGameName() + "'," + "'" + gameData.getGamePublisherName() + "'," + "'"
				+ gameData.getGamePackageName() + "'," + "'" + gameData.getGameStudioName() + "'," + "'" + imagePath
				+ "','" + gameData.getAgeRating() + "'," + gameData.getOsType() + "," + gameData.getNetworkType() + ","
				+ "'" + gameData.getMinPlayers() + "'," + "'" + gameData.getMaxPlayers() + "', now())";

		System.out.println("SQL statement->" + sql);
		jdbcTemplateObject.update(sql, new Object[] {});
	}

	private String getImagePath(GameData gameData) {
		if (!gameData.getGameImagePath().isEmpty()) {
			try {
				String filePath = "D:/Images/" + gameData.getGamePackageName() + "-uploaded";
				File fileToUpload = new File(filePath);
				byte[] bytes = gameData.getGameImagePath().getBytes();
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fileToUpload));
				stream.write(bytes);
				stream.close();

				Map uploadResult = cloudinary.uploader().upload(filePath, ObjectUtils.emptyMap());
				String imagePath = (String) uploadResult.get("secure_url");
				fileToUpload.delete();

				return imagePath;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public Object getGameList() {
		String sql = "select id as gameId, game_package_name as gamePackageName, game_image_path as gameImagePath,"
				+ " game_name as gameName from game_library";

		java.util.List<GameDataModel> userGameResponse = (List<GameDataModel>) jdbcTemplateObject.query(sql,
				new RowMapper<GameDataModel>() {
					@Override
					public GameDataModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						GameDataModel gameDataModel = new GameDataModel();
						gameDataModel.setGameId(rs.getLong("gameId"));
						gameDataModel.setGameImagePath(rs.getString("gameImagePath"));
						gameDataModel.setGamePackageName(rs.getString("gamePackageName"));
						gameDataModel.setGameName(rs.getString("gameName"));
						return gameDataModel;
					}
				});
		return userGameResponse;
	}

	@Override
	public void editGame(GameData gameData) {
		String imagePath = getImagePath(gameData);

		String sql = "update game_library set game_name = ?,"
				+ "game_studio_name = ?, game_image_path = ?, age_rating = ?,"
				+ "supporting_os = ?, network_type = ?, min_players = ?, max_players = ?, created_date = now() "
				+ "where id = " + gameData.getGameId();

		System.out.println("SQL statement->" + sql);
		jdbcTemplateObject.update(sql,
				new Object[] { gameData.getGameName(), gameData.getGameStudioName(), imagePath, gameData.getAgeRating(),
						gameData.getOsType(), gameData.getNetworkType(), gameData.getMinPlayers(),
						gameData.getMaxPlayers() });
	}

	@Override
	public void deleteGame(long gameId) {

		String sql = "delete from game_library where id = " + gameId;

		System.out.println("SQL statement->" + sql);
		jdbcTemplateObject.execute(sql);
	}

	@Override
	public void updateUserAvailablity(UserAvailablity availablity) {
		String sql = "insert into user_availability (user_id, availability, latitude, longitude, updated_at) values(?, ?, ?, ?, now())"
				+ " ON DUPLICATE KEY UPDATE user_id=" + availablity.getUserId() + ", availability="
				+ availablity.getAvailablity() + ", latitude = " + availablity.getLatitude() + "," + "longitude = "
				+ availablity.getLongitude() + ",updated_at = now()";

		jdbcTemplateObject.update(sql, new Object[] { availablity.getUserId(), availablity.getAvailablity(),
				availablity.getLatitude(), availablity.getLongitude() });
	}

	@SuppressWarnings("unchecked")
	public List<UserGameResponse> getMutualGameList(long userId) {
		String latSql = "select ua.latitude from user_availability ua where ua.user_id = " + userId;

		String latitude = (String) jdbcTemplateObject.queryForObject(latSql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		});

		String sql = "select u.id as user_id, u.image_path as user_image_path, "
				+ "u.firstname, u.lastname, GROUP_CONCAT(distinct gl.id) as game_id, GROUP_CONCAT(distinct gl.game_name) as game_name, "
				+ "GROUP_CONCAT(distinct gl.game_image_path) as game_image_path from game_profile gf "
				+ "inner join game_profile gf1 on gf.game_id = gf1.game_id and gf.user_id <> gf1.user_id "
				+ "join game_library gl on gl.id = gf.game_id join user u on u.id = gf.user_id where "
				+ "u.id in (select u.id from user u join user_availability ua on u.id = ua.user_id "
				+ "where ua.availability = 1 and (ua.latitude -  " + latitude + ") < 10 ) "
				+ "and gf.game_id in (select gf.game_id from game_profile gf where gf.user_id = " + userId
				+ ")and u.id <> " + userId + " group by u.id";

		java.util.List<UserGameResponse> userGameResponse = (List<UserGameResponse>) jdbcTemplateObject.query(sql,
				new RowMapper<UserGameResponse>() {
					@Override
					public UserGameResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
						UserGameResponse userGameResponse = new UserGameResponse();
						userGameResponse.setUserId(rs.getLong("user_id"));
						userGameResponse.setUserImagePath(rs.getString("user_image_path"));
						userGameResponse.setUserFirstName(rs.getString("firstname"));
						userGameResponse.setUserLastName(rs.getString("lastname"));

						List<Long> gameIdList = new ArrayList<>();
						List<String> gameNameList = new ArrayList<>();
						List<String> gameImagePathList = new ArrayList<>();

						String[] gameIds = rs.getString("game_id").split(",");
						String[] gameNames = rs.getString("game_name").split(",");
						String[] gameImagePath = rs.getString("game_image_path").split(",");

						for (String gameId : gameIds) {
							gameIdList.add(Long.parseLong(gameId));
						}

						for (String gameName : gameNames) {
							gameNameList.add(gameName);
						}

						for (String gameIPath : gameImagePath) {
							gameImagePathList.add(gameIPath);
						}

						userGameResponse.setGameId(gameIdList);
						userGameResponse.setGameName(gameNameList);
						userGameResponse.setGameImagePath(gameImagePathList);
						return userGameResponse;
					}
				});

		return userGameResponse;

	}

	@SuppressWarnings("unchecked")
	public List<GameDataModel> getMutualGames(long userId, ArrayList<Long> userIds) {
		userIds.add(userId);
		String whereIn = "(";
		for (int i = 0; i < userIds.size(); i++) {
			if (i != userIds.size() - 1) {
				whereIn += userIds.get(i) + ",";
			} else {
				whereIn += userIds.get(i) + ")";
			}
		}

		String sql = "select gl.game_name, gl.game_package_name, gl.id as game_id, gl.game_image_path, gl.network_type from game_library gl join "
				+ "(select count(game_id) as cg, game_id, " + "group_concat(user_id) as users from game_profile "
				+ "where user_id in " + whereIn + "group by game_id) as cgk on cgk.game_id = gl.id " + "where cgk.cg > "
				+ (userIds.size() - 1);

		java.util.List<GameDataModel> gameResponse = (List<GameDataModel>) jdbcTemplateObject.query(sql,
				new RowMapper<GameDataModel>() {
					@Override
					public GameDataModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						GameDataModel gameResponse = new GameDataModel();
						gameResponse.setGameId(rs.getLong("game_id"));
						gameResponse.setGameImagePath(rs.getString("game_image_path"));
						gameResponse.setGameName(rs.getString("game_name"));
						gameResponse.setGameNetworkType(rs.getInt("network_type"));
						gameResponse.setGamePackageName(rs.getString("game_package_name"));

						return gameResponse;
					}
				});

		return gameResponse;

	}

	@SuppressWarnings("unused")
	@Override
	public void addUserAvailabilityTime(long userId, String fromTime, String toTime) {
		DateFormat formatter = new SimpleDateFormat("HH:mm");
		
		java.sql.Time fromTimeValue;
		try {
			fromTimeValue = new java.sql.Time(formatter.parse(fromTime).getTime());
			java.sql.Time toTimeValue = new java.sql.Time(formatter.parse(toTime).getTime());
			String sql = "insert into user_available_times (user_id, from_time, to_time, created_at) "
					+ "values (?, ?, ?, now())";
			String sql1 = "insert into user_notifications "
					+ "(user_id, notification_sent, sent_at) values (?, 0, now())";
			jdbcTemplateObject.update(sql, new Object[] { userId, fromTimeValue, toTimeValue });
			jdbcTemplateObject.update(sql1, new Object[] { userId });
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void updateAndNotifyNearByUsers() {
		List<Long> availableUsers = getAvailableUsers();
		updateUsersAvailability(availableUsers);
		notifyNearByUsers(availableUsers);
	}

	private List<Long> getAvailableUsers() {
		String sql = "select uat.user_id from user_available_times uat " +
				"left join user_notifications un on uat.user_id = un.user_id " +
				"join user u on uat.user_id = u.id " + 
				"where uat.from_time < now() and uat.to_time > now() and un.user_id is null";

		List<Long> userIds = (List<Long>) jdbcTemplateObject.query(sql, new RowMapper<Long>() {
			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				return rs.getLong("user_id");
			}
		});
		return userIds;
	}
	
	private void updateUsersAvailability(List<Long> userIds) {
		String whereIn = "";
		for (int i = 0; i < userIds.size(); i++) {
			if (i != userIds.size() - 1) {
				whereIn += userIds.get(i) + ",";
			} else {
				whereIn += userIds.get(i);
			}
		}

		if( userIds.size() > 0 ) {
			String sql = "update user_availability ua set ua.availability = 1 where user_id in (" + whereIn + ")";
			jdbcTemplateObject.update(sql);
		}
	}

	private void notifyNearByUsers(List<Long> userIds) {
		for(Long userId : userIds) {
			String latSql = "select ua.latitude from user_availability ua where ua.user_id = " + userId;
			Long latitude = (Long)jdbcTemplateObject.queryForObject(latSql, new RowMapper<Long>() {
				@Override
				public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getLong("latitude");
				}
			});
			
			String sql = "select u.id as user_id, u.device_token, u.firstname, u.lastname " +
			"from game_profile gf " + 
			"join game_library gl on gl.id = gf.game_id join user u on u.id = gf.user_id where " +
			"u.id in (select u.id from user u join user_availability ua on u.id = ua.user_id " +
			"where ua.availability = 1 and abs(ua.latitude - " + latitude + ") < 10 ) " +
			"and gf.game_id in (select gf.game_id from game_profile gf where gf.user_id = " + userId +
			") and u.id <> " + userId + " group by u.id";
			
			@SuppressWarnings("unchecked")
			List<NearByUserDBModel> nearByUserModels = (List<NearByUserDBModel>)jdbcTemplateObject.query(sql, new NearByUserRowMapper());
			
			List<String> deviceTokens = new ArrayList<>();
			List<Long> neighbourUserIds = new ArrayList<>();
			for( NearByUserDBModel model : nearByUserModels ) {
				deviceTokens.add(model.getDeviceToken());
				neighbourUserIds.add(model.getUserId());
			}
			NotificationUtil.notifyOtherUsers(userId, deviceTokens);
			updateNotifications(userId, neighbourUserIds);
		}
	}
	
	private void updateNotifications(long userId, List<Long> userIds) {
		
		String sql = "insert into user_notifications (user_id, neighbour_user_id, notification_sent, sent_at)"
				+ " values (?, ?, ?, now()) on duplicate key update notification_sent = 1";
		try {
			PreparedStatement ps = dataSource.getConnection().prepareStatement(sql);
			for (Long user : userIds) {
			    ps.setLong(1, userId);
			    ps.setLong(2, user);
			    ps.setInt(3, 1);
			    ps.addBatch();
			}
			ps.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String sql1 = "insert into user_notifications (user_id, neighbour_user_id, notification_sent, sent_at)"
				+ " values (?, ?, ?, now()) on duplicate key update notification_sent = 1";
		try {
			PreparedStatement ps = dataSource.getConnection().prepareStatement(sql);
			for (Long user : userIds) {
			    ps.setLong(1, user);
			    ps.setLong(2, userId);
			    ps.setInt(3, 1);
			    ps.addBatch();
			}
			ps.executeBatch();
			
			dataSource.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void checkUserAvailability() {
		String sql = "select uat.user_id from user_available_times uat " +
				 	 "where uat.from_time < now() and uat.to_time > now()";
		@SuppressWarnings("unchecked")
		List<Long> userIds = (List<Long>)jdbcTemplateObject.query(sql, new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("user_id"); 
			}
		});
		disableUserAvailability(userIds);
		deleteNotificationsForUsers(userIds);
	}
	
	private void disableUserAvailability(List<Long> userIds) {
		String whereIn = "";
		for (int i = 0; i < userIds.size(); i++) {
			if (i != userIds.size() - 1) {
				whereIn += userIds.get(i) + ",";
			} else {
				whereIn += userIds.get(i);
			}
		}
		
		String sql = "update user_availability ua set ua.availability = 0 where user_id not in (" + whereIn + ")";
		jdbcTemplateObject.update(sql);
	}
	
	private void deleteNotificationsForUsers(List<Long> userIds) {
		String whereIn = "";
		for (int i = 0; i < userIds.size(); i++) {
			if (i != userIds.size() - 1) {
				whereIn += userIds.get(i) + ",";
			} else {
				whereIn += userIds.get(i);
			}
		}
		
		for(Long userId : userIds) {
			String latSql = "select ua.latitude from user_availability ua where ua.user_id = " + userId;
			Long latitude = (Long)jdbcTemplateObject.queryForObject(latSql, new RowMapper<Long>() {
				@Override
				public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getLong("latitude");
				}
			});
		String sqlStatement = "delete from user_notifications where " +
					"(user_id = " + userId + " and neighbour_user_id not in  (select u.id as user_id " +
					"from game_profile gf " +
					"join game_library gl on gl.id = gf.game_id join user u on u.id = gf.user_id where " +
					"u.id in (select u.id from user u join user_availability ua on u.id = ua.user_id " +
					"where ua.availability = 1 and (abs(ua.latitude - " + latitude + ") < 10)) " +
					"and gf.game_id in (select gf.game_id from game_profile gf where gf.user_id = " + userId + ") " +
					"and u.id <> " + userId + " group by u.id)) " +
					
 					"or user_id not in (" + whereIn + ")" +

					"or (user_id not in (select u.id as user_id " +
					"from game_profile gf " +
					"join game_library gl on gl.id = gf.game_id join user u on u.id = gf.user_id where " +
					"u.id in (select u.id from user u join user_availability ua on u.id = ua.user_id " +
					"where ua.availability = 1 and (abs(ua.latitude - " + latitude + ") < 10)) " +
					"and gf.game_id in (select gf.game_id from game_profile gf where gf.user_id = " + userId + ") " +
					"and u.id <> " + userId + " group by u.id) and neighbour_user_id = " + userId + ")";
		
			jdbcTemplateObject.execute(sqlStatement);
		}
	}
}
