package com.tao.controller;

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
            case "queryall":
                PageBean pb = queryAll(req, resp);
                map1.put("pb", pb);
                break;

        }

        map1.put("user", req.getSession().getAttribute("user"));//put user 的session信息
        freemarkUtil.forward(map.get("success").toString(), map1, resp);//将session在页面中传递，保证用户的权限
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
