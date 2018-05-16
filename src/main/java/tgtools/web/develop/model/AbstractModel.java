package tgtools.web.develop.model;

import tgtools.util.StringUtil;
import tgtools.web.develop.util.ModelHelper;
import tgtools.web.util.PageSqlUtil;

/**
 * 模板方法类
 * @author 田径
 * @Title
 * @Description
 * @date 18:47
 */
public class AbstractModel {

    /**
     * 通用分页sql
     * @param pPageIndex
     * @param pPageSize
     * @return
     */
    public String pageSql(int pPageIndex, int pPageSize)
    {
        String tablename= ModelHelper.getTableName(this.getClass());
        String sql="select * from ${tablename} ";
        sql= StringUtil.replace(sql,"${tablename}",tablename);
        sql = PageSqlUtil.getPageDataSQL(sql,String.valueOf(pPageIndex),String.valueOf(pPageSize));
        return sql;
    }

    /**
     * 获取所有数据 根据rev排序
     * @return
     */
    public String getAllDataSql()
    {
        String sql="select * from ${tablename} ";
        String tablename= ModelHelper.getTableName(this.getClass());
        sql= StringUtil.replace(sql,"${tablename}",tablename);
        return sql;
    }

    /**
     * 默认排序
     * @return
     */
    public String getDefaultOrders()
    {
        return StringUtil.EMPTY_STRING;
    }
}
