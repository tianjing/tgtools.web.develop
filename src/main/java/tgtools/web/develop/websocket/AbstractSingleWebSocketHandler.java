package tgtools.web.develop.websocket;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import tgtools.exceptions.APPErrorException;
import tgtools.json.JSONObject;
import tgtools.web.develop.command.CommandFactory;
import tgtools.web.develop.message.ValidMessage;
import tgtools.web.develop.service.UserService;
import tgtools.web.entity.ResposeData;

/**
 * 通用的WebSocket+command的组合
 *
 * @author 田径
 * @Title
 * @Description
 * @date 17:17
 */
public abstract class AbstractSingleWebSocketHandler<T extends UserService> extends AbstractWebSocketHandler {
    public AbstractSingleWebSocketHandler()
    {
        mClientFactory= new ClientFactory();
        mWebsocketCommand = new CommandFactory(getCommandType());
        mWebsocketCommand.init();
    }

    protected abstract String getCommandType();
    protected CommandFactory mWebsocketCommand;
    protected ClientFactory mClientFactory;

    @Autowired
    protected SecurityManager mSecurityManager;
    @Autowired
    protected T mUserService;

    @Override
    public String getRest() {
        return "rest1";
    }

    @Override
    public String getUrl() {
        return "/wcpt";
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
    }

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

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        mClientFactory.removeClient(webSocketSession);
    }

    protected void validLogin(WebSocketSession pWebSocketSession, ValidMessage pValidMessage) throws APPErrorException {
        mClientFactory.addClient(pValidMessage.getUser(), pWebSocketSession);

        SimpleSession session = new SimpleSession();
        session.setHost(pWebSocketSession.getRemoteAddress().getHostName());
        WebDelegatingSubject subject = new WebDelegatingSubject(null, true, "", session, null, null, mSecurityManager);
        ThreadContext.bind(subject);


        mUserService.tokenLogin(pWebSocketSession.getRemoteAddress().getHostName(), pValidMessage.getUser(), pValidMessage.getToken());
        SecurityUtils.getSubject().getPrincipal();
    }
}
