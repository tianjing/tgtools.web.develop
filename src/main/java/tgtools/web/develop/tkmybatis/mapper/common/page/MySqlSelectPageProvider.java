package tgtools.web.develop.tkmybatis.mapper.common.page;

import tgtools.util.StringUtil;
import tgtools.web.sqls.BaseViewSqls;
import tgtools.web.sqls.SqlsFactory;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 9:42
 */
public class MySqlSelectPageProvider extends BaseSelectPageProvider {
    private static String mMysql;

    public MySqlSelectPageProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    @Override
    public String getPageSql() {
        if (StringUtil.isNullOrEmpty(mMysql)) {
            BaseViewSqls sqls = SqlsFactory.getSQLs("mysql", new BaseViewSqls());
            mMysql = sqls.Page_GetPageData_SQL;
        }
        return mMysql;
    }

}
