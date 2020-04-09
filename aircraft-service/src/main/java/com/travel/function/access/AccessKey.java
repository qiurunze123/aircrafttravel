package com.travel.function.access;

import com.travel.function.redisManager.BasePrefix;

/**
 * @author 邱润泽 bullock
 */
public class AccessKey extends BasePrefix {

	private AccessKey( int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	
	public static AccessKey withExpire(int expireSeconds) {
		return new AccessKey(expireSeconds, "access");
	}
	
}
