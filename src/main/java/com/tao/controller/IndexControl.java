package com.tao.controller;

import com.tao.util.freemarkUtil;

import javax.servlet.RequestDispatcher;
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


/**
 * 进入首页的时候，先查询一下分页的第一页
 */
@WebServlet(name = "IndexControl", urlPatterns = {"/index"}
        , initParams = {@WebInitParam(name = "success", value = "article?action=queryall&page=1")})


//@WebServlet(name = "IndexControl", urlPatterns = {"/index"}
//        , initParams = {@WebInitParam(name = "success", value = "/show.ftl")})
/**
 * 注释时间1209
 *
 * 业务增加，条改件变
 * 首页要求有一部分帖子内容
 * 所以访问首页的时候应该先进行一次帖子查询
 */
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

        /**
         * 注释时间1209
         *
         * 业务增加，条改件变
         * 首页要求有一部分帖子内容
         * 所以访问首页的时候应该先进行一次帖子查询
         */
//        Map map1 = new HashMap();
//        map1.put("user", req.getSession().getAttribute("user"));//put user 的session信息
//        freemarkUtil.forward(map.get("success").toString(), map1, resp);//将session在页面中传递，保证用户的权限


        /**
         *  通过servlet协作转向到article页
         */
        RequestDispatcher dispatcher = null;
        dispatcher = req.getRequestDispatcher(map.get("success"));
        dispatcher.forward(req, resp);
    }
}
