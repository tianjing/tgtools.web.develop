package tgtools.web.develop.mybatis.interceptor;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import tgtools.util.StringUtil;
import tgtools.web.core.Constants;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * mybatis RowBounds 是通过获取所有数据后再过滤
 * 本拦截器就是根据 RowBounds 动态 调整sql为分页sql 优化数据获取
 * @author 田径
 * @Title
 * @Description
 * @date 8:29
 */
@Intercepts(@Signature(method = "query", type = Executor.class, args = { MappedStatement.class, Object.class , RowBounds.class , ResultHandler.class}) )
public class RowBoundsInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取代理目标对象
        if(invocation.getArgs()[2] instanceof RowBounds ){
            RowBounds row =(RowBounds)invocation.getArgs()[2];
            if(row.getOffset()>-1&&row.getLimit()>0&&row.getLimit()<Integer.MAX_VALUE)
            {
                return query((Executor)invocation.getTarget(),
                        (MappedStatement)invocation.getArgs()[0],
                        invocation.getArgs()[1],(RowBounds)invocation.getArgs()[2]);
            }
        }
        return invocation.proceed();
    }
    private Object query(Executor executor, MappedStatement ms , Object parameterObject, RowBounds rowBounds) throws Exception {
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        //生成新的分页sql
        String sql =StringUtil.replace(Constants.SQLs.Page_GetPageData_SQL,"${sql}",boundSql.getSql());
        sql=StringUtil.replace(sql,"{currParge}",String.valueOf(rowBounds.getOffset()+1));
        sql=StringUtil.replace(sql,"{pargeSize}",String.valueOf(rowBounds.getLimit()));

        //将分页sql替换到 boundSql的sql，BoundSql无法直接new 因为有些成员无法获取 如 additionalParameters
        Field field= tgtools.util.ReflectionUtil.findField(BoundSql.class,"sql");
        field.setAccessible(true);
        field.set(boundSql,sql);

        CacheKey key = executor.createCacheKey(ms, parameterObject, rowBounds, boundSql);
        return executor.query(ms, parameterObject, rowBounds, null, key, boundSql);
    }
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
