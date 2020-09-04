package com.message.feed.application.web.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;

@Service
public class MessageFeedService {

	@Autowired
	private MessageRepository messageRepo;

	private long id = 0;

	MessageDigest md;
	String hash = "35454B055CC325EA1AF2126E27707052";
	String regex = "^[A-Z0-9+_.-]+@(.+)$";
	Pattern pattern;
	Matcher matcher;

	public MessageFeedService() throws NoSuchAlgorithmException {
		super();

		md = MessageDigest.getInstance("MD5");

	}

	public static LocalDateTime millsToLocalDateTime(long millis) {
		Instant instant = Instant.ofEpochMilli(millis);
		LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
		return date;
	}

	public List<MessageData> displayFeeds() {
		List<MessageData> listData = new ArrayList<>();
		messageRepo.findAll().forEach(listData::add);
		return listData;

	}

	public List<MessageData> viewSpecificMessagesForTime(String maxAge) {
		long timeLimit = System.currentTimeMillis() - (Integer.parseInt(maxAge) * 60 * 1000);
		LocalDateTime limitTime = millsToLocalDateTime(timeLimit);
		LocalDateTime timeNow = java.time.LocalDateTime.now();
		List<MessageData> eachEntry = new ArrayList<>();
		List<MessageData> sortedEntry = new ArrayList<>();
		messageRepo.findAll().forEach(eachEntry::add);
		for (int i = 0; i < eachEntry.size(); i++) {
			if (eachEntry.get(i).getUpdated().isAfter(limitTime) && eachEntry.get(i).getUpdated().isBefore(timeNow)) {
				sortedEntry.add(eachEntry.get(i));
			}
		}
		return sortedEntry;
	}

	public ResponseEntity<String> addNewMessage(MessageData message) {

		md.update((message.getMessageContent() + (id + 1)).getBytes());
		byte[] digest = md.digest();
		String myHash = DatatypeConverter.printHexBinary(digest);
		StringBuilder hashedValue = new StringBuilder(myHash);
		for (int i = 0; i < myHash.length(); i += 2)
			hashedValue.setCharAt(i, Character.toLowerCase(myHash.charAt(i)));
		message.setToken(hashedValue.toString());
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(message.getEmail());
		if (message.getName().length() <= 255 && message.getMessageContent().length() <= 512) {
			message.setId(++id);
			message.setCreated(java.time.LocalDateTime.now());
			message.setUpdated(java.time.LocalDateTime.now());
			messageRepo.save(message);
			return new ResponseEntity(HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<String>("Data not valid", HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity<String> updateMessage(MessageData message, String messageId) {
		String token = message.getToken();
		boolean found = false;
		List<MessageData> dataPresent = new ArrayList<>();
		MessageData matchedData = new MessageData();
		messageRepo.findAll().forEach(dataPresent::add);
		for (int i = 0; i < dataPresent.size(); i++) {
			if (("" + dataPresent.get(i).getId()).equals(messageId)) {
				matchedData = dataPresent.get(i);
				found = true;
			}
		}
		if (found) {
			if (matchedData.getToken().equals(token)) {
				message.setCreated(matchedData.getCreated());
				message.setEmail(matchedData.getEmail());
				message.setId(matchedData.getId());
				message.setName(matchedData.getName());
				message.setUpdated(java.time.LocalDateTime.now());
				messageRepo.save(message);

				return null;
			} else {
				return new ResponseEntity<String>("Invalid Token", HttpStatus.UNAUTHORIZED);
			}
		} else {
			return new ResponseEntity("Id Mismatch", HttpStatus.BAD_REQUEST);
		}

	}

}
