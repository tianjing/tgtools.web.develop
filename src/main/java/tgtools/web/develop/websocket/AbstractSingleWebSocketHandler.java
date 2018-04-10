package tgtools.web.develop.websocket;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import tgtools.exceptions.APPErrorException;
import tgtools.json.JSONObject;
import tgtools.web.develop.command.CommandFactory;
import tgtools.web.develop.message.NotifyMessage;
import tgtools.web.develop.message.ValidMessage;
import tgtools.web.develop.service.UserService;
import tgtools.web.develop.websocket.listener.ClientFactoryListener;
import tgtools.web.entity.ResposeData;

import java.io.IOException;

/**
 * 通用的WebSocket+command的组合
 * 注意：当前handler是单例的 也就是说 一个url 一个 handler ，不会一次请求 new一个handler
 * @author 田径
 * @Title
 * @Description
 * @date 17:17
 */
public abstract class AbstractSingleWebSocketHandler extends AbstractWebSocketHandler {
    public AbstractSingleWebSocketHandler()
    {
        mClientFactory= new ClientFactory();
        mWebsocketCommand = new CommandFactory(getCommandType());
        mWebsocketCommand.init();
    }

    protected abstract String getCommandType();
    protected CommandFactory mWebsocketCommand;
    protected ClientFactory mClientFactory;


    protected abstract SecurityManager getSecurityManager();

    protected abstract <T extends UserService>T getUserService();

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
    }

    /**
     * websocket 消息 处理
     * @param webSocketSession
     * @param webSocketMessage
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        try {
            JSONObject json = new JSONObject(((TextMessage) webSocketMessage).getPayload());
            ValidMessage rm = (ValidMessage) tgtools.util.JsonParseHelper.parseToObject(json, ValidMessage.class);
            validLogin(webSocketSession, rm);
            mWebsocketCommand.process(rm.getOperation(), rm.getData());
        } catch (Exception e) {
            try {
                ResposeData data = new ResposeData();
                data.setSuccess(false);
                data.setError(e.getMessage());
                webSocketSession.sendMessage(new TextMessage(tgtools.util.JsonParseHelper.parseToJsonObject(data).toString()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
    public void sendMessage(String pLoginName, String pMessage) throws APPErrorException {
        mClientFactory.sendMessage(pLoginName, pMessage);
    }

    public void sendNotifyMessage(String pLoginName, NotifyMessage pMessage) throws APPErrorException {
        sendMessage(pLoginName, pMessage.toString());
    }
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        mClientFactory.removeClient(webSocketSession);
    }

    protected void validLogin(WebSocketSession pWebSocketSession, ValidMessage pValidMessage) throws APPErrorException {
        //模拟登陆
        SimpleSession session = new SimpleSession();
        session.setHost(pWebSocketSession.getRemoteAddress().getHostName());
        WebDelegatingSubject subject = new WebDelegatingSubject(null, true, "", session, null, null, getSecurityManager());
        ThreadContext.bind(subject);
        getUserService().tokenLogin(pWebSocketSession.getRemoteAddress().getHostName(), pValidMessage.getUser(), pValidMessage.getToken());
        //登录成功后 添加客户端
        mClientFactory.addClient(pValidMessage.getUser(), pWebSocketSession);
    }
    @Override
    public void close() {
      if(null!=mWebsocketCommand)
      {
         mWebsocketCommand.close();
      }
        if(null!=mClientFactory)
        {
            mClientFactory.close();
        }
    }
}
