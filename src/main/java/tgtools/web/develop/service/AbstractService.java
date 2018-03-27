package tgtools.web.develop.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tgtools.exceptions.APPErrorException;
import tgtools.util.GUID;
import tgtools.util.StringUtil;
import tgtools.web.develop.model.BaseModel;
import tgtools.web.entity.GridData;
import tk.mybatis.mapper.common.BaseMapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 通用 业务层包含了常用的dao方法
 * @author 田径
 * @Title
 * @Description
 * @date 19:14
 */
public abstract class AbstractService<T extends BaseMapper> {

    @Autowired
    protected T mDao;

    public abstract BaseModel createModel();

    /**
     * 获取分所有数据
     *
     * @return
     */
    public List listAll() {
        return mDao.selectAll();
    }
    /**
     * 根据条件查询数据
     *
     * @return
     */
    public List list(Object pParam) {
        return mDao.select(pParam);
    }
    /**
     * 获取分页的表格数据
     *
     * @param pPageIndex 页码 从1开始
     * @param pPageSize  页大小
     *
     * @return
     */
    public GridData listPage(int pPageIndex, int pPageSize) {
        GridData data = new GridData();
        data.setData(invokePage(pPageIndex, pPageSize));
        data.setTotalRows(mDao.selectCount(null));
        return data;
    }

    /**
     * 获取一条数据
     * @param pData
     * @return
     */
    public Object get(Object pData) {
        return mDao.selectOne(pData);
    }
    protected Collection invokePage(int pPageIndex, int pPageSize) {
        try {
            Method method = mDao.getClass().getDeclaredMethod("page", Integer.class, Integer.class);
            if (null != method) {
                Object obj = method.invoke(mDao, pPageIndex, pPageSize);
                if (null != obj) {
                    return (Collection) obj;
                }
            }
        } catch (Exception ex) {

        }
        return new ArrayList(0);
    }

    /**
     * 添加一条空记录
     *
     * @return
     */
    public String save(Object pData) {
        String id =GUID.newGUID();
        if(pData instanceof BaseModel)
        {
            BaseModel entity =(BaseModel)pData;
            entity.setId(id);
            entity.setRev(System.currentTimeMillis());
        }
        mDao.insert(pData);
        return id;
    }
    /**
     * 添加一条空记录
     *
     * @return
     */
    public String addEmpty() {
        createModel();
        String id = GUID.newGUID();
        BaseModel entity =createModel();
        entity.initNew();
        mDao.insert(entity);
        return id;
    }

    /**
     * 保存所有数据
     *
     * @param pDatas
     *
     * @throws APPErrorException
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateAll(List pDatas) throws APPErrorException {
        for (int i = 0; i < pDatas.size(); i++) {
            update(pDatas.get(i));
        }
    }

    /**
     * 保存所有数据
     *
     * @param pData
     *
     * @throws APPErrorException
     */
    public void update(Object pData) throws APPErrorException {
        valid(pData);
        mDao.updateByPrimaryKey(pData);
    }

    /**
     * 验证数据
     *
     * @param pData
     *
     * @throws APPErrorException
     */
    protected void valid(Object pData) throws APPErrorException {
        BaseModel entity = (BaseModel) pData;
        if (StringUtil.isNullOrEmpty(entity.getId())) {
            throw new APPErrorException("无效的主键");
        }
        if (null == entity.getRev()) {
            throw new APPErrorException("无效的REV");
        }
    }

    /**
     * 删除所有数据
     *
     * @param pDatas
     *
     * @throws APPErrorException
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeAll(List pDatas) throws APPErrorException {
        for (int i = 0; i < pDatas.size(); i++) {
            remove(pDatas.get(i));
        }
    }

    /**
     * 删除一条数据
     *
     * @param pData
     *
     * @throws APPErrorException
     */
    public void remove(Object pData) throws APPErrorException {
        mDao.deleteByPrimaryKey(pData);
    }

    protected void validModel(BaseModel pModel) throws APPErrorException {
        if(null==pModel)
        {
            throw new APPErrorException("Model不能为空");
        }
        if(StringUtil.isNullOrEmpty(pModel.getId()))
        {
            throw new APPErrorException("Id 不能为空");
        }
    }
}
