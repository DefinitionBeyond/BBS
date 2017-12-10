package com.tao.service;

import com.alibaba.fastjson.JSONObject;
import com.tao.dao.articleDao;
import com.tao.dao.articleDaoImpl;
import com.tao.po.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author liutao
 * @date 2017/12/9  15:45
 */

@Service
@Transactional
public class articleServiceImpl {
    private Map<String, String> types = new HashMap<String, String>();

    public articleServiceImpl() {
        types.put("image/jpeg", ".jpg");
        types.put("image/gif", ".gif");
        types.put("image/x-ms-bmp", ".bmp");
        types.put("image/png", ".png");
    }

    @Autowired
    private articleDao dao;
    @Autowired
    private articleDaoImpl dao1;

    public Page<Article> findAll(Pageable pageable, int num) {
        return dao.findAll(pageable, num);
    }

    public int deleteZT(Integer id) {
        return dao.deleteZT(id);
    }

    public Article save(Article article) {
        return dao.save(article);
    }

    public String uploadPic(HttpServletRequest req) {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(req.getSession().getServletContext());
        resolver.setDefaultEncoding("utf-8");
        resolver.setResolveLazily(true);
        resolver.setMaxInMemorySize(4 * 1024 * 1024);
        resolver.setMaxUploadSize(2 * 1024 * 1024);
        resolver.setMaxUploadSizePerFile(2 * 1024 * 1024);
        if (resolver.isMultipart(req)) {
            MultipartHttpServletRequest request = resolver.resolveMultipart(req);
            MultipartFile file = request.getFile("imgFile");
            String type = file.getContentType();

            if (types.containsKey(type)) {//如果是该系统支持的图片格式才给与解析
                String s3 = articleServiceImpl.class.getClassLoader().getResource("").toString();
                String dir = req.getParameter("dir");
                String id = UUID.randomUUID().toString();//生成一个全球唯一码来区别图片
                String newFileName = s3 + "static/editor/upload/" + dir + "/" + id + types.get(type);
                newFileName = newFileName.substring(6);
                File imageFile = new File(newFileName);
                try {
                    file.transferTo(imageFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String tpath = req.getRequestURL().toString();
                tpath = tpath.substring(0, tpath.lastIndexOf("/"));
                String path = tpath + "/editor/upload/" + dir + "/";//最终显示在编辑器中图片路径
                JSONObject obj = new JSONObject();
                obj.put("error", 0);//无错误
                obj.put("url", path + id + types.get(type));//使用json格式把上传文件信息传递到前端
                return obj.toJSONString();
            }
        }
        return "";
    }

    public Map<String, Object> queryById(int id) {

        return dao1.queryById(id);
    }

    public int deleteCT(int rid) {
        return dao.deleteCT(rid);
    }
}