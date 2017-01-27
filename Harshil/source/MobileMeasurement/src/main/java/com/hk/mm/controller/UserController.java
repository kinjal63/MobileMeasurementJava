package com.hk.mm.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hk.mm.vo.RequestVO;

@RestController
@RequestMapping(value="/user")
public class UserController {

	@RequestMapping(value = "/register",method = RequestMethod.GET)
	public String registerUser(@RequestBody final RequestVO requestVO, @RequestParam("file") MultipartFile file)
	{
		if (!file.isEmpty()) {
			System.out.println("File is empty");
		}
		System.out.println("Request parames : " + requestVO.toString());
		return null;
	}
}
