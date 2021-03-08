package com.dynamo.store;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BpStore {
	private final ConcurrentHashMap<String, KeyValuePair> bpStore = new ConcurrentHashMap<>();

	public void put(KeyValuePair kvp) {
		if (bpStore.containsKey(kvp.getKey())) {
			bpStore.put(kvp.getKey(), kvp);
		} else {
			bpStore.put(kvp.getKey(), kvp);
		}
	}

	public KeyValuePair get(String key) {
		if (bpStore.containsKey(key)) {
			return bpStore.get(key);
		}
		return null;
	}

	public Collection<KeyValuePair> values() {
		return bpStore.values();
	}

	public void clear() {
		bpStore.clear();
	}

}
