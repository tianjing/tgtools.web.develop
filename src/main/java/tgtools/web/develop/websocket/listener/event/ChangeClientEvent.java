package tgtools.web.develop.websocket.listener.event;

import org.springframework.web.socket.WebSocketSession;
import tgtools.interfaces.Event;
import tgtools.web.develop.websocket.ClientFactory;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 14:03
 */
public class ChangeClientEvent extends Event {
    public ChangeClientEvent(){}
    public ChangeClientEvent(String pLoginName, WebSocketSession pNewClient, WebSocketSession pOldClient,boolean pCancelChange)
    {
        mLoginName=pLoginName;
        mCancelChange=pCancelChange;
        mNewClient=pNewClient;
        mOldClient=pOldClient;
    }

    /**
     * 是否取消保存
     */
    private boolean mCancelChange =false;
    /**
     * 登录名称
     */
    private String mLoginName;
    /**
     * 连接对象
     */
    private WebSocketSession mNewClient;
    private WebSocketSession mOldClient;

    public WebSocketSession getNewClient() {
        return mNewClient;
    }

    public WebSocketSession getOldClient() {
        return mOldClient;
    }

    public String getLoginName() {
        return mLoginName;
    }

    public boolean getCancelChange() {
        return mCancelChange;
    }

    public void setCancelChange(boolean pCancelChange) {
        mCancelChange = pCancelChange;
    }

}
