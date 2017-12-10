package com.tao.controller;

import com.alibaba.fastjson.JSON;
import com.tao.po.Article;
import com.tao.po.PageBean;
import com.tao.po.User1;
import com.tao.service.articleServiceImpl;
import com.tao.util.freemarkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 帖子控制器
 * 控制帖子的增删改查
 */
@WebServlet(name = "articleControl", urlPatterns = {"/article"},
        initParams = {@WebInitParam(name = "success", value = "/show.ftl")})
public class articleControl extends HttpServlet {

    @Autowired
    private articleServiceImpl service;

    private Map<String, String> map = new HashMap();

    @Override
    public void init(ServletConfig config) throws ServletException {
        map.put("success", config.getInitParameter("success"));
//        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        Map map1 = new HashMap();
        switch (action) {
            case "queryall": //查询所有已经发布的帖子
                PageBean pb = queryAll(req, resp);
                map1.put("pb", pb);
                break;
            case "delz": //删除主贴
                delTZ(req, resp);
                break;
            case "addz": // 发布主贴
                add(req, resp);
                break;
            case "queryid": //查看主贴下的从贴
                queryReply(req, resp);
                break;
            case "delc":
                delCT(req, resp);//删除从贴
                break;
            case "reply"://回帖
                add(req, resp);
                break;
        }

        map1.put("user", req.getSession().getAttribute("user"));//put user 的session信息
        freemarkUtil.forward(map.get("success").toString(), map1, resp);//将session在页面中传递，保证用户的权限
    }

    private void delCT(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("rid");

        service.deleteCT(Integer.parseInt(id));

        queryReply(req, resp);
    }

    private void queryReply(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        Map<String, Object> map = service.queryById(Integer.parseInt(id));

        String s = JSON.toJSONString(map, true); //把查询结果转成json格式
        System.out.print(s);
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        try {
            PrintWriter out = resp.getWriter();
            out.print(s); //传入前端页面
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) {
        String title = req.getParameter("title");

        String content = req.getParameter("content");
        String rootid = req.getParameter("rootid");

        String userid = req.getParameter("userid");

        Article article = new Article();

        article.setRootid(Integer.parseInt(rootid));

        article.setContent(content);

        article.setTitle(title);
        User1 user = new User1();
        user.setUserid(Integer.parseInt(userid));
        article.setUser(user);
        article.setDatatime(new Date(System.currentTimeMillis()));
        if (service.save(article) != null) {
            if (rootid.equals("0")) {
                RequestDispatcher dispatcher = null;
                try {
                    dispatcher = req.getRequestDispatcher("index");
                    dispatcher.forward(req, resp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                this.queryReply(req, resp);
            }
        }
    }

    private void delTZ(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");

        service.deleteZT(Integer.parseInt(id));

        RequestDispatcher dispatcher = null;
        dispatcher = req.getRequestDispatcher("index");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PageBean queryAll(HttpServletRequest req, HttpServletResponse resp) {
        String currentPage = req.getParameter("page");

        User1 user = (User1) req.getSession().getAttribute("user");
        int pageNum = 5;
        if (user != null) {
            pageNum = user.getPagenum();
        }
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(Integer.parseInt(currentPage) - 1, pageNum, sort); //spring-data-jpa提供的分页查询与排序
        Page<Article> pa = service.findAll(pageable, 0);//rid查主贴，默认是0

        PageBean pb = new PageBean();
        pb.setRowsPerPage(pageNum);
        pb.setCurPage(Integer.parseInt(currentPage));
        pb.setMaxRowCount(pa.getTotalElements());
        pb.setData(pa.getContent());
        pb.setMaxPage(pa.getTotalPages());

        return pb;
    }
}
