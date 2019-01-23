package tgtools.web.develop.util;

import tgtools.util.LogHelper;
import tgtools.util.StringUtil;

import javax.persistence.Table;
import java.lang.reflect.Method;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 15:28
 */
public class ModelHelper {

    /**
     * 通过注解获取表名称
     *
     * @param pClazz
     * @return
     */
    public static String getTableName(Class<?> pClazz) {
        Table table = pClazz.getAnnotation(Table.class);
        if (null != table) {
            return table.name();
        } else if (!Object.class.equals(pClazz.getSuperclass())) {
            return getTableName(pClazz.getSuperclass());
        }
        return StringUtil.EMPTY_STRING;
    }

    /**
     * 实体类 复制
     * @param pSource 源实体类
     * @param pTtarget 目标实体类
     * @param <T>
     */
    public static <T> void copy(T pSource, T pTtarget) {
        Method[] vMethods = pSource.getClass().getMethods();
        for (int i = 0; i < vMethods.length; i++) {
            if (vMethods[i].getDeclaringClass().equals(Object.class)) {
                continue;
            }
            if (vMethods[i].getName().startsWith("get")) {
                String vMethodName = "set" + vMethods[i].getName().substring(3);
                try {
                    Method vMethod = pTtarget.getClass().getMethod(vMethodName, vMethods[i].getReturnType());
                    if (null == vMethod) {
                        continue;
                    }
                    Object vValue = vMethods[i].invoke(pSource);
                    if (null != vValue) {
                        vMethod.invoke(pTtarget, vValue);
                    }
                } catch (Exception e) {
                    LogHelper.error("", "复制对象错误", "ModelHelper.copy", e);
                }
            }
        }
    }
}
