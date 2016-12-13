package org.arpit.java2blog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"success", "ad_type", "placement_id", "tracking_code", "ad_details",
"ad_priority"})

public class UserResponse {
	
	public UserResponse(int success, long userId) {
		this.success = success;
		this.userId = userId;
	}
	
	@JsonProperty("success")
	private int success;

	@JsonProperty("user_id")
	private long userId;
}
