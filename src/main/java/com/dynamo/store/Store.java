package com.dynamo.store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.util.Scanner;


@Component
public class Store {
	private final String filePath;
	private final ConcurrentHashMap<String, KeyValuePair> store = new ConcurrentHashMap<>();

	public Store(@Value("${server.port}") String localServerPort) throws IOException {
		filePath = "./" + localServerPort;
		File logFile = new File(filePath);
		if (logFile.exists()) {
			System.out.println("Found log file, recover from it...");
			Scanner reader = new Scanner(logFile);
			while (reader.hasNextLine()) {
				String data = reader.nextLine();
				String[] kv = data.split("->");
				KeyValuePair kvp = new KeyValuePair(kv[0], kv[1]);
				store.put(kvp.getKey(), kvp);
				System.out.println("Recovered: " + data);
			}
			reader.close();
		} else {
			System.out.println("No log file found, create a new one: " + filePath);
			logFile.createNewFile();
		}
	}

	public void put(KeyValuePair kvp) throws IOException {
		FileWriter fw = new FileWriter(filePath, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(kvp.getKey() + "->" + kvp.getVal());
		bw.newLine();
		bw.close();

		store.put(kvp.getKey(), kvp);
	}

	public KeyValuePair get(String key) {
		if (store.containsKey(key)) {
			return store.get(key);
		}
		return null;
	}
}
