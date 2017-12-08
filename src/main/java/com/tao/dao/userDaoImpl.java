package com.tao.dao;

import com.tao.po.User1;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface userDaoImpl extends CrudRepository<User1, Integer> {
    /**
     * 根据用户名密码查询该是否存在该用户
     *
     * @param username
     * @param password
     * @return User对象
     */
    @Query("select c from User1  c where username =:u and password=:p")
    User1 login(@Param("u") String username, @Param("p") String password);


    @Override
    User1 save(User1 user);

    /**
     * 根据用户的id去查找用户的头像
     *
     * @param id 用户id
     * @return User对象
     */
    @Override
    User1 findOne(Integer id);
}
