package com.tao.service;

import com.tao.dao.articleDao;
import com.tao.po.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author liutao
 * @date 2017/12/9  15:45
 */

@Service
@Transactional
public class articleServiceImpl {

    @Autowired
    private articleDao dao;

    public Page<Article> findAll(Pageable pageable, int num) {
        return dao.findAll(pageable, num);
    }

    public int deleteZT(Integer id) {
        return dao.deleteZT(id);
    }
}
