package tgtools.web.develop.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.net.URI;

/**
 * 文件映射过滤器规则
 * uri: file:/file#GBK
 * "file:/"表示文件类型 （必须）
 * "file"第二个 表示目录 （必须）
 * "#GBK"表示文件编码 （不必须）
 *
 * @author 田径
 * @date 2020-04-24 11:00
 * @desc
 **/
public class NettyFileResourcesWriteResponseFilter implements GlobalFilter, Ordered {
    /**
     * Order for write response filter.
     */
    public static final int FILE_RESOURCES_WRITE_RESPONSE_FILTER_ORDER = -1;
    private static final Log log = LogFactory.getLog(NettyFileResourcesWriteResponseFilter.class);

    public static File getFile(String pFilePath) {
        String osName = System.getProperty("os.name");
        log.info("os.name:" + osName);
        if (osName.startsWith("Mac OS")) {
            // 苹果
            return new File(pFilePath.substring(5));
        } else if (osName.startsWith("Windows")) {
            // windows
            return new File(pFilePath.substring(6));
        } else {
            // unix or linux
            return new File(pFilePath.substring(5));
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // NOTICE: nothing in "pre" filter stage as CLIENT_RESPONSE_CONN_ATTR is not added
        // until the NettyRoutingFilter is run
        return chain.filter(exchange).then(Mono.defer(() -> {
            URI vTargetUrl = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
            Route vRoute = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            if (null == vTargetUrl) {
                return Mono.empty();
            }

            String vFilePath = vTargetUrl.toString();
            if (!vFilePath.startsWith("file")) {
                return Mono.empty();
            }

            ServerHttpResponse response = exchange.getResponse();
            ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;

            File vFile = getFile(vFilePath);
            if (vFile.exists()) {
                zeroCopyResponse.getHeaders().setContentType(
                        getMimeType(vFile, vRoute));
                log.trace("NettyFileResourcesWriteResponseFilter start");
                return zeroCopyResponse.writeWith(vFile, 0, vFile.length());
            } else {
                log.warn("NettyFileResourcesWriteResponseFilter 文件不存在：" + vFile.toString());
                response.setStatusCode(HttpStatus.NOT_FOUND);
                return Mono.empty();
            }

        }));
    }

    private MediaType getMimeType(File vFile, Route pRoute) {
        String vContentType = new MimetypesFileTypeMap().getContentType(vFile);
        String vEncode = getEncode(pRoute);

        if (!StringUtils.isEmpty(StringUtils.trimAllWhitespace(vEncode))) {
            vContentType = vContentType + "; charset=" + vEncode;
        }
        return MediaType.parseMediaType(vContentType);
    }

    private String getEncode(Route pRoute) {
        String vEncode = "";
        String vUrl = pRoute.getUri().toString();
        int vIndex = vUrl.lastIndexOf("#");
        if (vIndex > 0 && (vIndex + 1) <= vUrl.length()) {
            vEncode = vUrl.substring(vIndex + 1);
        }
        return vEncode;
    }

    @Override
    public int getOrder() {
        return -1;
    }

}
