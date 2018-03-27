package tgtools.web.develop.command;

import tgtools.exceptions.APPErrorException;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 16:56
 */
public interface Command {
    /**
     * 命令类型（分组）
     * @return
     */
    String getType();

    /**
     * 命令名称
     * @return
     */
    String getName();

    /**
     * 执行命令入口
     * @param params
     * @return
     * @throws APPErrorException
     */
    Object excute(Object... params) throws APPErrorException;
}
