package com.willculture.thirdlogin.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.wutongwei.httprequest.HttpRequest;
import com.wutongwei.httprequest.HttpRequest.CharType;

import net.sf.json.JSONObject;

/**
 * qq登陆
 * 
 * @author paopao
 *
 */
public class QQLoginService extends LoginService {

  public QQLoginService(String clientid, String secret, String redirect_uri) {
    super(clientid, secret, redirect_uri);

  }


  @Override
  public String login$url() {
    StringBuffer sb = new StringBuffer();
    sb.append("https://graph.qq.com/oauth2.0/authorize");
    sb.append("?");
    sb.append("which=login&");
    sb.append("display=pc&");
    sb.append("state=test&");
    sb.append("response_type=code&");
    sb.append("scope=get_user_info&");
    sb.append("client_id=" + this.clientid + "&");
    sb.append("redirect_uri=" + this.redirect_uri);
    return sb.toString();

  }

  @Override
  public JSONObject login$token(String code) {
    StringBuffer sb = new StringBuffer();
    sb.append("https://graph.qq.com/oauth2.0/token?");
    sb.append("grant_type=authorization_code&");
    sb.append("client_id=" + this.clientid + "&");
    sb.append("client_secret=" + this.secret + "&");
    sb.append("code=" + code + "&");
    sb.append("redirect_uri=" + this.redirect_uri);

    String bd = HttpRequest.get(sb.toString()).body(CharType.UTF8);

    JSONObject obj = new JSONObject();
    if (bd.contains("error")) {
      bd = bd.substring(bd.indexOf("(") + 1, bd.lastIndexOf(")") - 1);
      JSONObject subobj = JSONObject.fromObject(bd.trim());
      obj.put("state", "fail");
      obj.put("code", subobj.get("error"));
      obj.put("msg", subobj.get("error_description"));
    } else {
      obj.put("state", "ok");
      Map<String, Object> data = new HashMap<String, Object>();
      String[] params = bd.split("&");
      for (String param : params) {
        String[] item = param.split("=");
        String key = item[0];
        if (key.contains("access_token")) {
          key = "token";
          data.put(key, item[1]);
        }
        if (key.contains("expires_in")) {
          key = "expires";
          data.put(key, item[1]);
        }

      }
      data.put("remind", new Date().getTime());
      // 获取openid
      data.put("openid", getOpenid(data.get("token").toString()));



      obj.put("data", data);


    }

    return obj;
  }

  /**
   * qq登陆获取用户信息，首先需要获取openid
   * 
   * @param token
   * @return
   */
  private String getOpenid(String token) {
    String bd = HttpRequest.get("https://graph.qq.com/oauth2.0/me?access_token=" + token)
        .body(CharType.UTF8);
    bd = bd.substring(bd.indexOf("(") + 1, bd.lastIndexOf(")") - 1);
    JSONObject subobj = JSONObject.fromObject(bd.trim());
    return subobj.getString("openid");
  }

  @Override
  public JSONObject login$user(String token, String openid) {
    String bd = HttpRequest.get("https://graph.qq.com/user/get_user_info?access_token=" + token
        + "&openid=" + openid + "&oauth_consumer_key=" + this.clientid).body();
    return JSONObject.fromObject(bd);
  }



}
