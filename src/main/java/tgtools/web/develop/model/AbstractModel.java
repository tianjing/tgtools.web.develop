package tgtools.web.develop.model;

import tgtools.util.GUID;
import tgtools.util.StringUtil;
import tgtools.web.develop.util.ModelHelper;
import tgtools.web.util.PageSqlUtil;

import javax.persistence.Column;
import javax.persistence.Id;

/**
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
    public String treeSql()
    {
        String tablename= ModelHelper.getTableName(this.getClass());
        String sql="select * from ${tablename} ";

        return sql;
    }



}
