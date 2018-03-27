package tgtools.web.develop.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tgtools.exceptions.APPErrorException;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 客户端容器
 * @author 田径
 * @Title
 * @Description
 * @date 16:05
 */
public class ClientFactory {
    private static ConcurrentHashMap<String,WebSocketSession> mClients=new ConcurrentHashMap<String,WebSocketSession>();

    /**
     * 添加一个客户端
     * @param pUserName
     * @param pClient
     */
    public static void addClient(String pUserName, WebSocketSession pClient)
    {
        if(!mClients.containsKey(pUserName))
        {
            mClients.put(pUserName,pClient);
        }
    }

    /**
     * 根据value 删除客户端
     * @param pClient
     */
    public static void removeClient(WebSocketSession pClient)
    {
        for(ConcurrentHashMap.Entry<String,WebSocketSession> item :mClients.entrySet())
        {
            if(item.getValue().equals(pClient))
            {
                mClients.remove(item.getKey());
                return;
            }
        }
    }

    /**
     * 根据用户名删除客户端
     * @param pUserName
     */
    public static void removeClient(String pUserName)
    {
        if(mClients.containsKey(pUserName))
        {
            mClients.remove(pUserName);
        }
    }

    /**
     * 发送信息
     * @param pUserName
     * @param pMessage
     * @throws APPErrorException
     */
    public static void sendMessage(String pUserName, TextMessage pMessage) throws APPErrorException {
        if(mClients.containsKey(pUserName))
        {
            try {
                mClients.get(pUserName).sendMessage(pMessage);
            } catch (IOException e) {
                throw new APPErrorException("发送消息失败；原因："+e.getMessage(),e);
            }
        }
    }

    /**
     * 发送信息
     * @param pUserName
     * @param pMessage
     * @throws APPErrorException
     */
    public static void sendMessage(String pUserName, String pMessage) throws APPErrorException {
        if(mClients.containsKey(pUserName))
        {
            try {
                mClients.get(pUserName).sendMessage(new TextMessage(pMessage));
            } catch (IOException e) {
                throw new APPErrorException("发送消息失败；原因："+e.getMessage(),e);
            }
        }
    }
}
