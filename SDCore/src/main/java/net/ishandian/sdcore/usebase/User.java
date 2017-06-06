package net.ishandian.sdcore.usebase;

/**
 * 类名：用户类
 *
 * @Author: huangdianhua on 2017/5/17 16:09.
 * E-mail: hdh.mr.666@gmail.com
 */

public class User {
    private String phoneNumStr = "";//账号
    private String passwordStr = "";//密码
    private String userId = "";//用户ID
    private String userName = "";//用户名称

    public String getPhoneNumStr() {
        return phoneNumStr;
    }

    public void setPhoneNumStr(String phoneNumStr) {
        this.phoneNumStr = phoneNumStr;
    }

    public String getPasswordStr() {
        return passwordStr;
    }

    public void setPasswordStr(String passwordStr) {
        this.passwordStr = passwordStr;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "phoneNumStr='" + phoneNumStr + '\'' +
                ", passwordStr='" + passwordStr + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    /**
     * 清理数据
     */
    public void clearDate() {
        setPhoneNumStr("");
        setUserName("");
        setUserId("");
        setPasswordStr("");
    }
}
