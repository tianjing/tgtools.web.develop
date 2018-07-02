package tgtools.web.develop.websocket.listener.event;

import org.springframework.web.socket.WebSocketSession;
import tgtools.interfaces.Event;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 14:03
 */
public class RemoveClientEvent extends Event {

    public RemoveClientEvent(){}
    public RemoveClientEvent(String pLoginName,String pId,String pAddress)
    {
        loginName=pLoginName;
    }
    private String loginName;
    private String id;
    private String address;


    public String getLoginName() {
        return loginName;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }
}
