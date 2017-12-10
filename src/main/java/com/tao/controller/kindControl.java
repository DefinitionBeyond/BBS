package com.tao.controller;

import com.tao.service.articleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author liutao
 * @date 2017/12/10  13:16
 */


/**
 * 文本编辑器应用
 * 使用当中上传图片功能时：
 * 需要修改 static/editor/plugins/image/image.js 的17行左右，路径与此servlet一致
 * 还需要修改 static/editor/plugins/multiimage.js 的201行左右 ，同样的改法
 */
@WebServlet(name = "kindControl", urlPatterns = {"/kindupload"})
public class kindControl extends HttpServlet {
    @Autowired
    private articleServiceImpl service;

    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String s = service.uploadPic(req);

        PrintWriter out = resp.getWriter();

        out.print(s);
        out.flush();
        out.close();
    }
}
