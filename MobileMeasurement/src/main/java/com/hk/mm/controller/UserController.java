package com.hk.mm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hk.mm.entity.User;
import com.hk.mm.service.UserService;
import com.hk.mm.vo.DUser;
import com.hk.mm.vo.RequestVO;

@RestController
@RequestMapping(value="/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/register",method = RequestMethod.POST)
	public String registerUser(@RequestBody final RequestVO requestVO, @RequestParam("file") MultipartFile file)
	{
		if (!file.isEmpty()) {
			System.out.println("File is empty");
		}
		System.out.println("Request parames : " + requestVO.toString());
		return null;
	}
	
	@RequestMapping(value = "/login",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> userLogin(@RequestBody final DUser dUser)
	{
		User user = userService.findByEmail(dUser.getEmail());
		List<User> userList = userService.findAll();
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/updateAvailability",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DUser> updateUserAvailability(final DUser dUser)
	{
		return null;
	}
	
	@RequestMapping(value = "/addTime",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DUser> addUserTime(final DUser dUser)
	{
		return null;
	}
	
	@RequestMapping(value = "/sendRemoteUserInput",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DUser> sendRemoteUserInput(final DUser dUser)
	{
		return null;
	}
	
	@RequestMapping(value = "/uploadImage",method = RequestMethod.POST)
	public String uploadImage(@RequestBody final RequestVO requestVO, @RequestParam("file") MultipartFile file)
	{
		if (!file.isEmpty()) {
			System.out.println("File is empty");
		}
		System.out.println("Request parames : " + requestVO.toString());
		return null;
	}
	
	
}
