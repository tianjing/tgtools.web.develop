package tgtools.web.develop.message;

/**
 * 通用接口层基础通信json 类型
 * @author 田径
 * @Title
 * @Description
 * @date 12:12
 */
public class ValidMessage {
    private String mToken;
    private String mUser;
    private String mOperation;

    public String getToken() {
        return mToken;
    }

    public void setToken(String pToken) {
        mToken = pToken;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String pUser) {
        mUser = pUser;
    }

    public String getOperation() {
        return mOperation;
    }

    public void setOperation(String pOperation) {
        mOperation = pOperation;
    }
}
