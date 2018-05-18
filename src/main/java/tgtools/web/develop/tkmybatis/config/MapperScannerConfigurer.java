package tgtools.web.develop.tkmybatis.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import tgtools.web.develop.tkmybatis.mapper.common.page.BaseSelectPageMapper;
import tgtools.web.develop.tkmybatis.mapper.common.page.MysqlSelectPageMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * tk mapper 扫描配置 默认自定义dao接口
 * @author 田径
 * @Title
 * @Description
 * @date 9:52
 */
public class MapperScannerConfigurer extends tk.mybatis.spring.mapper.MapperScannerConfigurer {
    private List<Class> mCumstemRegistryClass = new ArrayList<Class>();

    public MapperScannerConfigurer() {
        mCumstemRegistryClass.add(BaseSelectPageMapper.class);
        mCumstemRegistryClass.add(MysqlSelectPageMapper.class);

    }

    public void addRegistryClass(Class pClass) {
        mCumstemRegistryClass.add(pClass);
    }

    public void clearRegistryClass(Class pClass) {
        mCumstemRegistryClass.clear();
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        super.postProcessBeanDefinitionRegistry(registry);
        for (int i = 0; i < mCumstemRegistryClass.size(); i++) {
            getMapperHelper().registerMapper(mCumstemRegistryClass.get(i));
        }
    }
}