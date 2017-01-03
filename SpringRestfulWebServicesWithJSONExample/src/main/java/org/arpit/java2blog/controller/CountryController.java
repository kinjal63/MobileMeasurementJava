package org.arpit.java2blog.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.arpit.java2blog.bean.Country;
import org.arpit.java2blog.model.AppData;
import org.arpit.java2blog.model.CallDuration;
import org.arpit.java2blog.model.GameData;
import org.arpit.java2blog.model.GameInfo;
import org.arpit.java2blog.model.LoginREQ;
import org.arpit.java2blog.model.ResponseGame;
import org.arpit.java2blog.model.User;
import org.arpit.java2blog.model.UserAvailablity;
import org.arpit.java2blog.model.UserConnectionInfo;
import org.arpit.java2blog.model.UserDataUsage;
import org.arpit.java2blog.model.UserInfo;
import org.arpit.java2blog.model.UserInput;
import org.arpit.java2blog.model.UserRSSI;
import org.arpit.java2blog.model.UserResponse;
import org.arpit.java2blog.model.UserTimeModel;
import org.arpit.java2blog.service.RssiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
public class CountryController {
	
	@MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return "Helloserver";
    }
	
	@RequestMapping(value = "/countries", method = RequestMethod.GET,headers="Accept=application/json")
	public List<Country> getCountries()
	{
		List<Country> listOfCountries = new ArrayList<Country>();
		listOfCountries=createCountryList();
		return listOfCountries;
	}

	@RequestMapping(value = "/country/{id}", method = RequestMethod.GET,headers="Accept=application/json")
	public Country getCountryById(@PathVariable int id)
	{
		List<Country> listOfCountries = new ArrayList<Country>();
		listOfCountries=createCountryList();

		for (Country country: listOfCountries) {
			if(country.getId()==id)
				return country;
		}
		
		return null;
	}

	@Autowired
	RssiService rssiService;
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public
	@ResponseBody
	Object createUser(@RequestParam("file") MultipartFile file, @RequestParam("firstname") String firstname,
			@RequestParam("lastname") String lastname, @RequestParam("password") String password,
			@RequestParam("email") String email, @RequestParam("device_token") String deviceToken, MultipartHttpServletRequest request) {
		User user = new User();
		user.setFile(file);
		user.setFirstname(firstname);
		user.setLastname(lastname);
		user.setPassword(password);
		user.setEmail(email);
		user.setDeviceToken(deviceToken);
		int userId = rssiService.insert(user);
	
		UserResponse userResponse = new UserResponse(1, userId);
		return userResponse;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public
	@ResponseBody
	UserResponse registerUser(@RequestBody LoginREQ loginREQ, HttpServletRequest httpSerfvletRequest ) {
		User user = rssiService.findByEmail(loginREQ.getEmail());
		UserResponse userResponse = new UserResponse(1, user.getId());
		return userResponse;
	}
	
	@RequestMapping(value = "/recordRSSI", method = RequestMethod.POST)
	public
	@ResponseBody
	Object recordRSSI(@RequestBody UserRSSI rssiREQ, HttpServletRequest httpSerfvletRequest ) {
		rssiService.saveRssi(rssiREQ);
		return "Rssi and location info is logged";
	}
	
	@RequestMapping(value = "/saveDataUsage", method = RequestMethod.POST)
	public
	@ResponseBody
	Object recordMobileDataUsage(@RequestBody UserDataUsage dataUsage, HttpServletRequest httpSerfvletRequest ) {
		rssiService.saveDataUsage(dataUsage);
		return "Data usage is logged for userid : ";
	}
	
	@RequestMapping(value = "/aggregateCallDuration", method = RequestMethod.POST)
	public
	@ResponseBody
	Object aggregateCallDuration(@RequestBody CallDuration callDuration, HttpServletRequest httpSerfvletRequest ) {
		sendNotification();
		rssiService.aggregateCallDuration(callDuration);
		return "Data usage is logged for userid : ";
	}
	
	@RequestMapping(value = "/saveAppData", method = RequestMethod.POST)
	public
	@ResponseBody
	Object saveAppData(@RequestBody AppData appData, HttpServletRequest httpSerfvletRequest ) {
		rssiService.saveAppData(appData);
		return "App data are logged for userid : ";
	}
	
	@RequestMapping(value = "/addGame", method = RequestMethod.POST)
	public
	@ResponseBody
	Object addGameInfo(@RequestParam("gameImagePath") MultipartFile file, @RequestParam("gameName") String gameName,
			@RequestParam("gamePublisherName") String gamePublisherName, @RequestParam("gamePackageName") String gamePackageName,
			@RequestParam("gameStudioName") String gameStudioName, @RequestParam("ageRating") String ageRating,
			@RequestParam("osType") String osType, @RequestParam("networkType") String networkType,
			@RequestParam("minPlayers") String minPlayers, @RequestParam("maxPlayers") String maxPlayers,
			MultipartHttpServletRequest httpSerfvletRequest ) {
		
		GameData gameData = new GameData();
		gameData.setAgeRating(ageRating);
		gameData.setGameName(gameName);
		gameData.setGameImagePath(file);
		gameData.setGamePublisherName(gamePublisherName);
		gameData.setGamePackageName(gamePackageName);
		gameData.setGameStudioName(gameStudioName);
		gameData.setAgeRating(ageRating);
		gameData.setOsType(Integer.parseInt(osType));
		gameData.setNetworkType(Integer.parseInt(networkType));
		gameData.setMinPlayers(minPlayers);
		gameData.setMaxPlayers(maxPlayers);
		
		rssiService.addGameInfo(gameData);
		return "Game Data is inserted : ";
	}
	
	@RequestMapping(value = "/updateUserAvailablity", method = RequestMethod.POST)
	public
	@ResponseBody
	Object updateUserAvailablity(@RequestBody UserAvailablity availablity, HttpServletRequest httpSerfvletRequest ) {
		rssiService.updateUserAvailablity(availablity);
		return "App data are logged for userid : ";
	}
	
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public @ResponseBody
	Object handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("user_id") long userId,
			MultipartHttpServletRequest request) {
		
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream = 
                        new BufferedOutputStream(new FileOutputStream(new File("D:/Images/" + userId + "-uploaded")));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded for user ->" + userId + " into " + userId + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload for user ->" + userId + " => " + e.getMessage();
            }
        }
		return "Image is uploaded : ";
	}
	
	@RequestMapping(value = "/getNearByGameList", method = RequestMethod.POST)
	public @ResponseBody
	Object getMutualGameList(@RequestParam("user_id") long userId, HttpServletRequest httpSerfvletRequest) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserDetails(rssiService.getMutualGameList(userId));
		return userInfo;
	}
	
	@RequestMapping(value = "/getGameList", method = RequestMethod.POST)
	public @ResponseBody
	Object getGameList(HttpServletRequest httpSerfvletRequest) {
		GameInfo gameInfo = new GameInfo();
		gameInfo.setGameDetails(rssiService.getGameList());
		return gameInfo;
	}
	
	@RequestMapping(value = "/editGame", method = RequestMethod.POST)
	public
	@ResponseBody
	Object editGameInfo(@RequestParam("gameImagePath") MultipartFile file, @RequestParam("gameId") String gameId,
			@RequestParam("gameName") String gameName,
			@RequestParam("gamePublisherName") String gamePublisherName, @RequestParam("gamePackageName") String gamePackageName,
			@RequestParam("gameStudioName") String gameStudioName, @RequestParam("ageRating") String ageRating,
			@RequestParam("osType") String osType, @RequestParam("networkType") String networkType,
			@RequestParam("minPlayers") String minPlayers, @RequestParam("maxPlayers") String maxPlayers,
			MultipartHttpServletRequest httpSerfvletRequest ) {
		
		GameData gameData = new GameData();
		gameData.setAgeRating(ageRating);
		gameData.setGameId(Long.parseLong(gameId));
		gameData.setGameName(gameName);
		gameData.setGameImagePath(file);
		gameData.setGamePublisherName(gamePublisherName);
		gameData.setGamePackageName(gamePackageName);
		gameData.setGameStudioName(gameStudioName);
		gameData.setAgeRating(ageRating);
		gameData.setOsType(Integer.parseInt(osType));
		gameData.setNetworkType(Integer.parseInt(networkType));
		gameData.setMinPlayers(minPlayers);
		gameData.setMaxPlayers(maxPlayers);
		
		rssiService.editGame(gameData);
		return "Game Data is inserted : ";
	}
	
	@RequestMapping(value = "/deleteGame", method = RequestMethod.POST)
	public @ResponseBody
	Object deleteGame(@RequestParam("gameId") long gameId, HttpServletRequest httpSerfvletRequest) {
		rssiService.deleteGame(gameId);
		return "Game is deleted.";
	}
	
	@RequestMapping(value = "/sendConnectionInvite", method = RequestMethod.POST)
	public @ResponseBody
	Object sendConnectionInvite(@RequestBody UserConnectionInfo userConnectionInfo, HttpServletRequest httpSerfvletRequest) {
		rssiService.sendConnectionInvite(userConnectionInfo);
		return "Invitation is sent.";
	}
	
	@RequestMapping(value = "/sendRemoteUserInput", method = RequestMethod.POST)
	public @ResponseBody
	Object sendRemoteUserInput(@RequestParam("userId") long userId, @RequestParam("toUserId") long toUserId,
			@RequestParam("bluetoothAddress") String bluetoothAddress,
			@RequestParam("accept") boolean accept, HttpServletRequest httpSerfvletRequest) {
		UserInput userInput = new UserInput();
		userInput.setFromUserId(userId);
		userInput.setToUserId(toUserId);
		userInput.setBluetoothAddress(bluetoothAddress);
		userInput.setAccept(accept);
		
		rssiService.sendRemoteUserInput(userInput);
		return "Invitation is sent.";
	}
	
	@RequestMapping(value = "/getMutualGames", method = RequestMethod.POST)
	public @ResponseBody
	Object getMutualGames(@RequestBody UserConnectionInfo userConnectionInfo,
			HttpServletRequest httpSerfvletRequest) {
		ResponseGame resGame = new ResponseGame();
		resGame.setUserDetails(rssiService.getMutualGameList(userConnectionInfo.getUserId(), userConnectionInfo.getRemoteUserIds()));
		
		return resGame;
	}
	
	@RequestMapping(value = "/addUserTime", method = RequestMethod.POST)
	public @ResponseBody
	Object addUserTime(@RequestParam("userId") long userId, @RequestParam("fromTime") String fromTime,
			@RequestParam("toTime") String toTime,
			HttpServletRequest httpSerfvletRequest) {
		rssiService.addUserAvailabilityTime(userId, fromTime, toTime);
		
		return "Time entry is added.";
	}
	
	private static String sendNotification()
	{
		final String uri = "https://api.instapush.im/v1/post";
	     
		NullHostnameVerifier verifier = new NullHostnameVerifier(); 
		MySimpleClientHttpRequestFactory requestFactory = new  MySimpleClientHttpRequestFactory(verifier,null);
		
	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.setRequestFactory(requestFactory);
	    
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    headers.set("x-instapush-appid", "581c19e0a4c48ac98ab0390b");
	    headers.set("x-instapush-appsecret", "8ae91fa6799a86b2d8e00183892fb234");
	    headers.set("Content-Type", "application/json");
	    
	    String str = "{\"event\":\"test\",\"trackers\":{\"tracker\":\"Data is submitted for user. You can track it here... http://www.google.com/\"}}";
//	    		+ "Transmitted bytes: + transmitted_bytes + \", Received bytes:\" + received_bytes + \"}}";
	    HttpEntity<String> entity = new HttpEntity(str, headers);
	    entity.getBody();
	     
	    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
	     
	    return result.getBody();
	}
	
	static class MySimpleClientHttpRequestFactory extends SimpleClientHttpRequestFactory {

        private final HostnameVerifier verifier;
        private final String cookie;

        public MySimpleClientHttpRequestFactory(HostnameVerifier verifier ,String cookie) {
            this.verifier = verifier;
            this.cookie = cookie;
        }

        @Override
        protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
            if (connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) connection).setHostnameVerifier(verifier);
                ((HttpsURLConnection) connection).setSSLSocketFactory(trustSelfSignedSSL().getSocketFactory());
                ((HttpsURLConnection) connection).setAllowUserInteraction(true);
                String rememberMeCookie = cookie == null ? "" : cookie; 
                ((HttpsURLConnection) connection).setRequestProperty("Cookie", rememberMeCookie);
            }
            super.prepareConnection(connection, httpMethod);
        }

        public SSLContext trustSelfSignedSSL() {
            try {
                SSLContext ctx = SSLContext.getInstance("TLS");
                X509TrustManager tm = new X509TrustManager() {

                    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                };
                ctx.init(null, new TrustManager[] { tm }, null);
                SSLContext.setDefault(ctx);
                return ctx;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

    }


    static class NullHostnameVerifier implements HostnameVerifier {
           public boolean verify(String hostname, SSLSession session) {
              return true;
           }
        }
	
// Utiliy method to create country list.
	public List<Country> createCountryList()
	{
		Country indiaCountry=new Country(1, "India");
		Country chinaCountry=new Country(4, "China");
		Country nepalCountry=new Country(3, "Nepal");
		Country bhutanCountry=new Country(2, "Bhutan");

		List<Country> listOfCountries = new ArrayList<Country>();
		listOfCountries.add(indiaCountry);
		listOfCountries.add(chinaCountry);
		listOfCountries.add(nepalCountry);
		listOfCountries.add(bhutanCountry);
		return listOfCountries;
	}
}
