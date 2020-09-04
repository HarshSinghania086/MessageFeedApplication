package com.message.feed.application.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageFeedController {

	@Autowired
	private MessageFeedService feedService;

	@RequestMapping("/messages")
	public List<MessageData> displayAllMessages() {
		return feedService.displayFeeds();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/messages")
	public void createNewMessage(@RequestBody MessageData data) {
		feedService.addNewMessage(data);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/messages/{messageId}")
	public ResponseEntity updateMessageData(@RequestBody MessageData data, @PathVariable String messageId) {
		return feedService.updateMessage(data, messageId);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/messages", params = { "maxAge" })
	public List<MessageData> returnDataForTime(@RequestParam("maxAge") String maxAge) {
		return feedService.viewSpecificMessagesForTime(maxAge);
	}

}
