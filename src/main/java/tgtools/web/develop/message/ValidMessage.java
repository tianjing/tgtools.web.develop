package tgtools.web.develop.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tgtools.exceptions.APPErrorException;
import tgtools.json.JSONObject;
import tgtools.web.develop.util.JsonObjectDeserializer;


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

    @JsonDeserialize(using=JsonObjectDeserializer.class)
    @JsonProperty(value="data")
    private JSONObject mData;

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

    public JSONObject getData() {
        return mData;
    }

    public void setData(JSONObject pData) {
        this.mData = pData;
    }

   }
