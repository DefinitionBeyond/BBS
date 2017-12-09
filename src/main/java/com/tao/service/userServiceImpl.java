package com.tao.service;

import com.tao.dao.userDaoImpl;
import com.tao.po.User1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

@Service // 表示是 服务层
@Transactional //有事物处理
public class userServiceImpl {
    private Map<String, String> types = new HashMap<String, String>();

    public userServiceImpl() {
        //允许上传的文件类型
        types.put("image/jpeg", ".jpg");
        types.put("image/gif", ".gif");
        types.put("image/x-ms-bmp", ".bmp");
        types.put("image/png", ".png");
    }

    @Autowired
    private userDaoImpl dao;

    public User1 login(User1 user) {
        return dao.login(user.getUsername(), user.getPassword());
    }

    public User1 save(User1 user) {
        return dao.save(user);
    }

    public User1 findone(int id) {
        return dao.findOne(id);
    }

    /**
     * 对用户注册时的头像文件做解析，并且存入图片服务器，在数据库中做记录保留
     *
     * @param request
     * @param commonsMultipartResolver
     * @return
     */
    public User1 upLoadPic(HttpServletRequest request, CommonsMultipartResolver commonsMultipartResolver) {
        commonsMultipartResolver.setDefaultEncoding("utf-8");//设置字符编码
        commonsMultipartResolver.setResolveLazily(true);//是否懒加载
        commonsMultipartResolver.setMaxInMemorySize(4 * 1024 * 1024);//设置缓存
        commonsMultipartResolver.setMaxUploadSizePerFile(1024 * 1024);//设置每个文件的大小
        commonsMultipartResolver.setMaxUploadSize(2 * 1024 * 1024);//总上传文件的大小

        User1 user = null;
        if (commonsMultipartResolver.isMultipart(request)) {

            //转换req，把键值对的req转换成流的req
            MultipartHttpServletRequest Mrequest = commonsMultipartResolver.resolveMultipart(request);
            MultipartFile file = Mrequest.getFile("file0");
            String type = file.getContentType();//你将上传的文件格式
            if (types.containsKey(type)) {//符合
                //生成全局唯一码，做文件名字，防止多线程冲突
                File targetFile = new File("upload" + File.separator + request.getSession().getId() + types.get(type));

                user = new User1();

                String reusername = Mrequest.getParameter("reusername");
                String repassword = Mrequest.getParameter("repassword");

                user.setUsername(reusername);
                user.setPassword(repassword);
                user.setPicPath(targetFile.getPath());

                //拷贝上传的头像文件到缓冲区

                try {
                    file.transferTo(targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //储存头像文件到数据库
                FileInputStream fis = null;

                try {

                    fis = new FileInputStream(targetFile);

                    byte[] buffer = new byte[fis.available()];//开一个文件大小的空间

                    fis.read(buffer);

                    user.setPic(buffer);

                    user.setPagenum(5);

                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return user;
    }


    public int updatePageNumById(User1 user) {
        return dao.updatePageNumById(user);
    }
}