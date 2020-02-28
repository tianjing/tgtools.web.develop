package tgtools.web.develop.tkmybatis.mapper.common.page;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 9:39
 */
public interface BaseSelectPageMapper<T> {
    @SelectProvider(type=BaseSelectPageProvider.class,method = "dynamicSQL")
    List<T> selectPage(@Param("record") T record, @Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);

    @SelectProvider(type= BaseSelectPageProvider.class,method = "dynamicSQL")
    List<T> selectPageLimit(@Param("record") T record, @Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);

    @SelectProvider(type= BaseSelectPageProvider.class,method = "dynamicSQL")
    List<T> selectPageByFilterAndOrder(@Param("record") T record, @Param("filter") String pFilter ,@Param("order") String pOrder, @Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);

    @SelectProvider(type= BaseSelectPageProvider.class,method = "dynamicSQL")
    List<T> selectPageLimitByFilterAndOrder(@Param("record") T record, @Param("filter") String pFilter ,@Param("order") String pOrder, @Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);


}
