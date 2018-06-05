package tgtools.web.develop.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tgtools.exceptions.APPErrorException;
import tgtools.util.LogHelper;
import tgtools.web.develop.websocket.listener.ClientFactoryListener;
import tgtools.web.develop.websocket.listener.event.AddClientEvent;
import tgtools.web.develop.websocket.listener.event.ChangeClientEvent;
import tgtools.web.develop.websocket.listener.event.RemoveClientEvent;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 客户端容器
 *
 * @author 田径
 * @Title
 * @Description
 * @date 16:05
 */
public class ClientFactory implements Closeable {
    protected ConcurrentHashMap<String, WebSocketSession> mClients = new ConcurrentHashMap<String, WebSocketSession>();

    protected ClientFactoryListener mClientFactoryListener;

    /**
     * 是否存在用户连接
     * @param pLoginName 登录名
     * @return
     */
    public boolean hasClient(String pLoginName)
    {
        return mClients.containsKey(pLoginName);
    }

    /**
     * 是否存在用户连接
     * @param pWebSocketSession 连接对象
     * @return
     */
    public boolean hasClient(WebSocketSession pWebSocketSession)
    {
        return mClients.containsValue(pWebSocketSession);
    }

    public void setClientFactoryListener(ClientFactoryListener pClientFactoryListener) {
        mClientFactoryListener = pClientFactoryListener;
    }

    /**
     * 添加一个客户端
     *
     * @param pUserName
     * @param pClient
     */
    public void addClient(String pUserName, WebSocketSession pClient) {
        if (!mClients.containsKey(pUserName)) {
            LogHelper.info("", "增加客户端；name:" + pUserName, "MyClientFactory.addClient");
            mClients.put(pUserName, pClient);
            onAddClient(pUserName, pClient);
        } else {
            if (!mClients.get(pUserName).getId().equals(pClient.getId()) && mClients.get(pUserName).isOpen()) {
                changeClient(pUserName, pClient);
            }
        }
    }

    public void changeClient(String pUserName, WebSocketSession pClient) {
        ChangeClientEvent event=new ChangeClientEvent(pUserName,pClient,false);
        onChangeClient(event);
        if(!event.getCancelChange()) {
            mClients.remove(pUserName);
            mClients.put(pUserName, pClient);
        }
    }


    /**
     * 根据value 删除客户端
     *
     * @param pClient
     */
    public void removeClient(WebSocketSession pClient) {
        for (ConcurrentHashMap.Entry<String, WebSocketSession> item : mClients.entrySet()) {
            if (item.getValue().equals(pClient)) {
                mClients.remove(item.getKey());
                onRemoveClient(item.getKey());
                return;
            }
        }
    }

    /**
     * 根据用户名删除客户端
     *
     * @param pUserName
     */
    public void removeClient(String pUserName) {
        if (mClients.containsKey(pUserName)) {
            mClients.remove(pUserName);
            onRemoveClient(pUserName);
        }
    }

    /**
     * 发送信息
     *
     * @param pUserName
     * @param pMessage
     *
     * @throws APPErrorException
     */
    public void sendMessage(String pUserName, TextMessage pMessage) throws APPErrorException {
        if (mClients.containsKey(pUserName)) {
            try {
                mClients.get(pUserName).sendMessage(pMessage);
            } catch (IOException e) {
                throw new APPErrorException("发送消息失败；原因：" + e.getMessage(), e);
            }
        }
    }

    /**
     * 发送信息
     *
     * @param pUserName
     * @param pMessage
     *
     * @throws APPErrorException
     */
    public void sendMessage(String pUserName, String pMessage) throws APPErrorException {
        if (mClients.containsKey(pUserName)) {
            try {
                mClients.get(pUserName).sendMessage(new TextMessage(pMessage));
            } catch (IOException e) {
                throw new APPErrorException("发送消息失败；原因：" + e.getMessage(), e);
            }
        }
    }
    /**
     * 添加用户事件
     * @param pChangeClientEvent
     */
    protected void onChangeClient(ChangeClientEvent pChangeClientEvent) {
        if (null != mClientFactoryListener) {
            mClientFactoryListener.changeClient(this, pChangeClientEvent);
        }
    }
    /**
     * 添加用户事件
     * @param pUserName
     * @param pClient
     */
    protected void onAddClient(String pUserName, WebSocketSession pClient) {
        if (null != mClientFactoryListener) {
            mClientFactoryListener.addClient(this, new AddClientEvent(pUserName, pClient));
        }
    }

    /**
     * 删除用户事件
     * @param pUserName
     */
    protected void onRemoveClient(String pUserName) {
        if (null != mClientFactoryListener) {
            mClientFactoryListener.removeClient(this, new RemoveClientEvent(pUserName));
        }
    }
    @Override
    public void close()  {
        mClientFactoryListener =null;
        for(WebSocketSession session :mClients.values())
        {
            try {
                session.close();
            } catch (IOException e) {

            }
        }
        mClients.clear();
    }
}
