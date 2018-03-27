package tgtools.web.develop.gateway;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tgtools.exceptions.APPErrorException;
import tgtools.util.StringUtil;
import tgtools.web.develop.model.BaseModel;
import tgtools.web.develop.service.AbstractService;
import tgtools.web.entity.GridData;
import tgtools.web.entity.ResposeData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 一个通用的rest类 包含常用的方法
 * @author 田径
 * @Title
 * @Description
 * @date 8:53
 */
public class AbstractGateway<T extends AbstractService,E extends BaseModel> {

    @Autowired
    protected T mService;


    @ApiOperation("获取所有数据")
    @RequestMapping(value="/listall",method = {RequestMethod.POST})
    @ResponseBody
    public List listAll(HttpServletRequest request, HttpServletResponse response)
    {
        return mService.listAll();
    }

    @ApiOperation("获取分页")
    @ApiImplicitParams({@ApiImplicitParam(
            value = "页码从0开始",
            name = "pageIndex"
    ), @ApiImplicitParam(
            value = "页大小",
            name = "pageSize"
    )})
    @RequestMapping(value="/listpage",method = {RequestMethod.POST})
    @ResponseBody
    public GridData list(@RequestParam("pageIndex") int pPageIndex, @RequestParam("pageSize") int pPageSize)
    {
        return mService.listPage(pPageIndex+1,pPageSize);
    }

    @ApiOperation("根据ID获取数据")
    @RequestMapping(value="/get",method = {RequestMethod.GET})
    @ResponseBody
    public ResposeData get(@RequestParam("id")String id)
    {
        ResposeData data =new ResposeData();
        data.setSuccess(false);
        try {
            if(StringUtil.isNullOrEmpty(id))
            {
                throw new APPErrorException("请输入id");
            }
            BaseModel model =mService.createModel();
            model.setId(id);
            Object entity= mService.get(model);
            data.setSuccess(true);
            data.setData(entity);
        }
        catch (Exception e)
        {
            data.setSuccess(false);
            data.setError(e.getMessage());
        }
        return data;
    }
    @ApiOperation("添加一条空数据")
    @RequestMapping(value="/saveempty",method = {RequestMethod.POST})
    @ResponseBody
    public ResposeData save()
    {
        ResposeData data =new ResposeData();
        data.setSuccess(false);
        try {
            String id= mService.addEmpty();
            data.setSuccess(true);
            data.setData(id);
        }
        catch (Exception e)
        {
            data.setSuccess(false);
            data.setError(e.getMessage());
        }
        return data;
    }
    @ApiOperation("保存所有数据信息")
    @ApiImplicitParams({@ApiImplicitParam(
            value = "List<DataDictionary> 的 arrayjson",
            name = "datas"
    )})
    @RequestMapping(value="/updateall",method = {RequestMethod.PUT})
    @ResponseBody
    public ResposeData saveAll(@RequestBody List<E> datas)
    {
        ResposeData res =new ResposeData();
        res.setSuccess(false);
        try {
            mService.updateAll(datas);
            res.setSuccess(true);
            res.setData(true);
        }
        catch (Exception e)
        {
            res.setSuccess(false);
            res.setError(e.getMessage());
        }
        return res;
    }
    @RequestMapping(value="/update",method = {RequestMethod.PUT})
    @ResponseBody
    public ResposeData save(@RequestBody E  data)
    {
        ResposeData res =new ResposeData();
        res.setSuccess(false);
        try {
            mService.update(data);
            res.setSuccess(true);
            res.setData(true);
        }
        catch (Exception e)
        {
            res.setSuccess(false);
            res.setError(e.getMessage());
        }
        return res;
    }
    @ApiOperation("删除所有数据信息")
    @ApiImplicitParams({@ApiImplicitParam(
            value = "List<DataDictionary> 的 arrayjson",
            name = "datas"
    )})
    @RequestMapping(value="/removeall",method = {RequestMethod.DELETE})
    @ResponseBody
    public ResposeData removeAll(@RequestBody List<E> datas)
    {
        ResposeData data =new ResposeData();
        data.setSuccess(false);
        try {
            mService.removeAll(datas);
            data.setSuccess(true);
            data.setData(true);
        }
        catch (Exception e)
        {
            data.setSuccess(false);
            data.setError(e.getMessage());
        }
        return data;
    }

    @RequestMapping(value="/remove",method = {RequestMethod.DELETE})
    @ResponseBody
    public ResposeData removeAll(@RequestBody E data)
    {
        ResposeData res =new ResposeData();
        res.setSuccess(false);
        try {
            mService.remove(data);
            res.setSuccess(true);
            res.setData(true);
        }
        catch (Exception e)
        {
            res.setSuccess(false);
            res.setError(e.getMessage());
        }
        return res;
    }
}
