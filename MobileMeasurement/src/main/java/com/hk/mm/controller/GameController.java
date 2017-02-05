package com.hk.mm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hk.mm.entity.User;
import com.hk.mm.entity.UserAvailability;
import com.hk.mm.exception.ServiceUnauthorized;
import com.hk.mm.service.GameService;
import com.hk.mm.service.UserService;
import com.hk.mm.vo.DGame;

@RestController
@RequestMapping(value = "/game")
public class GameController {

	private static final String AUTH_TOKEN = "AER934LJS9DFMER0KEFE";

	@Autowired
	private UserService userService;
	
	@Autowired
	private GameService gameService;

	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DGame> addGame(@RequestHeader("authToken") final String authToken,
			@RequestBody final DGame dGame) {

		if (!isValid(authToken)) {
			throw new ServiceUnauthorized("Invalid auth token");
		}

		DGame resObj = new DGame();
		resObj.setMessage("Success");
		return new ResponseEntity<DGame>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/getNearByGameList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DGame> getNearByGameList(@RequestHeader("authToken") final String authToken,
			@RequestBody final DGame dGame) {

		if (!isValid(authToken)) {
			throw new ServiceUnauthorized("Invalid auth token");
		}
		User user = userService.findByUserId(Long.valueOf(dGame.getUserId()));
		if (user == null) {
			throw new ServiceUnauthorized("Invalid user Id");
		}
		
		UserAvailability userAvailability = userService.findUserAvailable(user);
		gameService.getMutalGames();	
		DGame resObj = new DGame();
		resObj.setMessage("Success");
		return new ResponseEntity<DGame>(resObj, HttpStatus.OK);
	}

	private boolean isValid(String authToken) {
		return authToken.equals(AUTH_TOKEN);
	}

}
