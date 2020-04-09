package com.travel.function.config;


import com.travel.function.entity.MiaoShaUser;
/**
 * @author 邱润泽 bullock
 */
public class UserContext {
	
	private static ThreadLocal<MiaoShaUser> userHolder = new ThreadLocal<MiaoShaUser>();
	
	public static void setUser(MiaoShaUser user) {
		userHolder.set(user);
	}
	
	public static MiaoShaUser getUser() {
		return userHolder.get();
	}

	public static void removeUser() {
		userHolder.remove();
	}

}
