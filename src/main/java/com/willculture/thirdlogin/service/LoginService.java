package com.willculture.thirdlogin.service;

import net.sf.json.JSONObject;

public abstract class LoginService {

  protected String clientid;
  protected String secret;
  protected String redirect_uri;

  public LoginService(String clientid, String secret, String redirect_uri) {
    this.clientid = clientid;
    this.secret = secret;
    this.redirect_uri = redirect_uri;
  }

  /**
   * 登陆地址
   * 
   * @return
   */
  public abstract String login$url();

  /**
   * 获取token
   * 
   * @param code
   * @return
   */
  public abstract JSONObject login$token(String code);

  /**
   * 获取用户基本信息
   * 
   * @param token
   * @param openid
   * @return
   */
  public abstract JSONObject login$user(String token, String openid);

}
