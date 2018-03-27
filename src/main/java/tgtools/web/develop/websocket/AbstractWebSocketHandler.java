package tgtools.web.develop.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.support.WebSocketHttpRequestHandler;
import tgtools.exceptions.APPErrorException;
import tgtools.json.JSONObject;
import tgtools.message.IMessageListening;
import tgtools.message.Message;
import tgtools.util.StringUtil;
import tgtools.web.platform.PlatformDispatcherServletFactory;

import javax.annotation.PostConstruct;


/**
 * @author 田径
 * @Title
 * @Description
 * @date 10:14
 */
public abstract class AbstractWebSocketHandler implements WebSocketHandler {
    public abstract String getRest();

    public abstract String getUrl();


    @PostConstruct
    public void init() {
        try {
            tgtools.message.MessageFactory.registerListening(new MyMessageListen(getRest(),getUrl()));
        } catch (APPErrorException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        System.out.println("afterConnectionEstablished id:" + webSocketSession.getId());
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        System.out.println("handleMessage id:" + webSocketSession.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        System.out.println("handleTransportError id:" + webSocketSession.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        System.out.println("afterConnectionClosed id:" + webSocketSession.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private  class MyMessageListen implements IMessageListening {
        private String mRestName;
        private String mUrl;

        public MyMessageListen(String pRestName, String pUrl) {
            mRestName = pRestName;
            mUrl = pUrl;
        }

        @Override
        public String getName() {
            return "WebSocketListen";
        }

        @Override
        public void onMessage(Message message) {
            if (StringUtil.isNullOrEmpty(mRestName) || StringUtil.isNullOrEmpty(mUrl)) {
                return;
            }
            try {
                if ("addDispatcherServlet".equals(message.getEvent())) {
                    System.out.println("Message Content :" + message.getContent());
                    JSONObject json = new JSONObject(message.getContent());
                    if (mRestName.equals(json.getString("ServletName"))) {
                        PlatformDispatcherServletFactory.getDispatcher(mRestName).addWebsocket(mUrl, new WebSocketHttpRequestHandler(AbstractWebSocketHandler.this));
                        tgtools.message.MessageFactory.unRegisterListening(getName());
                    }
                }
            } catch (APPErrorException e) {
                tgtools.util.LogHelper.error("", "region mywebsocket Error", getName(), e);
            }

        }
    }
}
