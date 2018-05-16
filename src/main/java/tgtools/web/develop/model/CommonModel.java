package tgtools.web.develop.model;

/**
 * 通用表实体类
 * @author 田径
 * @Title
 * @Description
 * @date 8:45
 */
public interface CommonModel {
    String getId();
    void setId(String pId);

    /**
     * 通用分页sql
     * @param pPageIndex
     * @param pPageSize
     * @return
     */
    String pageSql(int pPageIndex, int pPageSize);

    /**
     * 所有数据sql
     * @return
     */
    String getAllDataSql();
    /**
     * 默认排序
     * @return
     */
    String getDefaultOrders();
    /**
     * 初始化新建信息
     */
    void initNew();

}
