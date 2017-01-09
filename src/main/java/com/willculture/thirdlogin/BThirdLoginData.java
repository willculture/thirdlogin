package com.willculture.thirdlogin;

/**
 * token数据绑定
 * @author paopao
 *
 */
public class BThirdLoginData {
  
  private long tldataid;
  
  private String token; //访问token
  
  private long remind; //记住时间
  
  private long expires; //过期时间

  private String openid; //第三方用户对应的唯一ID
}
