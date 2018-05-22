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
import tgtools.util.LogHelper;
import tgtools.util.StringUtil;
import tgtools.web.platform.PlatformDispatcherServletFactory;

import javax.annotation.PostConstruct;
import java.io.Closeable;


/**
 * 实现springbean加载子类后，将url 加载到指定的servletName中
 * @author 田径
 * @Title
 * @Description
 * @date 10:14
 */
public abstract class AbstractWebSocketHandler implements WebSocketHandler, Closeable {
    public abstract String getServletName();

    public abstract String getUrl();


    @PostConstruct
    public void init() {
        try {
            /**
             * 增加消息监听 在 Servlet加载完后 触发监听 MyMessageListen
             */
            tgtools.message.MessageFactory.registerListening(new MyMessageListen(getServletName(),getUrl()));
        } catch (APPErrorException e) {
            LogHelper.error("","AbstractWebSocketHandler 初始化失败；原因："+e.toString(),"AbstractWebSocketHandler.init",e);
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

    /**
     * 通过tgtools message 在 Servlet加载完后附加websocket
     */
    public class MyMessageListen implements IMessageListening {
        protected String mServletName;
        protected String mUrl;

        public MyMessageListen(String pServletName, String pUrl) {
            mServletName = pServletName;
            mUrl = pUrl;
        }

        @Override
        public String getName() {
            return "WebSocketListen";
        }

        @Override
        public void onMessage(Message message) {
            if (StringUtil.isNullOrEmpty(mServletName) || StringUtil.isNullOrEmpty(mUrl)) {
                return;
            }
            try {
                if ("addDispatcherServlet".equals(message.getEvent())) {
                    System.out.println("Message Content :" + message.getContent());
                    JSONObject json = new JSONObject(message.getContent());
                    if (mServletName.equals(json.getString("ServletName"))) {
                        PlatformDispatcherServletFactory.getDispatcher(mServletName).addWebsocket(mUrl, new WebSocketHttpRequestHandler(AbstractWebSocketHandler.this));
                        tgtools.message.MessageFactory.unRegisterListening(getName());
                    }
                }
            } catch (APPErrorException e) {
                tgtools.util.LogHelper.error("", "region mywebsocket Error", getName(), e);
            }

        }
    }
}
