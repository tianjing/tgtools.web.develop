package tgtools.web.develop.service;


import tgtools.web.develop.model.BaseModel;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * 通用 业务层包含了常用的dao方法
 * @author 田径
 * @Title
 * @Description
 * @date 19:14
 */
public abstract class AbstractService<T extends BaseMapper> extends AbstractCommonService<T> {


    /**
     * 返回基于 BaseModel 的 具体 实体类
     * @return
     */
    @Override
    public abstract BaseModel createModel();

}
