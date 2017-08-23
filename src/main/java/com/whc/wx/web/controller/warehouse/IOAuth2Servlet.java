package com.whc.wx.web.controller.warehouse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import com.github.pagehelper.util.StringUtil;
import com.wanhuchina.common.util.weixin.cgi.WeixinUtil;
import com.wanhuchina.common.util.zk.ZkPropertyUtil;

/**
 * Created by WangShengZhan
 * Email：WangShengZhan@wanhuchina.com
 * Date：2017/7/20
 * Time：11:33
 */
abstract public class IOAuth2Servlet extends HttpServlet {
    protected String openId;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String code = request.getParameter("code");
        String url = "";
        if (code !=null && !"authdeny".equals(code)) {
            openId = WeixinUtil.getOpenIdByOAuth2Code(ZkPropertyUtil.get("corpId"),ZkPropertyUtil.get("secret"), code);
            url = getRedirectView(request, response, openId);
        } else {
            out.print("授权获取失败，至于为什么，自己找原因。。。");
        }
        if (StringUtil.isNotEmpty(url)) {
            url = "";
        }
        response.sendRedirect(url);
        return;
    }

    abstract protected String getRedirectView(HttpServletRequest request, HttpServletResponse response, String openId);

}
