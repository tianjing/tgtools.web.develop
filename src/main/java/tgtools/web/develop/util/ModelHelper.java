package tgtools.web.develop.util;

import tgtools.util.StringUtil;
import javax.persistence.Table;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 15:28
 */
public class ModelHelper {

    /**
     * 通过注解获取表名称
     * @param pClazz
     * @return
     */
    public static  String getTableName(Class<?> pClazz)
    {
        Table table= pClazz.getAnnotation(Table.class);
        if(null!=table)
        {
            return table.name();
        }
        else if(!Object.class.equals(pClazz.getSuperclass()))
        {
            return getTableName(pClazz.getSuperclass());
        }
        return StringUtil.EMPTY_STRING;
    }
}
