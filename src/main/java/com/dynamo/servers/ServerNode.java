package com.dynamo.servers;

import java.net.InetAddress;


public class ServerNode {
	private boolean online;
	private InetAddress address;
	private String port;
	private int position;
	private boolean self;

	public ServerNode(boolean self, InetAddress address, String port, int position) {
		this.self = self;
		this.address = address;
		this.online = false;
		this.port = port;
		this.position = position;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		if (this.online != online) {
			this.online = online;
			System.out.println(
					"Server at " + address.getHostName() + ":" + port + " is now " + (online ? "online" : "offline"));
		}
	}

	public InetAddress getAddress() {
		return address;
	}

	public boolean isSelf() {
		return self;
	}

	public String getPort() {
		return port;
	}

	public int getPosition() { return position; }

	@Override
	public String toString() {
		return "ServerNode [online=" + online + ", address=" + address + ", port=" + port + ", self=" + self + "]";
	}

}
