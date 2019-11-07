package tgtools.web.develop.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tgtools.exceptions.APPErrorException;
import tgtools.util.LogHelper;
import tgtools.util.StringUtil;
import tgtools.web.develop.websocket.listener.ClientFactoryListener;
import tgtools.web.develop.websocket.listener.event.AddClientEvent;
import tgtools.web.develop.websocket.listener.event.ChangeClientEvent;
import tgtools.web.develop.websocket.listener.event.RemoveClientEvent;

import java.io.Closeable;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
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
     *
     * @param pLoginName 登录名
     *
     * @return
     */
    public boolean hasClient(String pLoginName) {
        return mClients.containsKey(pLoginName);
    }


    /**
     * 获取当前所有用户信息用户名称
     * @return
     */
    public Map<String, WebSocketSession> getClientMap() {
        return this.mClients;
    }


    /**
     * 获取当前所有用户信息用户名称
     * @return
     */
    public Enumeration<String> getNames() {
        return this.mClients.keys();
    }

    /**
     * 获取用户名称
     *
     * @param pClient
     *
     * @return
     */
    public String getName(WebSocketSession pClient) {
        for (Map.Entry<String, WebSocketSession> item : mClients.entrySet()) {
            if (item.getValue().equals(pClient) || item.getValue().getId().equals(pClient.getId())) {
                return item.getKey();
            }
        }
        return StringUtil.EMPTY_STRING;
    }

    /**
     * 根据名称获取 WebSocket 连接对象
     *
     * @param pLoginName
     *
     * @return
     */
    @Deprecated
    public WebSocketSession getCient(String pLoginName) {
        if (!mClients.contains(pLoginName)) {
            return null;
        }
        return mClients.get(pLoginName);
    }
    /**
     * 根据名称获取 WebSocket 连接对象
     *
     * @param pLoginName
     *
     * @return
     */
    public WebSocketSession getClient(String pLoginName) {
        if (!mClients.contains(pLoginName)) {
            return null;
        }
        return mClients.get(pLoginName);
    }
    /**
     * 是否存在用户连接
     *
     * @param pWebSocketSession 连接对象
     *
     * @return
     */
    public boolean hasClient(WebSocketSession pWebSocketSession) {
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
        ChangeClientEvent event = new ChangeClientEvent(pUserName, pClient, mClients.get(pUserName), false);
        onChangeClient(event);
        if (!event.getCancelChange()) {
            WebSocketSession client = mClients.get(pUserName);
            mClients.remove(pUserName);
            try {
                client.close();
            } catch (IOException e) {
                LogHelper.error("", "关闭连接出错；原因：" + e.toString(), "changeClient", e);
            }
            mClients.put(pUserName, pClient);
        }
    }


    /**
     * 根据value 删除客户端
     *
     * @param pClient
     */
    public void removeClient(WebSocketSession pClient) {
        removeClient(getName(pClient));
    }

    /**
     * 根据用户名删除客户端
     *
     * @param pUserName
     */
    public void removeClient(String pUserName) {
        if (mClients.containsKey(pUserName)) {
            WebSocketSession pClient = mClients.get(pUserName);
            String id = pClient.getId();
            String vAddress = pClient.getRemoteAddress().getAddress().getHostAddress();
            try {
                if (pClient.isOpen()) {
                    pClient.close();
                }
            } catch (Exception e) {
            }
            pClient = null;
            mClients.remove(pUserName);
            onRemoveClient(pUserName, id, vAddress);
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
        validOnline(pUserName);
        try {
            mClients.get(pUserName).sendMessage(pMessage);
        } catch (IOException e) {
            throw new APPErrorException("发送消息失败；原因：" + e.getMessage(), e);
        }
    }

    protected void validOnline(String pUserName) throws APPErrorException {
        if (!mClients.containsKey(pUserName)) {
            throw new APPErrorException("发送消息失败；原因：用户不存在；用户：" + pUserName);
        }
        if (!mClients.get(pUserName).isOpen()) {
            throw new APPErrorException("发送消息失败；原因：用户连接已关闭；用户：" + pUserName);
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
        validOnline(pUserName);
        try {
            mClients.get(pUserName).sendMessage(new TextMessage(pMessage));
        } catch (IOException e) {
            throw new APPErrorException("发送消息失败；原因：" + e.getMessage(), e);
        }
    }

    /**
     * 添加用户事件
     *
     * @param pChangeClientEvent
     */
    protected void onChangeClient(ChangeClientEvent pChangeClientEvent) {
        if (null != mClientFactoryListener) {
            mClientFactoryListener.changeClient(this, pChangeClientEvent);
        }
    }

    /**
     * 添加用户事件
     *
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
     *
     * @param pUserName
     */
    protected void onRemoveClient(String pUserName, String pId, String pAddress) {
        if (null != mClientFactoryListener) {
            mClientFactoryListener.removeClient(this, new RemoveClientEvent(pUserName, pId, pAddress));
        }
    }

    @Override
    public void close() {
        mClientFactoryListener = null;
        for (WebSocketSession session : mClients.values()) {
            try {
                session.close();
            } catch (IOException e) {

            }
        }
        mClients.clear();
    }
}
