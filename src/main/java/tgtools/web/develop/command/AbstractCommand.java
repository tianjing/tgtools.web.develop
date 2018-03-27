package tgtools.web.develop.command;

import org.springframework.beans.factory.annotation.Autowired;
import tgtools.exceptions.APPErrorException;

import java.lang.reflect.Method;

/**
 * Command to Service
 * 如果想把逻辑放在Service中使用的抽象类
 * @author 田径
 * @Title
 * @Description
 * @date 13:38
 */
public abstract class AbstractCommand<T> implements Command {

    @Autowired
    protected T mService;

    /**
     * 业务中对应的方法名
     * @return
     */
    protected abstract String getMethodName();

    /**
     * 业务实体类（BO）
     * @return
     */
    protected abstract Class<?> getModelClass();

    /**
     * 映射方法
     * @param pObject
     * @throws APPErrorException
     */
    protected void invoke(Object pObject) throws APPErrorException {
        try {
            Method method= mService.getClass().getDeclaredMethod(getMethodName(),getModelClass());
            method.invoke(mService,pObject);
        }catch (Exception ex)
        {
            throw new APPErrorException("执行方法错误；原因："+ex.getCause().getMessage(),ex);
        }
    }

    /**
     * 执行命令
     * @param params
     * @return
     * @throws APPErrorException
     */
    @Override
    public Object excute(Object... params) throws APPErrorException {
        Object obj = tgtools.util.JsonParseHelper.parseToObject(params[0].toString(),getModelClass(),false);
        invoke(obj);
        return true;
    }
}
