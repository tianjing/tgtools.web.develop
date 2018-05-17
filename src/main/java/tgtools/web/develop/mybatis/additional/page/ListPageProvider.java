package tgtools.web.develop.mybatis.additional.page;

import tgtools.util.DateUtil;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.io.Closeable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Set;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 9:21
 */
public class ListPageProvider{



    public String selectPage(Object pData, int pPageIndex, int pPageSize) {
        Class clazz=pData.getClass();
        EntityTable table=tk.mybatis.mapper.mapperhelper.EntityHelper.getEntityTable(clazz);
        Set<EntityColumn> columns= tk.mybatis.mapper.mapperhelper.EntityHelper.getColumns(clazz);
        StringBuilder sql=new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(clazz));
        sql.append(SqlHelper.fromTable(clazz, table.getName()));
        sql.append(" where 1=1 ");
        for(EntityColumn column: columns)
        {
            try {
                Field field = tgtools.util.ReflectionUtil.findField(clazz, column.getProperty());
                field.setAccessible(true);
                Object value=field.get(pData);
                if (null != value) {
                    String valuestr=getValue(value);
                    if(null==valuestr){continue;}
                    sql.append("and ");
                    sql.append(column.getColumn());
                    sql.append("='");
                    sql.append(valuestr);
                    sql.append("' ");
              }
            }catch (Exception e)
            {}
        }
        return tgtools.web.util.PageSqlUtil.getPageDataSQL(sql.toString(),String.valueOf(pPageIndex),String.valueOf(pPageSize));
    }

    /**
     *
     * @param pValue
     * @return
     */
    private static String getValue(Object pValue)
    {
        if(pValue instanceof Date)
        {
            return DateUtil.formatLongtime((Date)pValue);
        }
        else if(pValue instanceof String)
        {
            return (String)pValue;
        }
        else if((pValue instanceof byte[])||(pValue instanceof Closeable))
        {
            return null;
        }
        return pValue.toString();
    }
}
