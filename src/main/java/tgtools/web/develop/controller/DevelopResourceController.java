package tgtools.web.develop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import tgtools.web.develop.util.ResourceUtils;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 12:33
 */

public class DevelopResourceController {
    protected MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();


    @RequestMapping(value = "/resource/**")
    public void get(HttpServletRequest pRequest, HttpServletResponse pResponse) {
        String url = pRequest.getRequestURI();
        String file = url.substring(url.indexOf("manage"));
        int end = file.indexOf("?");
        if (end >= 0) {
            file = file.substring(0, file.indexOf("?"));
        }
        try {
            pResponse.setContentType(mimetypesFileTypeMap.getContentType(file));
            copyAndClose(ResourceUtils.getResourceAsStream(DevelopResourceController.class,file), pResponse.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void copyAndClose(InputStream pInputStream, OutputStream pOutputStream) {
        if (null == pOutputStream) {
            closeStream(pInputStream);
            return;
        }

        if (null == pInputStream && null != pOutputStream) {
            closeStream(pOutputStream);
            return;
        }

        try {
            int len = 0;
            byte[] data = new byte[1024];
            while ((len = pInputStream.read(data)) > 0) {
                pOutputStream.write(data, 0, len);
            }

        } catch (Exception e) {

        } finally {
            closeStream(pInputStream);
            closeStream(pOutputStream);
        }
    }

    private void closeStream(Closeable pCloseable) {
        if (null != pCloseable) {
            try {
                pCloseable.close();
            } catch (IOException e) {
            }
        }
    }
}
