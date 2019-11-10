package com.travel.commons.enums;

/**
 * @auther 邱润泽 bullock
 * @date 2019/11/8
 */
public class CustomerConstant {

    /**
     * user status
     */
    public  enum  LoginUserStatus {
        USER_ENABLE(1,"enable"),
        USER_DISABLE(0,"disbale");

        private int code;
        private String message;

        private LoginUserStatus(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    public static final String COOKIE_NAME_TOKEN = "token" ;
}
