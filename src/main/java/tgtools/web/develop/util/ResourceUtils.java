package tgtools.web.develop.util;

import java.io.InputStream;
import java.net.URL;

public class ResourceUtils {


    public static InputStream getResourceAsStream(Class pClass, String name) {
        InputStream resourceStream = null;
        ClassLoader classLoader = getCustomClassLoader(pClass);
        if (classLoader != null) {
            resourceStream = classLoader.getResourceAsStream(name);
        }

        if (resourceStream == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
            resourceStream = classLoader.getResourceAsStream(name);
            if (resourceStream == null) {
                classLoader = ResourceUtils.class.getClassLoader();
                resourceStream = classLoader.getResourceAsStream(name);
            }
        }

        return resourceStream;
    }

    public static URL getResource(Class pClass, String name) {
        URL url = null;
        ClassLoader classLoader = getCustomClassLoader(pClass);
        if (classLoader != null) {
            url = classLoader.getResource(name);
        }

        if (url == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
            url = classLoader.getResource(name);
            if (url == null) {
                classLoader = ResourceUtils.class.getClassLoader();
                url = classLoader.getResource(name);
            }
        }

        return url;
    }


    private static ClassLoader getCustomClassLoader(Class pClass) {
        if (pClass != null) {
            ClassLoader classLoader = pClass.getClassLoader();
            if (classLoader != null) {
                return classLoader;
            }
        }

        return null;
    }

}
