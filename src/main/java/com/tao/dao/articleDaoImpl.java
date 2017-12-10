package com.tao.dao;


import com.tao.po.Article;
import com.tao.po.User1;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liutao
 * @date 2017/12/10  18:17
 */

/**
 * springdata jpa
 * 不支持存储过程
 * 所以需要hibernate 的原生JPA实现
 */
@Repository //证明自己是原生JPA层
public class articleDaoImpl {

    @PersistenceContext
    private EntityManager entity;//表明实体上下文解析

    public Map<String, Object> queryById(int id) {
        Map<String, Object> map = new HashMap();
        StoredProcedureQuery procedureQuery = entity.createStoredProcedureQuery("p_1");//得到存储过程实体类
        procedureQuery.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);//参数结构  一个输入参数
        procedureQuery.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);//参数结构  输出参数
        procedureQuery.setParameter(1, id); // 传入输入参数
        procedureQuery.execute(); //执行存储过程

        List<Object[]> list = procedureQuery.getResultList(); //结果集存入list
        List<Article> alist = new ArrayList<>();

        list.forEach((s) -> { // 对查询的结果集进行数据处理
            Article article = new Article();
            article.setId(Integer.parseInt(s[0].toString()));
            article.setRootid(Integer.parseInt(s[1].toString()));
            article.setTitle(s[2].toString());
            article.setContent(s[3].toString());
            User1 user = new User1();
            user.setUserid(Integer.parseInt(s[4].toString()));
            article.setUser(user);
            try {
                java.util.Date d = new SimpleDateFormat("yyyy-mm-dd").parse(s[5].toString());
                article.setDatatime(new java.sql.Date(d.getTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            alist.add(article);
        });
        map.put("title", procedureQuery.getOutputParameterValue(2)); //输出参数为一个主贴
        map.put("list", alist); // 该主贴下的所有从贴
        return map;  // 返回结果集
    }

}
