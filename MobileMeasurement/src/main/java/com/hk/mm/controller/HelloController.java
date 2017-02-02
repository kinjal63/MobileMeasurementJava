package com.hk.mm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@RequestMapping(value = "/Hello")
	public String sayHello() {
		return "Hello";
	}
}
