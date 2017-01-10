package com.willculture.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.annotation.RequestScope;

import com.willculture.thirdlogin.service.QQLoginService;
import com.willculture.thirdlogin.service.SinaLoginService;

import net.sf.json.JSONObject;

@RequestScope
@Controller
public class LoginAction {

  SinaLoginService sservice = new SinaLoginService("1033877277", "c763008c514460655f74345448ec645e",
      "http://www.willculture.com/authorize/sina");

  QQLoginService qservice = new QQLoginService("100525886", "8def5aac6219d0e3e283e678e8e91b2a",
      "http://www.willculture.com/authorize/qq");

  /**
   * 登陆提交
   * 
   * @param type
   * @param request
   * @param response
   * @return
   */
  @RequestMapping("/login/{type}")
  public String login(@PathVariable String type, HttpServletRequest request,
      HttpServletResponse response) {
    String url = "";
    if (type.equals("sina")) {
      url = sservice.login$url();
    }

    if (type.equals("qq")) {
      url = qservice.login$url();
    }

    try {
      response.sendRedirect(url);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }


  /**
   * 登陆后验证
   * 获取用户得昵称， 头像，地址，生日
   * @param type
   * @param request
   * @return
   */
  @RequestMapping("/authorize/{type}")
  @ResponseBody
  public String login$authorize(@PathVariable String type, HttpServletRequest request) {
    JSONObject obj = new JSONObject();
    if (type.equals("sina")) {
      String code = request.getParameter("code");
      obj = sservice.login$token(code);
      if (obj.getString("state").equals("ok")) {
        JSONObject user = sservice.login$user(obj.getJSONObject("data").getString("token"),
            obj.getJSONObject("data").getString("openid"));
        System.out.println(user);
      }
    }

    if (type.equals("qq")) {
      String code = request.getParameter("code");
      obj = qservice.login$token(code);
      if (obj.getString("state").equals("ok")) {
        JSONObject user = qservice.login$user(obj.getJSONObject("data").getString("token"),
            obj.getJSONObject("data").getString("openid"));
        System.out.println(user);
      }
    }
    // 如果用户存在，直接登陆


    // 如果用户不存在，两种选择，第一种创建账号直接登陆
    // 第二种，绑定已有的账号

    return obj.toString();
  }


}
