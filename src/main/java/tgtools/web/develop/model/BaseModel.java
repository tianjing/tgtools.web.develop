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
public class BaseModel {
    @Id
    @Column(name="ID_")
    private String mId;
    @Column(name="REV_")
    private Long mRev;

    public String getId() {
        return mId;
    }

    public void setId(String pId) {
        mId = pId;
    }

    public Long getRev() {
        return mRev;
    }

    public void setRev(Long pRev) {
        mRev = pRev;
    }
    /**
     * 通用分页sql
     * @param pPageIndex
     * @param pPageSize
     * @return
     */
    public String pageSql(int pPageIndex, int pPageSize)
    {
        String tablename= ModelHelper.getTableName(this.getClass());
        String sql="select * from ${tablename} order by rev_";
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
        String sql="select * from ${tablename} order by rev_";

        return sql;
    }

    /**
     * 初始化新建信息
     */
    public void initNew()
    {
        if(StringUtil.isNullOrEmpty(getId())) {
            setId(GUID.newGUID());
        }
        if(null==getRev()||getRev()<1) {
            setRev(System.currentTimeMillis());
        }
    }
}
