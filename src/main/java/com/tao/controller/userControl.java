package com.tao.controller;


import com.tao.po.User1;
import com.tao.service.userServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "userControl", urlPatterns = {"/user"})
public class userControl extends HttpServlet {

    @Autowired
    private userServiceImpl service;

    @Override
    public void init() throws ServletException {
//        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CommonsMultipartResolver commonsMultipartResolver = new
                CommonsMultipartResolver(req.getSession().getServletContext());

        //如果接到的是流式数据，就代表是注册的时候图片数据传过来
        if (commonsMultipartResolver.isMultipart(req)) {//是注册吗，，是就往数据库添加数据
            reg(req, commonsMultipartResolver, resp);
        } else {//数据不是流式数据，检查url判断操作

            String action = req.getParameter("action");
            switch (action) {
                case "login":
                    login(req, resp);
                    break;
//            case "reg":
//                reg(req,resp);
//                break;
                case "logout":
                    logout(req, resp);
                    break;
                case "pic":
                    pic(req, resp);
                    break;
            }
        }
    }

    private void pic(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
//        System.out.println(req.getParameter("id").getClass().getName());
        User1 user = service.findone(Integer.parseInt(id));


        try {
            byte[] buffer = user.getPic();
            ServletOutputStream out = resp.getOutputStream();
            out.write(buffer);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reg(HttpServletRequest req, CommonsMultipartResolver commonsMultipartResolver, HttpServletResponse resp) {
        User1 user = service.upLoadPic(req, commonsMultipartResolver);
        user = service.save(user);
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


    private void logout(HttpServletRequest req, HttpServletResponse resp) {
        RequestDispatcher requestDispatcher = null;
        req.getSession().removeAttribute("user");//删除用户的session
        requestDispatcher = req.getRequestDispatcher("index");//request 协作重定向页面
        try {
            requestDispatcher.forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void login(HttpServletRequest req, HttpServletResponse resp) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        User1 user = new User1(username, password);

        String flag = (user = service.login(user)) != null ? "true" : "false";//是否成功登录标志

        //登录成功
        if (flag.equals("true")) {
            //设置session
            req.getSession().setAttribute("user", user);

            String sun = req.getParameter("sun");
            if (sun != null) {
                Cookie cookie = new Cookie("paopaku", user.getUsername());
                cookie.setMaxAge(3600 * 24 * 7); //这是cookie有效时间为7天
                resp.addCookie(cookie);
                Cookie cookie1 = new Cookie("papaokp", user.getPassword());
                cookie1.setMaxAge(3600 * 24 * 7);
                resp.addCookie(cookie1);
            }
        }

        resp.setContentType("text/html"); //设置响应内容类型
        resp.setContentType("utf-8"); //设置响应内容编码
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.println(flag);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }

    }
}
