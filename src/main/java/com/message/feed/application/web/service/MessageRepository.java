package com.message.feed.application.web.service;

import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<MessageData, String> {

}
