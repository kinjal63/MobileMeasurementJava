package com.hk.mm.entity;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_notifications")
public class UserNotifications {

	private long id;

	private User user;
	private long neighbourId;
	private int notificationSent;
	private Date sentAt;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "neighbour_user_id")
	public long getNeighbourId() {
		return neighbourId;
	}

	public void setNeighbourId(long neighbourId) {
		this.neighbourId = neighbourId;
	}

	@Column(name = "notification_sent")
	public int getNotificationSent() {
		return notificationSent;
	}

	public void setNotificationSent(int notificationSent) {
		this.notificationSent = notificationSent;
	}

	@Column(name = "sent_at", columnDefinition = "DATETIME")
	public Date getSentAt() {
		return sentAt;
	}

	public void setSentAt(Date sentAt) {
		this.sentAt = sentAt;
	}

}
