package com.mmall.common;

public class Const {
    public static final String CURRENT_USER="current_User";
    public static final String EMALL="email";
    public static final String USERNAEM="username";

    /**
     * 用   户  0
     * 管理员   1
     */
    public interface Role{
        int ROLE_CUSTOMER=0;
        int ROLE_ADMIN=1;
    }
    public enum productStatusEnum{
        ON_SALE(1,"在售");
        private int code;
        private String desc;

        productStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

}
