package com.hk.mm.controller;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.hk.mm.entity.CallLogs;
import com.hk.mm.entity.User;
import com.hk.mm.exception.ServiceUnauthorized;
import com.hk.mm.service.AppService;
import com.hk.mm.service.UserService;
import com.hk.mm.vo.Country;
import com.hk.mm.vo.DApp;


@RestController
@RequestMapping(value = "/app")
public class AppController {

	private static final String AUTH_TOKEN = "AER934LJS9DFMER0KEFE";
	
	@Autowired
	private AppService appService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/aggregateCallDuration", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DApp> aggregateCallDuration(@RequestHeader("authToken") final String authToken,
			@RequestBody final DApp dApp) {
		if (!isValid(authToken)) {
			throw new ServiceUnauthorized("Invalid auth token");
		}
		User user = userService.findByUserId(Long.valueOf(dApp.getUserId()));
		if (user == null) {
			throw new ServiceUnauthorized("User unauthorized");
		}
		sendNotification();
		CallLogs callLogs = new CallLogs();
		appService.aggregateCallDuration(callLogs);	
		
		DApp resObj = new DApp();
		resObj.setMessage("Success");
		return new ResponseEntity<DApp>(resObj, HttpStatus.OK);
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

	
	static class NullHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
           return true;
        }
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

	private boolean isValid(String authToken) {
		return authToken.equals(AUTH_TOKEN);
	}
}
