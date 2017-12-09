package com.tao.dao;

import com.tao.po.User1;
import org.springframework.data.jpa.repository.Modifying;
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

    /**
     *  根据用户设定默认行数修改数据库的pagenum
     * @param user
     * @return
     */
    /**
     * 说明：
     * 在hibernate中支持传参数为对象操作
     * 在参数使用的时候
     * 格式为：   :#{#对象.属性}
     * 查了很久，最终在spring官网才查到
     * 属于springdata jpa下的事务操作
     */
    @Modifying
    @Query("update User1 set pagenum=:#{#user.pagenum} where userid =:#{#user.userid}")
    int updatePageNumById(@Param("user") User1 user);
}
