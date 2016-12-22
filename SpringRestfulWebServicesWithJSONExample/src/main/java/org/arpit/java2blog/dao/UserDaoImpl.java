package org.arpit.java2blog.dao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.arpit.java2blog.model.Aggregate;
import org.arpit.java2blog.model.AppData;
import org.arpit.java2blog.model.AppDetail;
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
import org.arpit.java2blog.util.NotificationUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
			if( i != size - 1 ) {
				whereIn += userConnectionInfo.getRemoteUserIds().get(i) + ",";
			}
			else {
				whereIn += userConnectionInfo.getRemoteUserIds().get(i) + ")";
			}
		}
		
		String idQuery = "select device_token from user ua where ua.id in " + whereIn;

		List<String> deviceTokens = (List<String>) jdbcTemplateObject.query(idQuery,
				new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		});
		NotificationUtil.sendBluetoothInvitation(userConnectionInfo.getUserId(), deviceTokens);
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
		System.out.println("SQL statement->" + sql1);
		jdbcTemplateObject.update(sql, new Object[] {});
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

	public static void main(String[] args) {
		UserDaoImpl impl = new UserDaoImpl();
		// impl.aggregateCallDuration(null);
		AppData appData = new AppData();
		appData.setUserId(31);

		AppDetail[] appDetail = new AppDetail[2];

		appDetail[0].setAppName("abc");
		appDetail[1].setAppPackageName("com.insta");

		appDetail[1].setAppName("abc1");
		appDetail[2].setAppPackageName("com.insta1");
		appData.setAppDetail(appDetail);

		impl.saveAppData(appData);
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
		String whereIn = "(";
		for (int i = 0; i < userIds.size(); i++) {
			if( i != userIds.size() - 1 ) {
				whereIn += userIds.get(i) + ",";
			}
			else {
				whereIn += userIds.get(i) + ")";
			}
		}
		
		String sql = "select gl.game_name, gl.id as game_id, gl.game_image_path, gl.network_type from game_library gl join " +
					"(select count(game_id) as cg, game_id, " +
					"group_concat(user_id) as users from game_profile " +
					"where user_id in " + whereIn +
					"group by game_id) as cgk on cgk.game_id = gl.id " +
					"where cgk.cg > " + (userIds.size() - 1);

		java.util.List<GameDataModel> gameResponse = (List<GameDataModel>) jdbcTemplateObject.query(sql,
				new RowMapper<GameDataModel>() {
					@Override
					public GameDataModel mapRow(ResultSet rs, int rowNum) throws SQLException {
						GameDataModel gameResponse = new GameDataModel();
						gameResponse.setGameId(rs.getLong("game_id"));
						gameResponse.setGameImagePath(rs.getString("game_image_path"));
						gameResponse.setGameName(rs.getString("game_name"));
						gameResponse.setGameNetworkType(rs.getInt("network_type"));
						
						return gameResponse;
					}
				});

		return gameResponse;

	}
}
