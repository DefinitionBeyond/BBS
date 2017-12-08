package com.tao.controller;

import com.tao.util.freemarkUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页控制器
 * <p>
 * 设置首页访问
 */
@WebServlet(name = "IndexControl", urlPatterns = {"/index"}
        , initParams = {@WebInitParam(name = "success", value = "/show.ftl")})
public class IndexControl extends HttpServlet {
    private Map<String, String> map = new HashMap<String, String>();

    @Override
    public void init(ServletConfig config) throws ServletException {
//        super.init();
        map.put("success", config.getInitParameter("success"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map map1 = new HashMap();
        map1.put("user", req.getSession().getAttribute("user"));
        freemarkUtil.forward(map.get("success").toString(), map1, resp);
    }
}
