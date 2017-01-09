package com.willculture.thirdlogin.service;

import java.util.HashMap;
import java.util.Map;

import com.wutongwei.httprequest.HttpRequest;

import net.sf.json.JSONObject;

/**
 * 新浪微博登陆
 * 
 * @author paopao
 *
 */
public class SinaLoginService extends LoginService {

  public SinaLoginService(String clientid, String secret, String redirect_uri) {
    super(clientid, secret, redirect_uri);
  }

  public String login$url() {
    StringBuffer sb = new StringBuffer();
    sb.append("https://api.weibo.com/oauth2/authorize?client_id=");
    sb.append(this.clientid);
    sb.append("&response_type=code");
    sb.append("&redirect_uri=");
    sb.append(this.redirect_uri);
    return sb.toString();
  }


  public JSONObject login$token(String code) {
    String url = "https://api.weibo.com/oauth2/access_token?client_id=" + this.clientid
        + "&client_secret=" + this.secret + "&grant_type=authorization_code" + "&redirect_uri="
        + this.redirect_uri + "&code=" + code;
    JSONObject robj = HttpRequest.post(url).create().json();
    JSONObject obj = new JSONObject();
    if (robj.has("error")) {
      obj.put("state", "fail");
      obj.put("code", robj.get("error_code"));
      obj.put("msg", robj.get("error_description"));
    } else {
      obj.put("state", "ok");
      Map<String, Object> data = new HashMap<String, Object>();
      data.put("expires", robj.get("expires_in"));
      data.put("remind", robj.getString("remind_in"));
      data.put("token", robj.get("access_token"));
      data.put("openid", robj.getString("uid"));
      obj.put("data", data);
    }
    return obj;
  }

  @Override
  public JSONObject login$user(String token, String openid) {
    JSONObject obj = HttpRequest
        .get("https://api.weibo.com/2/users/show.json?access_token=" + token + "&uid=" + openid)
        .json();
    return obj;
  }



}
