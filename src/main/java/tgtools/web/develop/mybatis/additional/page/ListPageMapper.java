package tgtools.web.develop.mybatis.additional.page;

import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 9:20
 */
public interface ListPageMapper<T> {

    @SelectProvider(type = ListPageProvider.class, method = "selectPage")
    List<T> selectPage(T pData, int pPageIndex, int pPageSize);


}
