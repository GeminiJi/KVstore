package com.dynamo.servers;

import com.dynamo.controller.RequestsController;
import com.dynamo.properties.ServerNodeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;


@Service
@EnableScheduling
public class Observer {

	@Autowired
	private ServerNodeProperties serverNodeProperties;

	private ArrayList<ServerNode> nodesList;

	public Observer() {
	}

	@PostConstruct
	public void init() {
		this.nodesList = serverNodeProperties.getNodesList();
	}

	@Scheduled(fixedRate = 2000)
	public void run() {
		// System.out.println(nodesList.get(0).toString());
		for (ServerNode node : nodesList) {
			RequestsController.sendHeartBeatRequest(node);
		}
	}

	public ArrayList<ServerNode> getAvailableNodes() {
		ArrayList<ServerNode> serverNodeList = new ArrayList<>();
		for (ServerNode node : nodesList) {
			if (node.isOnline()) {
				serverNodeList.add(node);
			}
		}
		return serverNodeList;
	}
}
