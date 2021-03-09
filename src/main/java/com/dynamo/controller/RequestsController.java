package com.dynamo.controller;

import com.dynamo.servers.ServerNode;
import com.dynamo.store.KeyValuePair;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class RequestsController {

	@Async
	public static void sendHeartBeatRequest(ServerNode serverNode) {
		final String uri = "http://{hostname}:{port}/heartbeat";
		Map<String, String> params = new HashMap<String, String>();
		params.put("hostname", serverNode.getAddress().getHostAddress().toString());
		params.put("port", serverNode.getPort());
		RestTemplate restTemplate = new RestTemplate();
		try {
			restTemplate.getForObject(uri, String.class, params);

		} catch (Exception e) {
			serverNode.setOnline(false);
			return;
		}
		serverNode.setOnline(true);
	}

	@Async
	public void sendStoreRecoverRequest(String addr, KeyValuePair kvp) throws JsonProcessingException {
		if (addr == null || addr.equals("")) {
			return;
		}

		String uri = "http://" + addr + "/set?key="+kvp.getKey() + "&val=" + kvp.getVal();
		RestTemplate restTemplate = new RestTemplate();
		try {
			restTemplate.getForObject(uri, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
