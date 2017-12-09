package com.tao.dao;

import com.tao.po.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author liutao
 * @date 2017/12/9  15:47
 */

public interface articleDao extends CrudRepository<Article, Integer> {

    /**
     * @param pb
     * @param rid
     * @return
     */
    @Query("select a from Article a where a.rootid=:rid")
    Page<Article> findAll(Pageable pb, @Param("rid") Integer rid);
}
