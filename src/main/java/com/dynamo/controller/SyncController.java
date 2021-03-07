package com.dynamo.controller;

import org.springframework.web.bind.annotation.*;

//import com.fasterxml.jackson.datatype.joda.JodaModule;

@RestController
public class SyncController {

//	@Autowired
//	private Store store;
//
//	@RequestMapping(value = "/sync/{key}")
//	public String set(@PathVariable String key, @RequestParam String val) {
//
//		KeyValuePair kvp = new KeyValuePair(key, val);
//		store.put(kvp);
//
//		return "Success";
//	}
//
//	@RequestMapping(value = "/sync", method = RequestMethod.POST)
//	public ResponseEntity<String> sync(@RequestBody String jsonKVP) {
//
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.registerModule(new JodaModule());
//		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//		try {
//			KeyValuePair kvp = mapper.readValue(jsonKVP, KeyValuePair.class);
//
//			store.put(kvp);
//
//			return new ResponseEntity<String>("success", HttpStatus.OK);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<String>("error", HttpStatus.BAD_REQUEST);
//		}
//	}
}
