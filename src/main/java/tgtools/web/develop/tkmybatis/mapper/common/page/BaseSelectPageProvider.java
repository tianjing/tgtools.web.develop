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

    public String selectPage(MappedStatement ms)
    {
        final Class<?> entityClass = getEntityClass(ms);
        //将返回值修改为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(whereAllIfColumns(entityClass,"record",false));
        String pageSql=StringUtil.replace(getPageSql(),"{currParge}","#{pageIndex}");
        pageSql=StringUtil.replace(pageSql,"{pargeSize}","#{pageSize}");
        pageSql=StringUtil.replace(pageSql,">","&gt;");
        pageSql=StringUtil.replace(pageSql,"<","&lt;");
        return StringUtil.replace(pageSql,"${sql}",sql.toString());
    }
    public String getPageSql()
    {
        return Constants.SQLs.Page_GetPageData_SQL;
    }
    /**
     * where主键条件
     *
     * @param entityClass
     * @return
     */
    public static String whereAllIfColumns(Class<?> entityClass, String entityName, boolean useVersion) {
        StringBuilder sql = new StringBuilder();
        sql.append("<where>");
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            if (!useVersion || !column.getEntityField().isAnnotationPresent(Version.class)) {
                sql.append(SqlHelper.getIfNotNull(entityName,column, " AND " + column.getColumnEqualsHolder(entityName), false));
            }
        }
        if (useVersion) {
            sql.append(SqlHelper.whereVersion(entityClass));
        }
        sql.append("</where>");
        return sql.toString();
    }
}
