package tgtools.web.develop.service;

import tgtools.exceptions.APPErrorException;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 10:07
 */
public interface UserService {

    boolean validLoginUser(String pUsername, String pPassword) throws APPErrorException;

    String createToken(String pUsername, String pAddr) throws APPErrorException;

    void tokenLogin(String pAddr, String pUsername, String pToken) throws APPErrorException;


}
