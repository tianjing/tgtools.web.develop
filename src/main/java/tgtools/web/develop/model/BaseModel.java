package tgtools.web.develop.model;

import tgtools.util.GUID;
import tgtools.util.StringUtil;
import tgtools.web.develop.util.ModelHelper;
import tgtools.web.util.PageSqlUtil;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 含有 ID_ 和 REV_ 的表实体类
 * @author 田径
 * @Title
 * @Description
 * @date 18:47
 */
public class BaseModel extends AbstractModel implements TemplateModel {
    @Id
    @Column(name="ID_")
    private String mId;
    @Column(name="REV_")
    private Long mRev;

    @Override
    public String getId() {
        return mId;
    }
    @Override
    public void setId(String pId) {
        mId = pId;
    }
    @Override
    public Long getRev() {
        return mRev;
    }
    @Override
    public void setRev(Long pRev) {
        mRev = pRev;
    }
    /**
     * 通用分页sql
     * @param pPageIndex
     * @param pPageSize
     * @return
     */
    @Override
    public String pageSql(int pPageIndex, int pPageSize)
    {
        String tablename= ModelHelper.getTableName(this.getClass());
        String sql="select * from ${tablename} order by rev_";
        sql= StringUtil.replace(sql,"${tablename}",tablename);
        sql = PageSqlUtil.getPageDataSQL(sql,String.valueOf(pPageIndex),String.valueOf(pPageSize));
        return sql;
    }

    /**
     * 初始化新建信息
     */
    @Override
    public void initNew()
    {
        if(StringUtil.isNullOrEmpty(getId())) {
            setId(GUID.newGUID());
        }
        if(null==getRev()||getRev()<1) {
            setRev(System.currentTimeMillis());
        }
    }

    /**
     * 获取所有数据 根据rev排序
     * @return
     */
    @Override
    public String buildAllDataSql()
    {
        String sql="select * from ${tablename} ${order}";
        String tablename= ModelHelper.getTableName(this.getClass());
        sql= StringUtil.replace(sql,"${tablename}",tablename);
        sql= StringUtil.replace(sql,"${order}",StringUtil.isNullOrEmpty(buildDefaultOrders())?"":" order by "+buildDefaultOrders());
        return sql;
    }

    /**
     * 默认排序
     * @return
     */
    @Override
    public String buildDefaultOrders()
    {
        return "REV_ DESC";
    }
}
