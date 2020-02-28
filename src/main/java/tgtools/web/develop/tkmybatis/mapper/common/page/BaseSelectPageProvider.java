package tgtools.web.develop.tkmybatis.mapper.common.page;

import org.apache.ibatis.mapping.MappedStatement;
import tgtools.util.StringUtil;
import tgtools.web.core.Constants;
import tk.mybatis.mapper.annotation.Version;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 9:42
 */
public class BaseSelectPageProvider extends MapperTemplate {
    public BaseSelectPageProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * where主键条件
     *
     * @param entityClass
     * @return
     */
    public static String whereAllIfColumns(Class<?> entityClass, String entityName, boolean useVersion, boolean pUseFilter, boolean pUseOrder) {
        StringBuilder sql = new StringBuilder();
        sql.append("<where>");
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            if (!useVersion || !column.getEntityField().isAnnotationPresent(Version.class)) {
                sql.append(SqlHelper.getIfNotNull(entityName, column, " AND " + column.getColumnEqualsHolder(entityName), false));
            }
        }
        if (useVersion) {
            sql.append(SqlHelper.whereVersion(entityClass));
        }

        if (pUseFilter) {
            sql.append("<if test='filter != null'> ${filter}</if>");
        }

        if (pUseOrder) {
            sql.append("<if test='order != null'> ${order}</if>");
        }

        sql.append("</where>");
        return sql.toString();
    }

    /**
     * limit 分页 sql
     *
     * @return
     */
    public String getPageLimitSql() {
        return Constants.SQLs.Page_GetPageData_Limit_SQL;
    }

    /**
     * 分页 sql
     *
     * @return
     */
    public String getPageSql() {
        return Constants.SQLs.Page_GetPageData_SQL;
    }


    /**
     * 分页
     *
     * @param pMappedStatement
     * @return
     */
    public String selectPage(MappedStatement pMappedStatement) {
        return buildSelectPage(pMappedStatement, false, false, false);
    }

    /**
     * 使用 limit 分页
     *
     * @param pMappedStatement
     * @return
     */
    public String selectPageLimit(MappedStatement pMappedStatement) {
        return buildSelectPage(pMappedStatement, true, false, false);
    }

    /**
     * 使用过滤 和 排序
     *
     * @param pMappedStatement
     * @return
     */
    public String selectPageByFilterAndOrder(MappedStatement pMappedStatement) {
        return buildSelectPage(pMappedStatement, false, true, true);
    }

    /**
     * 使用 limit 过滤 和 排序
     *
     * @param pMappedStatement
     * @return
     */
    public String selectPageLimitByFilterAndOrder(MappedStatement pMappedStatement) {
        return buildSelectPage(pMappedStatement, true, true, true);
    }

    protected String buildSelectPage(MappedStatement pMappedStatement, boolean pUseLimit, boolean pUseFilter, boolean pUseOrder) {
        final Class<?> entityClass = getEntityClass(pMappedStatement);
        //将返回值修改为实体类型
        setResultType(pMappedStatement, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(whereAllIfColumns(entityClass, "record", false, pUseFilter, pUseOrder));
        String pageSql = StringUtil.replace(pUseLimit ? getPageLimitSql() : getPageSql(), "{currParge}", pUseLimit ? "((#{pageIndex}-1)*#{pageSize})" : "#{pageIndex}");
        pageSql = StringUtil.replace(pageSql, "{pargeSize}", "#{pageSize}");
        pageSql = StringUtil.replace(pageSql, ">", "&gt;");
        pageSql = StringUtil.replace(pageSql, "<", "&lt;");
        return StringUtil.replace(pageSql, "${sql}", sql.toString());
    }

}
