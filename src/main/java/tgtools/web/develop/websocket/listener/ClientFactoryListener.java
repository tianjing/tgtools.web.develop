package tgtools.web.develop.websocket.listener;

import tgtools.web.develop.websocket.listener.event.AddClientEvent;
import tgtools.web.develop.websocket.listener.event.ChangeClientEvent;
import tgtools.web.develop.websocket.listener.event.RemoveClientEvent;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 14:03
 */
public interface ClientFactoryListener {

    /**
     * 增加客户端事件
     * @param pSender
     * @param pEvnet
     */
    void addClient(Object pSender, AddClientEvent pEvnet);

    /**
     * 更换客户端事件
     * 一般重复连接指 当前用户没有断开，但又来一个新的连接，默认关闭日志连接，添加新连接。
     * @param pSender
     * @param pEvnet
     */
    void changeClient(Object pSender, ChangeClientEvent pEvnet);

    /**
     * 一个客户连接关闭后的操作
     * @param pSender
     * @param pEvnet
     */
    void removeClient(Object pSender, RemoveClientEvent pEvnet);

}
