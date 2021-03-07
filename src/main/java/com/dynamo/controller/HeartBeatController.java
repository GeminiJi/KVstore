package com.dynamo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HeartBeatController {
	@RequestMapping(value = "/heartbeat")
	public String heartbeat() {

		return "ACK";
	}
}
