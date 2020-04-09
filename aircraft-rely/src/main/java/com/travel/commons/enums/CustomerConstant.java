package com.travel.commons.enums;

/**
 * @auther 邱润泽 bullock
 * @date 2019/11/8
 */
public class CustomerConstant {

    //正在进行中
    public static final Integer MS_ING = 0;
    //
    public static final Integer MS_S = 1;
    //秒杀失败
    public static final Integer MS_F = -1;

    //秒杀前缀
    public static final class RedisKeyPrefix {

        public static final String PRODUCT_STOCK = "product_stock";

        public static final String PRODUCT = "product";

        public static final String MIAOSHA_ORDER = "miaosha_order";

        public static final String MIAOSHA_ORDER_WAIT = "miaosha_order_wait";

        public static final String MIAOSHA_VERIFY_CODE = "miaosha_verify_code";

        public static final String MIAOSHA_ORDER_TOKEN = "miaosha_order_token";

    }
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


    public  enum  MiaoShaStatus {
        MIAO_SHA_NOT_START(0,"未开始秒杀"),
        MIAO_SHA_START(1,"开始秒杀"),
        MIAO_SHA_END(2,"秒杀结束");

        private int code;
        private String message;

        private MiaoShaStatus(int code, String message) {
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


    public static final class ZookeeperPathPrefix {

        public static final String PRODUCT_SOLD_OUT = "/product_sold_out";

        public static String getZKSoldOutProductPath(String goodId) {
            return ZookeeperPathPrefix.PRODUCT_SOLD_OUT + "/" + goodId;
        }

    }

    public static final class ProductType {
        public static final String PRODUCT_MIAOSHA = "miaosha";

        public static final String PRODUCT_HOT = "hot";
    }
}
