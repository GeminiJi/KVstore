package com.dynamo.controller;

import com.dynamo.properties.ServerNodeProperties;
import com.dynamo.servers.ServerNode;
import com.dynamo.store.KeyValuePair;
import com.dynamo.store.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


@RestController
public class StoreController {

	@Autowired
	private Store store;

	@Autowired
	private ServerNodeProperties serverNodeProperties;

	@Value("${quorum.N}")
	private int N;

	@Value("${quorum.R}")
	private int R;

	@Value("${quorum.W}")
	private int W;

	@RequestMapping(value = "/get")
	public String get(@RequestParam String key) throws InterruptedException {
		int hashKey = key.hashCode() % 100;
		ArrayList<ServerNode> nodesList = serverNodeProperties.getNodesList();
		int ring = nodesList.size();
		int index = hashKey % ring;
		System.out.println(index);
		ServerNode curNode = nodesList.get(index);
		System.out.println("Self :" +curNode.isSelf());
		RestTemplate restTemplate = new RestTemplate();
		String result = "Waiting for the response!";
		if (!curNode.isSelf()) {
			String forwardURL = "http://" + curNode.getAddress().getHostAddress() + ":" + curNode.getPort()
					+ "/get?key=" + key;
			System.out.println("URL : " + forwardURL);
			result = restTemplate.getForObject(forwardURL, String.class);
			System.out.println("Return " + result);
		} else {
			final CountDownLatch readLatch = new CountDownLatch(R);
			List<String> vectorClock = new ArrayList<>();

			int next = 0;
			while (next < N) {
				ServerNode node = nodesList.get((index + next) % ring);
				String getURL = "http://" + node.getAddress().getHostAddress() + ":" + node.getPort() + "/query?key=" + key;
				ReadThread readThread = new ReadThread(getURL, readLatch, vectorClock);
				readThread.start();
				next++;
			}
			readLatch.await();
			System.out.println("vectorClock.size() : " + vectorClock.size());
			if (vectorClock.size() == 0) {
				result = "Key : " + key + " does not exist!";
			} else {
				result = "Key : " + key + " value is " + vectorClock.get(0);
			}
		}
		System.out.println("FINAL RESULT8080 : " + result);
		return result;
	}

	private int getLastestVersion(int index, String key) throws InterruptedException {
		ArrayList<ServerNode> nodesList = serverNodeProperties.getNodesList();
		int ring = nodesList.size();
		System.out.println(index);

		final CountDownLatch readLatch = new CountDownLatch(R);
		List<String> vectorClock = new ArrayList<>();

		int next = 0;
		while (next < N) {
			ServerNode node = nodesList.get((index + next) % ring);
			String getURL = "http://" + node.getAddress().getHostAddress() + ":" + node.getPort() + "/query?key=" + key;
			ReadThread readThread = new ReadThread(getURL, readLatch, vectorClock);
			readThread.start();
			next++;
		}
		readLatch.await();
		if (vectorClock.size() == 0) {
			return 0;
		}
		return Integer.parseInt(vectorClock.get(2));
	}

	@RequestMapping(value = "/query")
	public String query(@RequestParam String key) {
		KeyValuePair kvp = store.get(key);
		if (kvp == null) {
			return "None";
		} else {
			return key + ";" + kvp.getVal();
		}
	}

	@RequestMapping(value = "/put")
	public String put(@RequestParam String key, @RequestParam String val) throws InterruptedException {
		int hashKey = key.hashCode() % 100;
		ArrayList<ServerNode> nodesList = serverNodeProperties.getNodesList();
		int ring = nodesList.size();
		int index = hashKey % ring;
		System.out.println(index);
		ServerNode curNode = nodesList.get(index);
		System.out.println("Self :" +curNode.isSelf());
		RestTemplate restTemplate = new RestTemplate();
		String result = "Waiting for the response!";
		if (!curNode.isSelf()) {
			String forwardURL = "http://" + curNode.getAddress().getHostAddress() + ":" + curNode.getPort()
					+ "/put?key=" + key + "&val=" + val;
			System.out.println("URL : " + forwardURL);
			result = restTemplate.getForObject(forwardURL, String.class);
			System.out.println("Return " + result);
		} else {
			int version = getLastestVersion(index, key) + 1;
			System.out.println("LatestVersion : " + version);
			final CountDownLatch writeLatch = new CountDownLatch(W);
			int next = 0;
			while (next < N) {
				ServerNode node = nodesList.get((index + next) % ring);
				String addr = node.getAddress().getHostAddress() + ":" + node.getPort();
				String copyURL = "http://" + addr + "/set?key=" + key + "&val=" + hashKey + ";" + val + ";" + addr + ";" + version;
				WriteThread writeThread = new WriteThread(copyURL, writeLatch);
				writeThread.start();
				next++;
			}
			writeLatch.await();
			result = "Put Success";
		}
		return result;
	}

	@RequestMapping(value = "/set")
	public Boolean set(@RequestParam String key, @RequestParam String val) {
		// value : 0 is hashkey, 1 is value, 2 is IP:Port, 3 is counter
		KeyValuePair kvp = new KeyValuePair(key, val);
		store.put(kvp);
		System.out.println("Insert Key8080 : " + key + " Val : " +val);
		return true;
	}
}