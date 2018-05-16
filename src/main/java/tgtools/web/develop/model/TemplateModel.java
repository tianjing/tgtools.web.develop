package tgtools.web.develop.model;

/**
 * 含有 id 和 rev 的表实体类
 * @author 田径
 * @Title
 * @Description
 * @date 8:45
 */
public interface TemplateModel  extends CommonModel{

    Long getRev();

    void setRev(Long pRev);


}
