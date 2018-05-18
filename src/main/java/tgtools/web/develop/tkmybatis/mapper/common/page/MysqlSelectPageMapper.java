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
public interface MysqlSelectPageMapper<T> {
    @SelectProvider(type=MySqlSelectPageProvider.class,method = "dynamicSQL")
    List<T> selectPage(@Param("record") T record, @Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize);
}
