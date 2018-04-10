package tgtools.web.develop.command;

import tgtools.exceptions.APPErrorException;
import tgtools.interfaces.IDispose;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 命令处理器
 * @author 田径
 * @Title
 * @Description
 * @date 16:56
 */
public class CommandFactory implements Closeable {
    protected HashMap<String, Command> mCommands = new HashMap<String, Command>();
    protected String mType;
    public CommandFactory(String pType) {
        mType = pType;
    }

    /**
     * 添加一个命令
     * @param pCommand
     */
    public void addCommand(Command pCommand) {
        if (!mCommands.containsKey(pCommand.getName())) {
            mCommands.put(pCommand.getName(), pCommand);
        }
    }

    /**
     * 处理命令
     * @param params
     * @return
     * @throws APPErrorException
     */
    public Object process(Object... params) throws APPErrorException {
        String command = (String) params[0];
        Object[] param = Arrays.copyOfRange(params, 1, params.length);
        Command comm = mCommands.get(command);
        if (null == comm) {
            throw new APPErrorException(mType + " 无法识别的命令；" + command);
        }
        return comm.excute(param);

    }

    /**
     * 初始化 在bean中查找符合 类型 和  Type的命令对象
     */
    public void init() {
        try {
            String[] names = tgtools.web.platform.Platform.getBeanFactory().getBeanDefinitionNames();
            for (String name : names) {
                if (name.endsWith("CommandImpl") && tgtools.web.platform.Platform.getBean(name) instanceof Command && mType.equals(((Command) tgtools.web.platform.Platform.getBean(name)).getType())) {
                    addCommand((Command)tgtools.web.platform.Platform.getBean(name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close()  {
        mCommands.clear();
    }
}
