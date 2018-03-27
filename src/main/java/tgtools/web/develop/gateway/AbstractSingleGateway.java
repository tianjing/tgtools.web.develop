package tgtools.web.develop.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tgtools.json.JSONObject;
import tgtools.util.StringUtil;
import tgtools.web.develop.command.CommandFactory;
import tgtools.web.develop.message.ValidMessage;
import tgtools.web.develop.service.UserService;
import tgtools.web.entity.ResposeData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 一个通用的接口层，通过一个url+command 的方式进行交互
 *
 * @author 田径
 * @Title
 * @Description
 * @date 10:21
 */

public abstract class AbstractSingleGateway<T extends UserService> {

    protected CommandFactory restCommand;

    @Autowired
    protected T mUserService;

    public AbstractSingleGateway() {
        restCommand = new CommandFactory(getCommandType());
        restCommand.init();
    }

    protected abstract String getCommandType();

    /**
     * 获取 token
     *
     * @param request
     * @param response
     *
     * @return
     */
    @RequestMapping(value = "/gettoken", method = {RequestMethod.POST})
    @ResponseBody
    public ResposeData getToken(HttpServletRequest request, HttpServletResponse response) {
        String username = StringUtil.EMPTY_STRING;
        String password = StringUtil.EMPTY_STRING;
        ResposeData result = new ResposeData();
        try {
            JSONObject json = tgtools.web.util.RequestHelper.parseRequest(request);
            if (json.has("username")) {
                username = json.getString("username");
            }
            if (json.has("password")) {
                password = json.getString("password");
            }

            mUserService.validLoginUser(username, password);
            String token = mUserService.createToken(username, request.getRemoteAddr());
            result.setSuccess(true);
            result.setData(token);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setError(e.getMessage());
        }
        return result;
    }

    /**
     * 调用命令
     *
     * @param request
     * @param response
     *
     * @return
     */
    @RequestMapping(value = "/invoke", method = {RequestMethod.POST})
    @ResponseBody
    public ResposeData invoke(HttpServletRequest request, HttpServletResponse response) {
        ResposeData res = new ResposeData();
        try {
            JSONObject json = tgtools.web.util.RequestHelper.parseRequest(request);
            ValidMessage rm = (ValidMessage) tgtools.util.JsonParseHelper.parseToObject(json, ValidMessage.class);

            mUserService.tokenLogin(request.getRemoteAddr(), rm.getUser(), rm.getToken());

            Object obj = restCommand.process(rm.getOperation(), json.getJSONObject("data"));
            res.setSuccess(true);
            res.setData(obj);
        } catch (Exception e) {
            res.setSuccess(false);
            res.setError(e.getMessage());
        }
        try {
            System.out.println("client结果：" + tgtools.util.JsonParseHelper.parseToJsonObject(res).toString());
        } catch (Exception d) {
        }
        return res;
    }


}