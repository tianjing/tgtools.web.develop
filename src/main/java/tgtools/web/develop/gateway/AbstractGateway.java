package tgtools.web.develop.gateway;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tgtools.exceptions.APPErrorException;
import tgtools.util.StringUtil;
import tgtools.web.develop.message.GridMessage;
import tgtools.web.develop.message.ResponseMessage;
import tgtools.web.develop.model.BaseModel;
import tgtools.web.develop.service.AbstractService;

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
    public GridMessage list(@RequestParam("pageIndex") int pPageIndex, @RequestParam("pageSize") int pPageSize)
    {
        return mService.listPage(pPageIndex+1,pPageSize);
    }

    @ApiOperation("根据ID获取数据")
    @RequestMapping(value="/get",method = {RequestMethod.GET})
    @ResponseBody
    public ResponseMessage get(@RequestParam("id")String id)
    {
        ResponseMessage data =new ResponseMessage();
        data.setStatus(false);
        try {
            if(StringUtil.isNullOrEmpty(id))
            {
                throw new APPErrorException("请输入id");
            }
            BaseModel model =mService.createModel();
            model.setId(id);
            Object entity= mService.get(model);
            data.setStatus(true);
            data.setData(entity);
        }
        catch (Exception e)
        {
            data.setStatus(false);
            data.setData(e.getMessage());
        }
        return data;
    }
    @ApiOperation("添加一条空数据")
    @RequestMapping(value="/saveempty",method = {RequestMethod.POST})
    @ResponseBody
    public ResponseMessage save()
    {
        ResponseMessage data =new ResponseMessage();
        data.setStatus(false);
        try {
            String id= mService.addEmpty();
            data.setStatus(true);
            data.setData(id);
        }
        catch (Exception e)
        {
            data.setStatus(false);
            data.setData(e.getMessage());
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
    public ResponseMessage saveAll(@RequestBody List<E> datas)
    {
        ResponseMessage res =new ResponseMessage();
        res.setStatus(false);
        try {
            mService.updateAll(datas);
            res.setStatus(true);
            res.setData(true);
        }
        catch (Exception e)
        {
            res.setStatus(false);
            res.setData(e.getMessage());
        }
        return res;
    }
    @RequestMapping(value="/update",method = {RequestMethod.PUT})
    @ResponseBody
    public ResponseMessage save(@RequestBody E  data)
    {
        ResponseMessage res =new ResponseMessage();
        res.setStatus(false);
        try {
            mService.update(data);
            res.setStatus(true);
            res.setData(true);
        }
        catch (Exception e)
        {
            res.setStatus(false);
            res.setData(e.getMessage());
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
    public ResponseMessage removeAll(@RequestBody List<E> datas)
    {
        ResponseMessage data =new ResponseMessage();
        data.setStatus(false);
        try {
            mService.removeAll(datas);
            data.setStatus(true);
            data.setData(true);
        }
        catch (Exception e)
        {
            data.setStatus(false);
            data.setData(e.getMessage());
        }
        return data;
    }

    @RequestMapping(value="/remove",method = {RequestMethod.DELETE})
    @ResponseBody
    public ResponseMessage removeAll(@RequestBody E data)
    {
        ResponseMessage res =new ResponseMessage();
        res.setStatus(false);
        try {
            mService.remove(data);
            res.setStatus(true);
            res.setData(true);
        }
        catch (Exception e)
        {
            res.setStatus(false);
            res.setData(e.getMessage());
        }
        return res;
    }
}
