package com.message.feed.application.web.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import javax.persistence.Id;

@Entity
public class MessageData {

	@Id
	public long id;
	public String messageContent;
	public String name;
	@Column(unique = true)
	public String email;
	public LocalDateTime created;
	public LocalDateTime updated;
	public String token;
	
	public MessageData() {
		
	}

	public MessageData(long id, String message, String name, String email, LocalDateTime created, LocalDateTime updated,String token) {
		super();
		this.id = id;
		this.messageContent = message;
		this.name = name;
		this.email = email;
		this.created = created;
		this.updated = updated;
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String message) {
		this.messageContent = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}
	
	
}
