package tgtools.web.develop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tgtools.util.GUID;
import tgtools.util.StringUtil;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 一个含有ID_ 表实体父类
 * @author 田径
 * @Title
 * @Description
 * @date 18:47
 */
public class BaseCommonModel extends AbstractModel implements CommonModel {
    @Id
    @Column(name="ID_")
    private String Id;

    @Override
    public String getId() {
        return Id;
    }
    @Override
    public void setId(String pId) {
        Id = pId;
    }


    /**
     * 初始化新建信息
     */
    @Override
    public void initNew()
    {
        if(StringUtil.isNullOrEmpty(getId())) {
            setId(GUID.newGUID());
        }
    }
}
