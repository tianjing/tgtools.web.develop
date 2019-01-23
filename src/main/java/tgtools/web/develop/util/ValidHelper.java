package tgtools.web.develop.util;

import tgtools.exceptions.APPErrorException;
import tgtools.util.DateUtil;
import tgtools.util.StringUtil;

import java.util.regex.Pattern;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 9:26
 */
public class ValidHelper {
    private static long minDate= DateUtil.getMinDate().getTime();

    /**
     * 验证对象不能为空
     * @param pContent 文本
     * @param pParamName 参数名称
     * @throws APPErrorException
     */
    public static void validObject(Object pContent, String pParamName) throws APPErrorException {
        if (null==pContent) {
            throw new APPErrorException(pParamName + " 不能为null");
        }
    }

    /**
     * 验证字符串 不能为空
     * @param pContent 文本
     * @param pParamName 参数名称
     * @throws APPErrorException
     */
    public static void validString(String pContent, String pParamName) throws APPErrorException {
        if (StringUtil.isNullOrEmpty(pContent)) {
            throw new APPErrorException(pParamName + " 不能为空");
        }
    }

    /**
     * 验证日期格式 不能为空和必须正值最小值
     * @param pContent 文本
     * @param pParamName 参数名称
     * @throws APPErrorException
     */
    public static void validDate(Long pContent, String pParamName) throws APPErrorException {
        if(null==pContent||pContent.longValue()<minDate)
        {
            throw new APPErrorException(pParamName + " 错误的时间值");
        }
    }

    /**
     * 验证大文本长度
     * @param pContent 文本
     * @param pLength 规则长度
     * @param pParamName 参数名称
     * @throws APPErrorException
     */
    public static void validBigTextLength(String pContent,int pLength,String pParamName) throws APPErrorException {
        if(!StringUtil.isNullOrEmpty(pContent)&&pContent.length()>pLength)
        {
            throw new APPErrorException((null==pParamName?StringUtil.EMPTY_STRING:pParamName)+"长度不能超过"+pLength+"，请调整；");
        }
    }
    /**
     * 验证大文本长度 小于 10000
     * @param pContent 文本
     * @throws APPErrorException
     */
    public static void validBigTextLength(String pContent) throws APPErrorException {
        if(!StringUtil.isNullOrEmpty(pContent)&&pContent.length()>10000)
        {
            throw new APPErrorException("内容长度超过10000，请调整；");
        }
    }
    private static Pattern htmlPattern=Pattern.compile("<(S*?)[^>]*>.*?|<.*? />|&lt;(.*)&gt;|&lt;.*? /&gt;");
    /**
     * 验证字符串是否含有 html 标签或 转义的内容；
     * @param pContent 文本
     */
    public static void validHtmlAndEscape(String pContent) throws APPErrorException {
        if(htmlPattern.matcher(pContent).find())
        {
            throw new APPErrorException("内容不能含有html标签");
        }
    }

}