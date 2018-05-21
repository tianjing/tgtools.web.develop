package tgtools.web.develop.service;

import org.springframework.transaction.annotation.Transactional;
import tgtools.exceptions.APPErrorException;
import tgtools.web.develop.message.GridMessage;
import tgtools.web.develop.model.CommonModel;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 16:01
 */
public interface CommonService<T extends BaseMapper> {
    /**
     * 返回基于 BaseModel 的 具体 实体类
     * @return
     */
    CommonModel createModel();

    /**
     * 获取分所有数据
     *
     * @return
     */
    List listAll();

    /**
     * 根据条件查询数据
     *
     * @return
     */
    List list(Object pParam);

    /**
     * 获取分页的表格数据
     *
     * @param pPageIndex 页码 从1开始
     * @param pPageSize  页大小
     *
     * @return
     */
    GridMessage listPage(int pPageIndex, int pPageSize);

    /**
     * 获取一条数据
     * @param pData
     * @return
     */
    Object get(Object pData);

    /**
     * 添加一条空记录
     *
     * @return
     */
    String save(Object pData);

    /**
     * 添加一条空记录
     *
     * @return
     */
    String addEmpty();

    /**
     * 保存所有数据
     *
     * @param pDatas
     *
     * @throws APPErrorException
     */
    @Transactional(rollbackFor = Exception.class)
    void updateAll(List pDatas) throws APPErrorException;

    /**
     * 保存所有数据
     *
     * @param pData
     *
     * @throws APPErrorException
     */
    void update(Object pData) throws APPErrorException;

    /**
     * 删除所有数据
     *
     * @param pDatas
     *
     * @throws APPErrorException
     */
    @Transactional(rollbackFor = Exception.class)
    void removeAll(List pDatas) throws APPErrorException;

    /**
     * 删除一条数据
     *
     * @param pData
     *
     * @throws APPErrorException
     */
    void remove(Object pData) throws APPErrorException;
}
