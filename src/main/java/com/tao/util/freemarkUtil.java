package com.tao.util;

import freemarker.template.Configuration;
import freemarker.template.Template;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


public class freemarkUtil {
    private static Configuration configuration;

    /**
     * 实例一个模板对象
     * 已单例模式创建
     *
     * @return 模板对象
     */
    private static Configuration builtConfiguration() {
        if (configuration == null) {
            configuration = new Configuration(Configuration.VERSION_2_3_26);
            String path = freemarkUtil.class.getResource("/").getPath();//找到resouce目录
            File ftlPathDir = new File(path + File.separator + "templates");//File.separator 路径分割符
            try {
                configuration.setDefaultEncoding("utf-8");
                configuration.setDirectoryForTemplateLoading(ftlPathDir);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return configuration;
        }
        return configuration;
    }

    /**
     * 得到freemark模板
     *
     * @param ftlName
     * @return 模板页
     */
    public static Template getTemplate(String ftlName) {
        Template template = null;
        try {
            template = builtConfiguration().getTemplate(ftlName);
            return template;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return template;
    }

    /**
     * freemark模板页的转向
     *
     * @param ftlName 模板路径
     *                //     * @param map 传值map
     * @param resp    响应对象
     */
    public static void forward(String ftlName,
                               Map map,
                               HttpServletResponse resp) {//freemarker 转向
        Template temp = getTemplate(ftlName);
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            temp.process(map, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.flush();
    }
}

