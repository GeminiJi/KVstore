package com.dynamo.properties;

import com.dynamo.servers.ServerNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;


@Component
public class ServerNodeProperties {

	private ArrayList<ServerNode> nodesList;

	@Autowired
    Environment environment;

	@Autowired
	public ServerNodeProperties(@Value("${store.nodes.hosts}") String[] nodes,
                                @Value("${server.port}") String localServerPort) throws NumberFormatException, UnknownHostException {
		nodesList = new ArrayList<>();
		// localServerPort = environment.getProperty("server.port");
		String[] address;
		boolean self;
		for (int i = 0; i < nodes.length; i++) {
			address = nodes[i].split(":");
			self = InetAddress.getLocalHost().equals(InetAddress.getByName(address[0]))
					&& address[1].equals(localServerPort) ? true : false;
//			nodesList.add(new ServerNode(self, InetAddress.getByName(address[0]), address[1]));
			nodesList.add(new ServerNode(self, InetAddress.getByName(address[0]), address[1],
					Integer.parseInt(address[2])));
			System.out.println(nodesList.get(i).toString() + "  " + localServerPort);
		}
	}

	public ArrayList<ServerNode> getNodesList() {
		return nodesList;
	}
}
