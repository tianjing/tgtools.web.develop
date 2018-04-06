package tgtools.web.develop.mybatis.interceptor;

import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;
import org.springframework.util.ReflectionUtils;
import tgtools.data.DataTable;
import tgtools.util.LogHelper;
import tgtools.util.StringUtil;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 16:01
 */
@Intercepts(@Signature(method = "handleResultSets", type = ResultSetHandler.class, args = {Statement.class}) )
public class DataTableInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取代理目标对象
        Object target = invocation.getTarget();
        if (target instanceof DefaultResultSetHandler) {
            DefaultResultSetHandler resultSetHandler = (DefaultResultSetHandler) target;
            // 利用反射获取返回值对象
            if (isDataTableResultType(resultSetHandler)) {
                Object[] args = invocation.getArgs();
                // 获取到当前的Statement
                Statement stmt =  (Statement) args[0];
                // 通过Statement获得当前结果集
                ResultSet resultSet = stmt.getResultSet();
                if(resultSet != null) {
                    //Mybatis 结果集都是List型，不论Dao的返回类型是不是List
                    List<DataTable> res=new ArrayList<DataTable>(1);
                    res.add(new DataTable(resultSet,getSql(resultSetHandler),false));
                    return res;
                }
            }
        }
        return invocation.proceed();
    }
    protected boolean isDataTableResultType(DefaultResultSetHandler resultSetHandler)
    {
        MappedStatement mappedStatement= reflect(resultSetHandler);
        List<ResultMap> res=mappedStatement.getResultMaps();
        if(res.size()==1&&res.get(0).getType().equals(DataTable.class))
        {
            return true;
        }
        return false;
    }
    protected String getSql(DefaultResultSetHandler resultSetHandler)
    {
        Field field = ReflectionUtils.findField(DefaultResultSetHandler.class, "rowBounds");
        field.setAccessible(true);
        Object value = null;
        try {
            value = field.get(resultSetHandler);
            if(value instanceof BoundSql)
            {
                BoundSql bsql= (BoundSql)value;
                return bsql.getSql();
            }
        } catch (Exception e) {
            LogHelper.error("","默认返回结果集反射参数对象异常,原因："+e.getMessage(),"DataTableInterceptor.getSql",e);
        }
        return StringUtil.EMPTY_STRING;
    }
    protected MappedStatement reflect(DefaultResultSetHandler resultSetHandler){
        Field field = ReflectionUtils.findField(DefaultResultSetHandler.class, "mappedStatement");
        field.setAccessible(true);
        Object value = null;
        try {
            value = field.get(resultSetHandler);
            if(value instanceof MappedStatement)
            {
               return (MappedStatement)value;
            }
        } catch (Exception e) {
            LogHelper.error("","默认返回结果集反射参数对象异常，原因："+ e.getMessage(),"DataTableInterceptor.reflect",e);
        }
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
