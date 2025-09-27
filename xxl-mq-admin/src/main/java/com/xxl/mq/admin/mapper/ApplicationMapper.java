package com.xxl.mq.admin.mapper;

import com.xxl.mq.admin.model.entity.Application;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* Application Mapper
*
* Created by xuxueli on '2024-12-07 16:55:27'.
*/
@Mapper
public interface ApplicationMapper {

    /**
    * 新增
    */
    public int insert(@Param("application") Application application);

    /**
     * 插入（防重复）
     */
    public int insertIgnoreRepeat(@Param("application") Application application);

    /**
    * 删除
    */
    public int delete(@Param("ids") List<Integer> ids);

    /**
    * 更新
    */
    public int update(@Param("application") Application application);

    /**
     * 更新注册数据
     */
    public int  updateRegistryData(@Param("application") Application application);

    /**
    * Load查询
    */
    public Application load(@Param("id") int id);

    /**
     * Load查询 with appname
     */
    public Application loadByAppName(@Param("appname") String appname);

    /**
    * 分页查询Data
    */
	public List<Application> pageList(@Param("offset") int offset,
                                      @Param("pagesize") int pagesize,
                                      @Param("appname") String appname,
                                      @Param("name") String name);

    /**
    * 分页查询Count
    */
    public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("appname") String appname,
                             @Param("name") String name);

    /**
     * find all
     */
    public List<Application> findAll();

    /**
     * 查询关联topic数量
     */
    public int queryRelateTopicCoutByIds(@Param("ids") List<Integer> ids);

}
