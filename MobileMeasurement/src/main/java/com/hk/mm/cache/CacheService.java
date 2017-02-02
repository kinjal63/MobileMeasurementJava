package com.hk.mm.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CacheService {

	private Map<Long,String> userCache = new HashMap<>();

	public Map<Long, String> getUserCache() {
		return userCache;
	}

	public void setUserCache(Map<Long, String> userCache) {
		this.userCache = userCache;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userCache == null) ? 0 : userCache.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CacheService other = (CacheService) obj;
		if (userCache == null) {
			if (other.userCache != null)
				return false;
		} else if (!userCache.equals(other.userCache))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CacheService [userCache=" + userCache + "]";
	}
	
	
}
