package com.dynamo.controller;

import com.dynamo.store.BpStore;
import com.dynamo.store.KeyValuePair;
import com.dynamo.store.Store;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
public class SyncController {

	@Autowired
	private BpStore bpStore;

	@RequestMapping(value = "/sendSync")
	public String sendSync(@RequestParam String ip, @RequestParam String port) throws JsonProcessingException {
		RequestsController request = new RequestsController();
		for (KeyValuePair kvp : bpStore.values()) {
			request.sendStoreSyncRequest(ip+ ":" + port, kvp);
		}
		bpStore.clear();
		return "Recovery Success!";
	}
}
