package tgtools.web.develop.service;


import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tgtools.exceptions.APPErrorException;
import tgtools.util.GUID;
import tgtools.util.StringUtil;
import tgtools.web.develop.message.GridMessage;
import tgtools.web.develop.model.CommonModel;
import tgtools.web.develop.model.TemplateModel;
import tgtools.web.develop.tkmybatis.mapper.common.page.BaseSelectPageMapper;
import tgtools.web.develop.tkmybatis.mapper.common.page.MysqlSelectPageMapper;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.Mapper;
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
public abstract class AbstractCommonService<T extends BaseMapper> implements CommonService<T>
 {

    @Autowired
    protected T mDao;

     /**
      * 返回基于 BaseModel 的 具体 实体类
      * @return
      */
     @Override
    public abstract CommonModel createModel();

     /**
     * 获取分所有数据
     *
     * @return
     */
    @Override
    public List listAll() {
        return mDao.selectAll();
    }
    /**
     * 根据条件查询数据
     *
     * @return
     */
    @Override
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
    @Override
    public GridMessage listPage(int pPageIndex, int pPageSize) {
        GridMessage data = new GridMessage();
        data.setData(invokePage(pPageIndex, pPageSize));
        data.setTotal(mDao.selectCount(null));
        return data;
    }

    /**
     * 获取一条数据
     * @param pData
     * @return
     */
    @Override
    public Object get(Object pData) {
        return mDao.selectOne(pData);
    }

    /**
     * 获取分页数据
     * @param pPageIndex
     * @param pPageSize
     * @return
     */
    protected Collection invokePage(int pPageIndex, int pPageSize) {
        if(mDao instanceof BaseSelectPageMapper)
        {
            return ((BaseSelectPageMapper)mDao).selectPage(createModel(),pPageIndex,pPageSize);
        }
        else if( mDao instanceof MysqlSelectPageMapper)
        {
            return ((MysqlSelectPageMapper)mDao).selectPage(createModel(),pPageIndex,pPageSize);
        }
        else if(mDao instanceof  Mapper)
        {
            Mapper mapper= (Mapper)mDao;
            return mapper.selectByRowBounds(createModel(),new RowBounds(pPageIndex-1,pPageSize));
        }
        return new ArrayList();
    }

    /**
     * 添加一条空记录
     *
     * @return
     */
    @Override
    public String save(Object pData) {
        String id =GUID.newGUID();
        if(pData instanceof TemplateModel)
        {
            TemplateModel entity =(TemplateModel)pData;
            entity.setId(id);
            entity.setRev(System.currentTimeMillis());
        }
        else if(pData instanceof CommonModel)
        {
            CommonModel entity =(CommonModel)pData;
            entity.setId(id);
        }
        mDao.insert(pData);
        return id;
    }
    /**
     * 添加一条空记录
     *
     * @return
     */
    @Override
    public String addEmpty() {
        String id = GUID.newGUID();
        CommonModel entity =createModel();
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
    @Override
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
    @Override
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
        if(pData instanceof TemplateModel){
            TemplateModel entity = (TemplateModel) pData;
            if (StringUtil.isNullOrEmpty(entity.getId())) {
                throw new APPErrorException("无效的主键");
            }
            if (null == entity.getRev()) {
                throw new APPErrorException("无效的REV");
            }
        }else if(pData instanceof CommonModel)
        {
            CommonModel entity = (CommonModel) pData;
            if (StringUtil.isNullOrEmpty(entity.getId())) {
                throw new APPErrorException("无效的主键");
            }
        }

    }

    /**
     * 删除所有数据
     *
     * @param pDatas
     *
     * @throws APPErrorException
     */
    @Override
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
   @Override
    public void remove(Object pData) throws APPErrorException {
        mDao.deleteByPrimaryKey(pData);
    }

    protected void validModel(CommonModel pModel) throws APPErrorException {
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
