package tgtools.web.develop.websocket.listener.event;

import tgtools.interfaces.Event;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 14:03
 */
public class RemoveClientEvent extends Event {

    public RemoveClientEvent(){}
    public RemoveClientEvent(String pLoginName)
    {
        mLoginName=pLoginName;
    }
    private String mLoginName;

    public String getLoginName() {
        return mLoginName;
    }
}
