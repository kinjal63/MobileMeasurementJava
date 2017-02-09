package com.hk.mm.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.hk.mm.common.NotificationUtils;
import com.hk.mm.entity.DataUsage;
import com.hk.mm.entity.Rssi;
import com.hk.mm.entity.User;
import com.hk.mm.entity.UserAvaillableTimes;
import com.hk.mm.exception.ServiceNotAcceptable;
import com.hk.mm.exception.ServiceUnauthorized;
import com.hk.mm.service.UserService;
import com.hk.mm.vo.DUser;
import com.hk.mm.vo.RequestVO;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	private static final String AUTH_TOKEN = "AER934LJS9DFMER0KEFE";
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerUser(@RequestBody final RequestVO requestVO, @RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			System.out.println("File is empty");
		}
		System.out.println("Request parames : " + requestVO.toString());
		return null;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DUser> userLogin(@RequestBody final DUser dUser) {
		User user = userService.findByEmail(dUser.getEmail());
		if (user == null) {
			throw new ServiceNotAcceptable("Invalid email address");
		}
		DUser resObj = new DUser();
		resObj.setAuthToken(AUTH_TOKEN);
		return new ResponseEntity<DUser>(resObj, HttpStatus.OK);
	}

	// @RequestMapping(value = "/updateAvailability", method =
	// RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces
	// = MediaType.APPLICATION_JSON_VALUE)
	// public ResponseEntity<DUser>
	// updateUserAvailability(@RequestHeader("authToken") String authToken,
	// final DUser dUser) {
	// if (!isValid(authToken)) {
	// throw new ServiceUnauthorized("Invalid auth token");
	// }
	// User user = userService.findByUserId(dUser.getUserId());
	// if (user == null) {
	// throw new ServiceUnauthorized("User unauthorized");
	// }
	// UserAvailability userAvailability = new UserAvailability();
	// userAvailability.setId(dUser.getUserId());
	//
	// return null;
	// }

	@RequestMapping(value = "/addTime", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DUser> addUserTime(@RequestHeader("authToken") final String authToken,
			@RequestBody final DUser dUser) {
		if (!isValid(authToken)) {
			throw new ServiceUnauthorized("Invalid auth token");
		}
		User user = userService.findByUserId(Integer.valueOf(dUser.getUserId()));
		if (user == null) {
			throw new ServiceUnauthorized("User unauthorized");
		}
		UserAvaillableTimes availlableTimes = new UserAvaillableTimes();
		DateFormat formatter = new SimpleDateFormat("HH:mm");
		Time fromTime = null;
		Time toTime = null;
		try {
			fromTime = new java.sql.Time(formatter.parse(dUser.getFromTime()).getTime());
			toTime = new java.sql.Time(formatter.parse(dUser.getToTime()).getTime());
		} catch (ParseException e) {
			throw new ServiceException("Internal Server Error");
		}

		availlableTimes.setFromTime(fromTime);
		availlableTimes.setToTime(toTime);
		availlableTimes.setUser(user);
		availlableTimes.setCreatedAt(new Date(System.currentTimeMillis()));
		userService.addUserTime(availlableTimes);

		// UserNotifications userNotifications = new UserNotifications();
		// userNotifications.setUser(user);
		// userNotifications.setNotificationSent(0);
		// userNotifications.setSentAt(new Date(System.currentTimeMillis()));
		// userService.addUserNotification(userNotifications);

		DUser resObj = new DUser();
		resObj.setMessage("Success");
		return new ResponseEntity<DUser>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/sendRemoteUserInput", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DUser> sendRemoteUserInput(@RequestHeader("authToken") final String authToken,
			@RequestBody final DUser dUser) {

		if (!isValid(authToken)) {
			throw new ServiceUnauthorized("Invalid auth token");
		}
		User user = userService.findByUserId(Long.valueOf(dUser.getUserId()));
		if (user == null) {
			throw new ServiceUnauthorized("Invalid user Id");
		}
		User toUser = userService.findByUserId(Long.valueOf(dUser.getToUserId()));
		if (toUser == null) {
			throw new ServiceUnauthorized("Invalid to userId");
		}
		NotificationUtils.sendBluetoothAddress(Long.valueOf(dUser.getUserId()), toUser.getDeviceToken(),
				dUser.getBluetoothAddress());

		DUser resObj = new DUser();
		resObj.setMessage("Success");
		return new ResponseEntity<DUser>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public String uploadImage(@RequestBody final RequestVO requestVO, @RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			System.out.println("File is empty");
		}
		System.out.println("Request parames : " + requestVO.toString());
		return null;
	}

	@RequestMapping(value = "/recordRSSI", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DUser> recordRSSI(@RequestHeader("authToken") final String authToken,
			@RequestBody final DUser dUser) {
		if (!isValid(authToken)) {
			throw new ServiceUnauthorized("Invalid auth token");
		}
		User user = userService.findByUserId(Long.valueOf(dUser.getUserId()));
		if (user == null) {
			throw new ServiceUnauthorized("User unauthorized");
		}
		
		//userId, deviceId, rssi, latitude, longitude, operator_name, timestamp
		Rssi rssi = new Rssi();
		rssi.setUserId(user.getUserId());
		rssi.setDeviceId(dUser.getDeviceId());
		rssi.setRssi(Double.valueOf(dUser.getRssi()));
		rssi.setLatitude(Long.valueOf(dUser.getLatitude()));
		rssi.setLongitude(Long.valueOf(dUser.getLongitude()));
		rssi.setOperatorName(dUser.getOperatorName());
		rssi.setTimestamp(new Date(System.currentTimeMillis()));
		userService.saveRssiData(rssi);
		
		DUser resObj = new DUser();
		resObj.setMessage("Success");
		return new ResponseEntity<DUser>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/saveDataUsage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DUser> saveDataUsage(@RequestHeader("authToken") final String authToken,
			@RequestBody final DUser dUser) {
		if (!isValid(authToken)) {
			throw new ServiceUnauthorized("Invalid auth token");
		}
		User user = userService.findByUserId(Long.valueOf(dUser.getUserId()));
		if (user == null) {
			throw new ServiceUnauthorized("User unauthorized");
		}
		
		//userId, country, deviceId, mobileTx, mobileRx, wifiTx, wifiRx, longitude, latitude, operator_name, timestamp
		DataUsage dataUsage = new DataUsage();
		dataUsage.setUserId(user.getUserId());
		dataUsage.setCountry(dUser.getCountry());
		dataUsage.setDeviceId(dUser.getDeviceId());
		dataUsage.setMobileTx(dUser.getMobileTx());
		dataUsage.setMobileRx(dUser.getMobileRx());
		dataUsage.setWifiTx(dUser.getWifiTx());
		dataUsage.setWifiRx(dUser.getWifiRx());
		dataUsage.setLongitude(Double.valueOf(dUser.getLongitude()));
		dataUsage.setLatitude(Double.valueOf(dUser.getLatitude()));
		dataUsage.setOperatorName(dUser.getOperatorName());
		dataUsage.setTimestamp(new Date(System.currentTimeMillis()));
		
		userService.saveDataUsage(dataUsage);
		
		DUser resObj = new DUser();
		resObj.setMessage("Success");
		return new ResponseEntity<DUser>(resObj, HttpStatus.OK);
	}	

	private boolean isValid(String authToken) {
		return authToken.equals(AUTH_TOKEN);
	}

}
